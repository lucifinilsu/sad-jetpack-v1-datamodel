package com.sad.jetpack.v1.datamodel.api.extension.client.socket.ipc;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.sad.jetpack.v1.datamodel.api.utils.AddressUtils;
import com.sad.jetpack.v1.datamodel.api.utils.LogcatUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

final class IPCServerLogic implements Runnable {
    public static final int SERVER_PORT=10176;
    protected static final Map<String, Socket> CLIENT_LIST = new HashMap<>();
    private int port = SERVER_PORT;
    private boolean isExit = false;
    private ServerSocket server;
    private Context context;
    private IPCServerConnectionListener connectionListener;

    public IPCServerConnectionListener getConnectionListener() {
        return connectionListener;
    }

    public void setConnectionListener(IPCServerConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }

    public int getPort() {
        return port;
    }

    public ServerSocket getServer() {
        return server;
    }
    public IPCServerLogic(Context context){
        this(context,SERVER_PORT);
    }
    public IPCServerLogic(Context context, int port) {
        this.context = context;
        this.port=port;
        try {
            server = new ServerSocket(port);
            LogcatUtils.e("server信息" + "ip:" + server.getLocalSocketAddress()/*AddressUtils.getLocalIP()*/ + ",port:" + port);
        } catch (IOException e) {
            LogcatUtils.e("启动server失败，错误原因：" + e.getMessage());
        }
    }


    // 群发的方法
    public static boolean sendMsgAll(String msg){
        try {
            for (Socket socket : CLIENT_LIST.values()) {
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(msg.getBytes("utf-8"));
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private class MessageDispatcher implements Runnable{
        private boolean clientSocketIsExit=false;
        private String address="";
        private Socket socket;

        public MessageDispatcher(String address, Socket socket) {
            this.address = address;
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // 单线程索锁
                synchronized (this){
                    // 放进到Map中保存
                    CLIENT_LIST.put(address,socket);
                }
                // 定义输入流
                InputStream inputStream = socket.getInputStream();
                while (!clientSocketIsExit && !isExit){
                    StringBuilder sb_header=new StringBuilder();
                    // 获取消息头长度
                    long headerLength = MessagePacketUtils.getHeaderSize(inputStream);
                    //定义消息头数组
                    byte[] headerBytes=new byte[(int) headerLength];
                    //每次实际读取的字节数
                    int headerReadCount=0;
                    //消息头读取进度
                    int headerReadIndex=0;
                    // 循环接收消息头
                    while (headerReadIndex <= (headerLength - 1) &&
                            (headerReadCount = inputStream.read(headerBytes, headerReadIndex, (int) headerLength-headerReadIndex)) != -1) {
                        headerReadIndex += headerReadCount;
                        sb_header.append(headerBytes);
                    }
                    String header=sb_header.toString();
                    //headerReadIndex = 0;
                    IPCMessageHeader messageHeader=IPCMessageHeader.parse(header);
                    //调一下回调
                    if (connectionListener!=null){
                        connectionListener.onReceivedMessageHeader(messageHeader);
                    }
                    //这里可以根据消息头来判断是否合法，这里暂时不看，后续加强。
                    //取出消息实体的长度
                    long bodySize=messageHeader.getBodySize();
                    //根据消息长度来遍历流，2023-2-22这里先不继续写了，暂时先发布一个版本供上层调用






                    byte[] buffer = new byte[4096];
                    int len;
                    StringBuilder sb=new StringBuilder();
                    while ((len = inputStream.read(buffer)) != -1){
                        String text = new String(buffer,0,len);
                        LogcatUtils.e("收到的数据为：" + text);
                        sb.append(text);
                    }
                    String msg=sb.toString();
                    if (!TextUtils.isEmpty(msg)){
                        // 在这里,要区分一下全部发消息和部分发消息，交给回调处理吧
                        sendMsgAll(sb.toString());
                    }

                }



            }catch (Exception e){
                e.printStackTrace();
                LogcatUtils.e("错误信息为：" + e.getMessage());
                stop();

            }finally {
                synchronized (this){
                    LogcatUtils.e("关闭链接：" + address);
                    CLIENT_LIST.remove(address);
                }
            }
        }

        public void stop(){
            clientSocketIsExit = true;
            if (socket != null){
                try {
                    CLIENT_LIST.remove(address);
                    socket.close();
                    LogcatUtils.e("已关闭sockent:"+address);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void run() {
        if (server!=null){
            try {
                saveLocalAddress(context);
                while (!isExit) {
                    // 进入等待环节
                    LogcatUtils.e("等待client的连接... ... ");
                    final Socket socket = server.accept();//阻塞
                    // 获取手机连接的地址及端口号
                    final String address = socket.getRemoteSocketAddress().toString();
                    LogcatUtils.e("连接成功，连接的client为：" + address);
                    new Thread(new MessageDispatcher(address,socket)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop(){
        isExit = true;
        if (server != null){
            try {
                server.close();
                LogcatUtils.e("已关闭server");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected static void saveLocalAddress(Context context){
        try {
            String ip= AddressUtils.getLocalIP();
            int port=SERVER_PORT;
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("ip",ip);
            jsonObject.put("port",port);
            saveLocalAddress(context,jsonObject);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    protected static void saveLocalAddress(Context context, JSONObject jsonObject){
        SharedPreferences sharedPreferences=context.getSharedPreferences("SAD_IPC_LIGHT",Context.MODE_MULTI_PROCESS|Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("address",jsonObject.toString()).commit();
    }

    public static JSONObject getServerAddress(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences("SAD_IPC_LIGHT",Context.MODE_MULTI_PROCESS|Context.MODE_PRIVATE);
        String s=sharedPreferences.getString("address","");
        if (!TextUtils.isEmpty(s)){
            try {
                JSONObject jsonObject=new JSONObject(s);
                return jsonObject;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String getServerIP(Context context){
        JSONObject jsonObject=getServerAddress(context);
        if (jsonObject!=null){
            jsonObject.optJSONObject("ip");
        }
        return "";
    }
}
