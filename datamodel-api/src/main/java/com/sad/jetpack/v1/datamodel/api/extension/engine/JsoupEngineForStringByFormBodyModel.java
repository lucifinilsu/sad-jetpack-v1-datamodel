package com.sad.jetpack.v1.datamodel.api.extension.engine;

import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;

import org.jsoup.Connection;

import java.util.Map;

public class JsoupEngineForStringByFormBodyModel extends JsoupEngineForStringModel<Map<String,String>> {

    @Override
    public void onResetJsoupConnection(IDataModelRequest<Map<String, String>> request, Connection connection) {
        Map<String,String> body=request.body();
        if (body!=null){
            connection.data(body);
        }
    }
}
