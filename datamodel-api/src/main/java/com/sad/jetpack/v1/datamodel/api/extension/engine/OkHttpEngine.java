package com.sad.jetpack.v1.datamodel.api.extension.engine;

import androidx.annotation.NonNull;

import com.sad.jetpack.v1.datamodel.api.IDataModelChainOutput;
import com.sad.jetpack.v1.datamodel.api.IDataModelProductEngine;
import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public abstract class OkHttpEngine<RQ,RP> implements IDataModelProductEngine<RQ,RP> {
    @Override
    public void onEngineExecute(IDataModelRequest<RQ> request, IDataModelChainOutput<RQ, RP> chainOutput) throws Exception {
        Request.Builder okhttpRequestBuilder=new Request.Builder();
        okhttpRequestBuilder.url(request.url());
        if (request.method()== IDataModelRequest.Method.GET){
            okhttpRequestBuilder=okhttpRequestBuilder.get();
        }
        onRestOkhttpRequest(request,okhttpRequestBuilder);
        OkHttpClient.Builder builder=new OkHttpClient.Builder()
                .callTimeout(request.timeout(), TimeUnit.MILLISECONDS)
                .connectTimeout(request.timeout(), TimeUnit.MILLISECONDS)
                .followRedirects(true);
        onResetOkhttpClient(request,builder);
        OkHttpClient okHttpClient=builder.build();
        okHttpClient.newCall(okhttpRequestBuilder.build())
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        if (chainOutput.exceptionListener()!=null){
                            chainOutput.exceptionListener().onDataObtainedException(request,e);
                        }
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                        onHandleOkhttpResponse(request,response,chainOutput);
                    }
                });
    }
    public void onResetOkhttpClient(IDataModelRequest<RQ> request, OkHttpClient.Builder builder){

    }
    public abstract void onRestOkhttpRequest(IDataModelRequest<RQ> request, Request.Builder okhttpRequestBuilder);
    public abstract void onHandleOkhttpResponse(IDataModelRequest<RQ> request, Response response, IDataModelChainOutput<RQ, RP> chainOutput) throws IOException;
}
