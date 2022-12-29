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

public abstract class OkhttpEngineForString extends OkHttpEngine<String> {

    @Override
    public void onHandleOkhttpResponse(IDataModelRequest request, Response response, IDataModelChainOutput<String> chainOutput) throws IOException {
        ResponseBody responseBody=response.body();
        int code=response.code();
        DataSource dataSource= DataSource.NET;
        IDataModelResponse.Creator<String> creator= DataModelResponseImpl.<String>newCreator()
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
        IDataModelResponse<String> netDataResponse=creator.create();
        chainOutput.proceed(netDataResponse);
    }
}
