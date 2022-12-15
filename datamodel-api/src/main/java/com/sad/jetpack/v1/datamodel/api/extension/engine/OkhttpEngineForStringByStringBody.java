package com.sad.jetpack.v1.datamodel.api.extension.engine;

import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkhttpEngineForStringByStringBody extends OkhttpEngineForString<String> {
    @Override
    public void onRestOkhttpRequest(IDataModelRequest<String> request, Request.Builder okhttpRequestBuilder) {
        if (request.method()== IDataModelRequest.Method.POST){
            okhttpRequestBuilder=okhttpRequestBuilder.post(RequestBody.create(request.body(), MediaType.parse("application/json;charset=utf-8")));
        }
    }
}
