package com.sad.jetpack.v1.datamodel.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.sad.jetpack.v1.datamodel.api.DataModelProducerImpl;
import com.sad.jetpack.v1.datamodel.api.DataModelProviders;
import com.sad.jetpack.v1.datamodel.api.DataModelRequestImpl;
import com.sad.jetpack.v1.datamodel.api.DataSource;
import com.sad.jetpack.v1.datamodel.api.DefaultDataModel;
import com.sad.jetpack.v1.datamodel.api.IDataModel;
import com.sad.jetpack.v1.datamodel.api.IDataModelObtainedCallback;
import com.sad.jetpack.v1.datamodel.api.IDataModelObtainedExceptionListener;
import com.sad.jetpack.v1.datamodel.api.IDataModelProducer;
import com.sad.jetpack.v1.datamodel.api.IDataModelProducerFactory;
import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;
import com.sad.jetpack.v1.datamodel.api.IDataModelResponse;
import com.sad.jetpack.v1.datamodel.api.extension.cache.CacheUtil;
import com.sad.jetpack.v1.datamodel.api.extension.engine.OkhttpEngineForStringByStringBody;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.DefaultCacheLoader;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.DefaultStringCacheDataModelInterceptor;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.LogDataModelInterceptor;
import com.sad.jetpack.v1.datamodel.api.utils.LogcatUtils;

import java.util.Observable;
import java.util.Observer;

public class SecondActivity extends AppCompatActivity {
    private TextView tv_console;
    private IDataModel dataModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        dataModel=DataModelProviders.get("xxxx");
        initView();
    }
    private void initView(){
        tv_console=findViewById(R.id.console2);
        //观察者
        dataModel.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                LogcatUtils.e("观察者接收到变化,arg="+arg);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}