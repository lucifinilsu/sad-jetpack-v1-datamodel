package com.sad.jetpack.v1.datamodel.api.extension.client.socket;

import com.sad.jetpack.v1.datamodel.api.DataModelProducerImpl;
import com.sad.jetpack.v1.datamodel.api.IDataModelProducer;
import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;
import com.sad.jetpack.v1.datamodel.api.extension.client.DataClient;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.LogDataModelInterceptor;

public class WebSocketDataClient extends DataClient<WebSocketDataClient> implements ISocketMessenger {

    private OkHttpWebSocketEngine.IOkhttpWebSocketConnectionListener connectionListener;
    private OkHttpWebSocketEngine engine;
    private boolean overSocket=true;//同tag是否重新连接。
    private int retryTimes=2;

    public WebSocketDataClient retryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
        return this;
    }

    public WebSocketDataClient overSocket(boolean overSocket){
        this.overSocket=overSocket;
        return this;
    }

    private WebSocketDataClient(OkHttpWebSocketEngine.IOkhttpWebSocketConnectionListener connectionListener){
        super();
        this.connectionListener=connectionListener;
        this.engine=new OkHttpWebSocketEngine(connectionListener);
    }

    public static WebSocketDataClient newInstance(OkHttpWebSocketEngine.IOkhttpWebSocketConnectionListener connectionListener){
        return new WebSocketDataClient(connectionListener);
    }

    @Override
    public IDataModelProducer<String> dataModelProducer(String tagAndClientKey) {
        IDataModelRequest request=requestCreator.tag(tagAndClientKey).create();
        LogDataModelInterceptor logInterceptor = LogDataModelInterceptor.newInstance();
        IDataModelProducer<String> dataModelProducer = DataModelProducerImpl.<String>newInstance();
        if (isInternalLog()){
            dataModelProducer.addInputInterceptor(logInterceptor).addOutputInterceptor(logInterceptor);
        }
        engine.setOverSocket(this.overSocket);
        engine.setRetryTimes(this.retryTimes);
        dataModelProducer
                .request(request)
                .engine(engine);
        return dataModelProducer;
    }

    @Override
    public boolean connect(boolean isRetry) {
        if (engine!=null){
            return engine.connect(isRetry);
        }
        return false;
    }

    @Override
    public boolean sendMsg(String msg) {
        if (engine!=null){
            return engine.sendMsg(msg);
        }
        return false;
    }

    @Override
    public void close() {
        if (engine!=null){
            engine.close();
        }
    }
}
