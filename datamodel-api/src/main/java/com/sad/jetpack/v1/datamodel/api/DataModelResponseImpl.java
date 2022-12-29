package com.sad.jetpack.v1.datamodel.api;

import java.util.HashMap;
import java.util.Map;

public class DataModelResponseImpl<RP> implements IDataModelResponse<RP>, IDataModelResponse.Creator<RP> {

    private IDataModelRequest request;
    //private IDataModelResponse<RP> cacheResponse;
    private int code=200;
    private RP body=null;
    private DataSource dataSource=DataSource.NET;
    private Map<String, String> headers=new HashMap<>();
    private DataModelResponseImpl(){}
    public static <RP> Creator<RP> newCreator(){
        return new DataModelResponseImpl<RP>();
    }
    @Override
    public IDataModelRequest request() {
        return this.request;
    }

    @Override
    public int code() {
        return this.code;
    }

    @Override
    public RP body() {
        return this.body;
    }

    @Override
    public DataSource dataSource() {
        return this.dataSource;
    }

    @Override
    public Map<String, String> headers() {
        return this.headers;
    }

    /*@Override
    public IDataModelResponse< RP> cacheResponse() {
        return cacheResponse;
    }*/

    @Override
    public Creator<RP> toCreator() {
        return this;
    }

    @Override
    public Creator<RP> code(int code) {
        this.code=code;
        return this;
    }

    @Override
    public Creator<RP> body(RP body) {
        this.body=body;
        return this;
    }

    @Override
    public Creator<RP> headers(Map<String, String> headers) {
        this.headers=headers;
        return this;
    }

    @Override
    public Creator<RP> addHeader(String key, String value) {
        this.headers.put(key,value);
        return this;
    }

    @Override
    public Creator<RP> request(IDataModelRequest request) {
        this.request=request;
        return this;
    }

    @Override
    public Creator<RP> dataSource(DataSource dataSource) {
        this.dataSource=dataSource;
        return this;
    }

    /*@Override
    public Creator< RP> cacheResponse(IDataModelResponse< RP> cacheResponse) {
        this.cacheResponse=cacheResponse;
        return this;
    }*/

    @Override
    public DataModelResponseImpl create() {
        return this;
    }
}
