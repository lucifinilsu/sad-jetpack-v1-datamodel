package com.sad.jetpack.v1.datamodel.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sad.jetpack.v1.datamodel.api.DataModelProducerImpl;
import com.sad.jetpack.v1.datamodel.api.DataModelRequestImpl;
import com.sad.jetpack.v1.datamodel.api.DataSource;
import com.sad.jetpack.v1.datamodel.api.IDataModelObtainedCallback;
import com.sad.jetpack.v1.datamodel.api.IDataModelObtainedExceptionListener;
import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;
import com.sad.jetpack.v1.datamodel.api.IDataModelResponse;
import com.sad.jetpack.v1.datamodel.api.extension.cache.CacheUtil;
import com.sad.jetpack.v1.datamodel.api.extension.engine.OkhttpEngineForStringByStringBody;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.DefaultCacheLoader;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.LogDataModelInterceptor;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.DefaultStringCacheDataModelInterceptor;
import com.sad.jetpack.v1.datamodel.api.utils.LogcatUtils;

public class MainActivity extends AppCompatActivity {
    private AppCompatButton button_start;
    private TextView tv_console;
    private AppCompatButton button_cleanCache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DefaultCacheLoader.initCacheLoader(getApplicationContext());
        initView();
    }
    private void initView(){

        button_start=findViewById(R.id.startTest);
        tv_console=findViewById(R.id.console);
        button_cleanCache=findViewById(R.id.cleanCache);
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
    }
    private void test(){
        IDataModelRequest<String> request= DataModelRequestImpl.<String>newCreator()
                        .url("https://www.baidu.com")
                        .method(IDataModelRequest.Method.GET)
                        .tag("xxx")
                        .create();
        LogDataModelInterceptor logInterceptor=LogDataModelInterceptor.newInstance();
        DefaultStringCacheDataModelInterceptor<String> cacheInterceptor=new DefaultStringCacheDataModelInterceptor<>(this);
        DataModelProducerImpl.<String,String>newInstance()
                .addInputInterceptor(logInterceptor)
                .addInputInterceptor(cacheInterceptor)
                .addOutputInterceptor(logInterceptor)
                .addOutputInterceptor(cacheInterceptor)
                .request(request)
                .callback(new IDataModelObtainedCallback<String, String>() {
                    @Override
                    public void onDataObtainedCompleted(IDataModelResponse<String, String> response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(response.dataSource()== DataSource.NET){
                                    Toast.makeText(getApplicationContext(),"重新请求数据",Toast.LENGTH_SHORT).show();
                                }
                                tv_console.setText(response.body());

                            }
                        });
                    }
                })
                .exceptionListener(new IDataModelObtainedExceptionListener<String>() {
                    @Override
                    public void onDataObtainedException(IDataModelRequest<String> request, Throwable throwable) {
                        LogcatUtils.e("哎呦。报错了。");
                        throwable.printStackTrace();
                    }
                })
                .engine(new OkhttpEngineForStringByStringBody())
                .execute();
    }
}