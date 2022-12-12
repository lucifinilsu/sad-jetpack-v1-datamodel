package com.sad.jetpack.v1.datamodel.api;

import java.util.Map;

public interface IDataModelResponse<RQ,RP> {

    IDataModelRequest<RQ> request();

    int code();

    RP body();

    DataSource dataSource();

    Map<String,String> headers();

    Creator<RQ,RP> toCreator();

    interface Creator<RQ,RP>{

        Creator<RQ,RP> code(int code);

        Creator<RQ,RP> body(RP body);

        Creator<RQ,RP> headers(Map<String,String> headers);

        Creator<RQ,RP> addHeader(String key, String value);

        Creator<RQ,RP> request(IDataModelRequest request);

        Creator<RQ,RP> dataSource(DataSource dataSource);

        IDataModelResponse<RQ,RP> create();
    }



}
