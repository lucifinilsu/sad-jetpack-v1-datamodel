package com.sad.jetpack.v1.datamodel.api.extension.engine;

import android.text.TextUtils;

import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;

import org.jsoup.Connection;

public class JsoupEngineForStringByStringBodyModel extends JsoupEngineForStringModel<String> {

    @Override
    public void onResetJsoupConnection(IDataModelRequest<String> request, Connection connection) {
        String body= request.body();
        if (!TextUtils.isEmpty(body)){
            connection.requestBody(body);
        }
    }
}
