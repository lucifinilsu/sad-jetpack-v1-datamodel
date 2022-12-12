package com.sad.jetpack.v1.datamodel.api.extension.engine;

import com.sad.jetpack.v1.datamodel.api.DataSource;
import com.sad.jetpack.v1.datamodel.api.IDataModelChainOutput;
import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;
import com.sad.jetpack.v1.datamodel.api.IDataModelResponse;
import com.sad.jetpack.v1.datamodel.api.DataModelResponseImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kotlin.Pair;
import okhttp3.Headers;
import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class OkhttpEngineForStringModel<RQ> extends OkHttpEngineModel<RQ,String> {
    /*@Override
    public void onRestOkhttpRequest(INetDataRequest<String> request, Request.Builder okhttpRequestBuilder) {
        if (request.method()== INetDataRequest.Method.POST){
            okhttpRequestBuilder=okhttpRequestBuilder.post(RequestBody.create(request.body(), MediaType.parse("application/json;charset=utf-8")));
        }
    }*/

    @Override
    public void onHandleOkhttpResponse(IDataModelRequest<RQ> request, Response response, IDataModelChainOutput<RQ, String> chainOutput) throws IOException {
        ResponseBody responseBody=response.body();
        int code=response.code();
        DataSource dataSource= DataSource.NET;
        IDataModelResponse.Creator<RQ,String> creator= DataModelResponseImpl.<RQ,String>newCreator()
            .code(code)
            .body(responseBody.string())
            .dataSource(dataSource)
            .request(request);
        Headers headers=response.headers();
        Map<String,String> h=new HashMap<>();
        Iterator<Pair<String, String>> iterator=headers.iterator();
        while (iterator.hasNext()){
            Pair<String, String> entityEntry=iterator.next();
            String k=entityEntry.getFirst();
            String v=entityEntry.getSecond();
            if (v!=null){
                h.put(k,v);
            }
        }
        creator.headers(h);
        IDataModelResponse<RQ,String> netDataResponse=creator.create();
        chainOutput.proceed(netDataResponse);
    }
}
