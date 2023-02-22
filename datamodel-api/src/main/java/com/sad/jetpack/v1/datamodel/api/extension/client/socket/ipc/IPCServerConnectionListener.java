package com.sad.jetpack.v1.datamodel.api.extension.client.socket.ipc;

public interface IPCServerConnectionListener {

    /*void onConnected();

    void sendMsg(String s);

    void close();*/

    void onReceivedMessageHeader(IPCMessageHeader header);

}
