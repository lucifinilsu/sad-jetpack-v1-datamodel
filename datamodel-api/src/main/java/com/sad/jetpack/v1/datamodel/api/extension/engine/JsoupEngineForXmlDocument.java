package com.sad.jetpack.v1.datamodel.api.extension.engine;

import com.sad.jetpack.v1.datamodel.api.DataModelResponseImpl;
import com.sad.jetpack.v1.datamodel.api.DataSource;
import com.sad.jetpack.v1.datamodel.api.IDataModelChainOutput;
import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;
import com.sad.jetpack.v1.datamodel.api.IDataModelResponse;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JsoupEngineForXmlDocument extends JsoupEngine<Document> {
    @Override
    public void onHandleJsoupResponse(IDataModelRequest request, Connection.Response jsoupResponse, IDataModelChainOutput< Document> chainOutput) {
        int code=jsoupResponse.statusCode();
        DataSource dataSource= DataSource.NET;
        IDataModelResponse<Document> netDataResponse= DataModelResponseImpl.<Document>newCreator()
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
