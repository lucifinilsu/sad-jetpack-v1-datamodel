package com.sad.jetpack.v1.datamodel.api.extension.engine;

import com.sad.jetpack.v1.datamodel.api.DataSource;
import com.sad.jetpack.v1.datamodel.api.IDataModelChainOutput;
import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;
import com.sad.jetpack.v1.datamodel.api.IDataModelResponse;
import com.sad.jetpack.v1.datamodel.api.DataModelResponseImpl;

import org.jsoup.Connection;

public class JsoupEngineForString extends JsoupEngine<String> {
    @Override
    public void onHandleJsoupResponse(IDataModelRequest request, Connection.Response jsoupResponse, IDataModelChainOutput< String> chainOutput) {
        int code=jsoupResponse.statusCode();
        DataSource dataSource= DataSource.NET;
        IDataModelResponse<String> netDataResponse= DataModelResponseImpl.<String>newCreator()
                .code(code)
                .body(jsoupResponse.body())
                .dataSource(dataSource)
                .request(request)
                .headers(jsoupResponse.headers())
                .create();
        //Log.e("sad-jetpack-v1","---------------->开始调用输出链");
        chainOutput.proceed(netDataResponse);
    }
}
