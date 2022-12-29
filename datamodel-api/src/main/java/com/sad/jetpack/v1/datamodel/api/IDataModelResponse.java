package com.sad.jetpack.v1.datamodel.api;

import java.util.Map;

public interface IDataModelResponse<RP> {

    IDataModelRequest request();

    int code();

    RP body();

    DataSource dataSource();

    Map<String,String> headers();

    //IDataModelResponse<RP> cacheResponse();

    Creator<RP> toCreator();

    interface Creator<RP>{

        Creator<RP> code(int code);

        Creator<RP> body(RP body);

        Creator<RP> headers(Map<String,String> headers);

        Creator<RP> addHeader(String key, String value);

        Creator<RP> request(IDataModelRequest request);

        Creator<RP> dataSource(DataSource dataSource);

        //Creator<RP> cacheResponse(IDataModelResponse<RP> cacheResponse);

        IDataModelResponse<RP> create();
    }



}
