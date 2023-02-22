package com.sad.jetpack.v1.datamodel.api.extension.client.socket.ipc;

import android.content.Context;

import com.sad.jetpack.v1.datamodel.api.utils.LogcatUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;

public class IPCServer {

    private static IPCServerLogic ipcServerLogic = null;

    private IPCServer(){

    }
    public static IPCServerLogic startServer(Context context){
        LogcatUtils.e("开启server");
        if (ipcServerLogic != null){
            showDown();
        }
        ipcServerLogic = new IPCServerLogic(context, IPCServerLogic.SERVER_PORT);
        new Thread(ipcServerLogic).start();
        LogcatUtils.e("开启server成功");
        return ipcServerLogic;
    }

    // 关闭所有server socket 和 清空Map
    public static void showDown(){
        for (Socket socket : IPCServerLogic.CLIENT_LIST.values()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ipcServerLogic.stop();
        IPCServerLogic.CLIENT_LIST.clear();
    }

    public static JSONObject getServerAddress(Context context){
        return IPCServerLogic.getServerAddress(context);
    }



}
