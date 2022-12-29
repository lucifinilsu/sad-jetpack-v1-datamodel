package com.sad.jetpack.v1.datamodel.api.extension.engine;

import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;

import org.jsoup.Connection;

import java.util.Map;

public class JsoupEngineForStringByFormBody extends JsoupEngineForString {

    @Override
    public void onResetJsoupConnection(IDataModelRequest request, Connection connection) {
        Map<String,String> body=request.body();
        if (body!=null){
            connection.data(body);
        }
    }
}
