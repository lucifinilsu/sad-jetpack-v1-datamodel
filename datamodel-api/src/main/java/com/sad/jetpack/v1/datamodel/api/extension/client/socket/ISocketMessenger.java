package com.sad.jetpack.v1.datamodel.api.extension.client.socket;

public interface ISocketMessenger {

    boolean connect(boolean isRetry);

    boolean sendMsg(String msg);

    void close();
}
