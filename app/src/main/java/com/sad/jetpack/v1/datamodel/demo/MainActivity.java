package com.sad.jetpack.v1.datamodel.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Lifecycle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.LogDataModelInterceptor;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.DefaultStringCacheDataModelInterceptor;
import com.sad.jetpack.v1.datamodel.api.extension.client.socket.ipc.IPCServer;
import com.sad.jetpack.v1.datamodel.api.utils.LogcatUtils;

public class MainActivity extends AppCompatActivity {
    private AppCompatButton button_start;
    //private TextView tv_console;
    private AppCompatButton button_cleanCache;
    private AppCompatButton button_ac2;
    private IDataModel dataModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DefaultCacheLoader.initCacheLoader(getApplicationContext());
        dataModel = new DefaultDataModel();
        configDataModel(dataModel);
        initView();
        testServerSocket();

    }

    private void initView() {
        button_start = findViewById(R.id.startTest);
        //tv_console = findViewById(R.id.console);
        button_cleanCache = findViewById(R.id.cleanCache);
        button_ac2 = findViewById(R.id.ac2);
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });
        button_cleanCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CacheUtil.clearAll();
            }
        });
        button_ac2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });

        FragmentMaster.with(getSupportFragmentManager())
                .prePare(new Fragment1())
                .choiceMode()
                .addAllToContainer(R.id.fragment_container1)
                .hideAll(Lifecycle.State.STARTED)
                .show(0, Lifecycle.State.RESUMED);
        FragmentMaster.with(getSupportFragmentManager())
                .prePare(new Fragment1())
                .choiceMode()
                .addAllToContainer(R.id.fragment_container2)
                .hideAll(Lifecycle.State.STARTED)
                .show(0, Lifecycle.State.RESUMED);
    }

    private void configDataModel(IDataModel dataModel) {
        IDataModelRequest request = DataModelRequestImpl.newCreator()
                .url("https://www.baidu.com")
                .method(IDataModelRequest.Method.GET)
                .tag("xxx")
                .create();
        LogDataModelInterceptor logInterceptor = LogDataModelInterceptor.newInstance();
        DefaultStringCacheDataModelInterceptor cacheInterceptor = new DefaultStringCacheDataModelInterceptor(this);
        IDataModelProducer<String> dataModelProducer = DataModelProducerImpl.<String>newInstance()
                .addInputInterceptor(logInterceptor)
                .addInputInterceptor(cacheInterceptor)
                .addOutputInterceptor(logInterceptor)
                .addOutputInterceptor(cacheInterceptor)
                .request(request)
                .callback(new IDataModelObtainedCallback<String>() {
                    @Override
                    public void onDataObtainedCompleted(IDataModelResponse<String> response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (response.dataSource() == DataSource.NET) {
                                    Toast.makeText(getApplicationContext(), "重新请求数据", Toast.LENGTH_SHORT).show();
                                }
                                //tv_console.setText(response.body());

                            }
                        });
                    }
                })
                .exceptionListener(new IDataModelObtainedExceptionListener() {
                    @Override
                    public void onDataObtainedException(IDataModelRequest request, Throwable throwable) {
                        LogcatUtils.e("哎呦。报错了。");
                        throwable.printStackTrace();
                    }
                })
                .engine(new OkhttpEngineForStringByStringBody());
        dataModel.producerFactory(new IDataModelProducerFactory() {
            @Override
            public IDataModelProducer dataModelProducer(String tagAndClientKey) {
                return dataModelProducer;
            }
        });
        DataModelProviders.register("xxxx",dataModel);
    }

    private void test() {
        dataModel.request("xxx");
    }

    private void testWebSocket(){

    }

    private void testServerSocket(){
        IPCServer.startServer(this);
    }
}