package com.sad.jetpack.v1.datamodel.api.extension.engine;

import com.sad.jetpack.v1.datamodel.api.DataModelResponseImpl;
import com.sad.jetpack.v1.datamodel.api.DataSource;
import com.sad.jetpack.v1.datamodel.api.IDataModelChainOutput;
import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;
import com.sad.jetpack.v1.datamodel.api.IDataModelResponse;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JsoupEngineForXmlDocument<RQ> extends JsoupEngine<RQ, Document> {
    @Override
    public void onHandleJsoupResponse(IDataModelRequest<RQ> request, Connection.Response jsoupResponse, IDataModelChainOutput<RQ, Document> chainOutput) {
        int code=jsoupResponse.statusCode();
        DataSource dataSource= DataSource.NET;
        IDataModelResponse<RQ,Document> netDataResponse= DataModelResponseImpl.<RQ,Document>newCreator()
                .code(code)
                .body(Jsoup.parse(jsoupResponse.body()))
                .dataSource(dataSource)
                .request(request)
                .headers(jsoupResponse.headers())
                .create();
        //Log.e("sad-jetpack-v1","---------------->开始调用输出链");
        chainOutput.proceed(netDataResponse);
    }
}
