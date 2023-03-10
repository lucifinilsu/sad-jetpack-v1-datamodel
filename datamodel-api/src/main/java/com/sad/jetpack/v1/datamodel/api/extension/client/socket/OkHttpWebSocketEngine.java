package com.sad.jetpack.v1.datamodel.api.extension.client.socket;

import com.sad.jetpack.v1.datamodel.api.DataModelResponseImpl;
import com.sad.jetpack.v1.datamodel.api.DataSource;
import com.sad.jetpack.v1.datamodel.api.IDataModelChainOutput;
import com.sad.jetpack.v1.datamodel.api.IDataModelObtainedExceptionListener;
import com.sad.jetpack.v1.datamodel.api.IDataModelProductEngine;
import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;
import com.sad.jetpack.v1.datamodel.api.IDataModelResponse;
import com.sad.jetpack.v1.datamodel.api.utils.LogcatUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import kotlin.Pair;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class OkHttpWebSocketEngine extends WebSocketListener implements IDataModelProductEngine<String>, ISocketMessenger {
    private static Map<String,WebSocket> mWebSocketMap=new HashMap<>();
    private static Map<String,OkHttpClient> mOkHttpClientMap=new HashMap<>();
    public final static int SOCKET_ALIVE_MODE_INIT=0;
    public final static int SOCKET_ALIVE_MODE_LIVE=1;
    private WebSocket mWebSocket=null;
    private OkHttpClient mOkHttpClient=null;
    private IDataModelResponse.Creator<String> responseCreator;
    private String requestMsg="";
    private IDataModelChainOutput<String> chainOutput;
    private IDataModelRequest dataModelRequest;
    private IOkhttpWebSocketConnectionListener connectionListener;

    private boolean overSocket=true;//同tag是否重新连接。
    private int retryTimes=2;
    private int currRetry=0;

    public IOkhttpWebSocketConnectionListener getConnectionListener() {
        return connectionListener;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public void setOverSocket(boolean overSocket) {
        this.overSocket = overSocket;
    }

    public boolean isOverSocket() {
        return overSocket;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setConnectionListener(IOkhttpWebSocketConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }

    public OkHttpWebSocketEngine(IOkhttpWebSocketConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }

    public static interface IOkhttpWebSocketConnectionListener{

        void onWebSocketConnectionAlive(String tag, ISocketMessenger messenger, int socketAliveMode);

        void onWebSocketMessageReceived(String tag, ISocketMessenger messenger,String text, int socketAliveMode);

        void onWebSocketConnectionShutDown(String tag,int code, String reason);

        void onWebSocketConnectionClosed(String tag,int code, String reason);
    }


    @Override
    public void onEngineExecute(IDataModelRequest dataModelRequest, IDataModelChainOutput<String> chainOutput) throws Exception {
        this.chainOutput=chainOutput;
        this.dataModelRequest=dataModelRequest;
        requestMsg=dataModelRequest.body()+"";
        responseCreator = DataModelResponseImpl.<String>newCreator()
                .request(dataModelRequest)
                .dataSource(DataSource.NET)
                ;
        connect(false);
    }


    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        currRetry=0;
        // WebSocket 连接建立
        mWebSocket=webSocket;
        mWebSocketMap.put(dataModelRequest.tag(),mWebSocket);
        responseCreator.code(response.code());
        Headers headers=response.headers();
        Map<String,String> h=new HashMap<>();
        Iterator<Pair<String, String>> iterator=headers.iterator();
        while (iterator.hasNext()){
            Pair<String, String> entityEntry=iterator.next();
            String k=entityEntry.getFirst();
            String v=entityEntry.getSecond();
            if (v!=null){
                h.put(k,v);
            }
        }
        responseCreator.headers(h);
        if (!response.isSuccessful()){
            chainOutput.proceed(responseCreator.create());
        }
        if (connectionListener!=null){
            connectionListener.onWebSocketConnectionAlive(dataModelRequest.tag(),this,SOCKET_ALIVE_MODE_INIT);
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        // 收到服务端发送来的 String 类型消息
        /**
         * 在这里对收到的信息进行处理
         */
        responseCreator.body(text);
        if (connectionListener!=null){
            connectionListener.onWebSocketMessageReceived(dataModelRequest.tag(),this,text,SOCKET_ALIVE_MODE_LIVE);
        }
        this.chainOutput.proceed(responseCreator.create());
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
        //客户端主动关闭时回调
        if (connectionListener!=null){
            connectionListener.onWebSocketConnectionShutDown(dataModelRequest.tag(),code,reason);
        }
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        try {
            // WebSocket 连接关闭
            mWebSocket.close(code,reason);
            mWebSocket.cancel();
            mWebSocketMap.remove(dataModelRequest.tag());
            mWebSocket=null;
        }catch (Exception e){
            e.printStackTrace();
        }
        if (connectionListener!=null){
            connectionListener.onWebSocketConnectionClosed(dataModelRequest.tag(),code,reason);
        }
    }

    public boolean connect(boolean isRetry){
        String k=dataModelRequest.tag();
        mWebSocket=mWebSocketMap.get(k);
        mOkHttpClient=mOkHttpClientMap.get(k);
        if (!isRetry){
            if (mOkHttpClient==null){
                mOkHttpClient = new OkHttpClient.Builder()
                        .pingInterval(40, TimeUnit.SECONDS) // 设置 PING 帧发送间隔---包活
                        .connectTimeout(dataModelRequest.timeout(),TimeUnit.MILLISECONDS)
                        .build();
                mOkHttpClientMap.put(k,mOkHttpClient);
                if (mWebSocket!=null && overSocket){
                    mWebSocketMap.remove(k);
                    mWebSocket.cancel();
                    mWebSocket.close(1001,"客户端覆盖关闭");
                    mWebSocket=null;
                }
            }

            if (mWebSocket==null){
                String url=dataModelRequest.url();
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                mOkHttpClient.newWebSocket(request,this);
            }
            else {
                if (connectionListener!=null){
                    connectionListener.onWebSocketConnectionAlive(k,this,SOCKET_ALIVE_MODE_LIVE);
                }
            }
        }
        else {
            if (mOkHttpClient==null){
                mOkHttpClient = new OkHttpClient.Builder()
                        .pingInterval(40, TimeUnit.SECONDS) // 设置 PING 帧发送间隔---包活
                        .connectTimeout(dataModelRequest.timeout(),TimeUnit.MILLISECONDS)
                        .build();
                mOkHttpClientMap.put(k,mOkHttpClient);
            }
            String url=dataModelRequest.url();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            mOkHttpClient.newWebSocket(request,this);

        }
        return false;
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        t.printStackTrace();
        // 出错了
        if(currRetry!=retryTimes){
            try {
                currRetry++;
                connect(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            mWebSocketMap.remove(dataModelRequest.tag());
            mOkHttpClientMap.remove(dataModelRequest.tag());
            if (mWebSocket!=null){
                mWebSocket.close(1001,"连接失败");
                mWebSocket.cancel();
            }
            mWebSocket=null;
            mOkHttpClient=null;
            IDataModelObtainedExceptionListener exceptionListener=chainOutput.exceptionListener();
            if (exceptionListener!=null){
                exceptionListener.onDataObtainedException(dataModelRequest,t);
            }
        }

    }

    @Override
    public void close(){
        try {
            if(mOkHttpClient!=null){
                mOkHttpClient.dispatcher().executorService().shutdown();
                mOkHttpClient=null;
            }
            if (mWebSocket!=null){
                mWebSocket.cancel();
                mWebSocket.close(1001,"客户端主动关闭");
                mWebSocket=null;
            }

            mOkHttpClientMap.remove(dataModelRequest.tag());
            mWebSocketMap.remove(dataModelRequest.tag());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean sendMsg(String m){
        if (mWebSocket!=null){
            return mWebSocket.send(m);
        }
        else {
            LogcatUtils.e("--------->mWebSocket==null!!!");
            return false;
        }
    }
}
