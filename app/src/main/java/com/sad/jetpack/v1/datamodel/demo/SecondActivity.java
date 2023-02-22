package com.sad.jetpack.v1.datamodel.demo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sad.jetpack.v1.datamodel.api.DataModelProviders;
import com.sad.jetpack.v1.datamodel.api.IDataModel;
import com.sad.jetpack.v1.datamodel.api.IDataModelResponse;
import com.sad.jetpack.v1.datamodel.api.extension.client.socket.ipc.IPCServer;
import com.sad.jetpack.v1.datamodel.api.utils.LogcatUtils;

import java.util.Observable;
import java.util.Observer;

public class SecondActivity extends AppCompatActivity {
    private TextView tv_console;
    private IDataModel dataModel;
    private Observer observer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        dataModel=DataModelProviders.get("xxxx");
        initView();
        testClientSocket();
    }
    private void initView(){
        tv_console=findViewById(R.id.console2);

        if (dataModel!=null){
            IDataModelResponse<String> response=dataModel.get("xxx");
            if (response!=null){
                tv_console.setText(response.body());
            }
            //观察者
            observer=new Observer() {
                @Override
                public void update(Observable o, Object arg) {
                    LogcatUtils.e("观察者接收到变化,arg="+arg);
                    IDataModelResponse<String> responseNew= (IDataModelResponse<String>) arg;
                    tv_console.setText(responseNew.body());
                }
            };
            dataModel.addObserver(observer);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dataModel!=null){
            dataModel.deleteObserver(observer);
        }
    }

    private void testClientSocket(){
        LogcatUtils.e("client获取server的地址信息："+ IPCServer.getServerAddress(this).toString());
    }
}