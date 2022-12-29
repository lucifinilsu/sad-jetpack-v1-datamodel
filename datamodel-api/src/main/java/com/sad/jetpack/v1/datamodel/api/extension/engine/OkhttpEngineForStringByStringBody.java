package com.sad.jetpack.v1.datamodel.api.extension.engine;

import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkhttpEngineForStringByStringBody extends OkhttpEngineForString {
    @Override
    public void onRestOkhttpRequest(IDataModelRequest request, Request.Builder okhttpRequestBuilder) {
        if (request.method()== IDataModelRequest.Method.POST){
            okhttpRequestBuilder=okhttpRequestBuilder.post(RequestBody.create(request.body().toString(), MediaType.parse("application/json;charset=utf-8")));
        }
    }
}
