package com.sad.jetpack.v1.datamodel.api.extension.client.socket.ipc;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.sad.core.async.ISADTaskProccessListener;
import com.sad.core.async.SADTaskRunnable;
import com.sad.core.async.SADTaskSchedulerClient;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class IPCClient {

    private Socket socket;
    private Context context;

    public Socket getSocket() {
        return socket;
    }

    private IPCClient(Context context){
        this.context=context;
    }

    public static IPCClient newInstance(Context context){
        return new IPCClient(context);
    }

    public IPCClient connect(String ip,int port){
        SADTaskSchedulerClient.newInstance().execute(new SADTaskRunnable<String>("zzz", new ISADTaskProccessListener<String>() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onFail(Throwable throwable) {

            }

            @Override
            public void onCancel() {

            }
        }) {
            @Override
            public String doInBackground() throws Exception {
                socket = new Socket(ip, port);
                InputStream inputStream = socket.getInputStream();
                byte[] buffer = new byte[4096];
                int len;
                StringBuilder sb=new StringBuilder();
                while ((len = inputStream.read(buffer)) != -1) {
                    String data = new String(buffer, 0, len);
                    sb.append(data);
                }
                return sb.toString();
            }
        });
        return this;
    }











    private class ClientHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                //
                int localPort = socket.getLocalPort();
                String s= (String) msg.obj;
            }
        }
    }


    private class ClientThread extends Thread{

        private String serverIP ="";
        private int serverPort;
        private Context context;
        private boolean isExit=false;

        public ClientThread(Context context){
            this.context=context;
            JSONObject jsonObject=IPCServer.getServerAddress(context);
            if (jsonObject!=null){
                this.serverIP =jsonObject.optString("ip");
                this.serverPort =jsonObject.optInt("port");
            }
         }
        public ClientThread(Context context, String serverIP, int serverPort){
            this.context=context;
            this.serverIP = serverIP;
            this.serverPort = serverPort;
        }

        public void close(){
            try {
                isExit=true;
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            try {
                socket = new Socket(serverIP, serverPort);
                InputStream inputStream = socket.getInputStream();
                while (!isExit){
                    byte[] buffer = new byte[1024];
                    int len;
                    StringBuilder sb=new StringBuilder();
                    while ((len = inputStream.read(buffer)) != -1) {
                        String data = new String(buffer, 0, len);
                        sb.append(data);
                    }
                    String data=sb.toString();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
