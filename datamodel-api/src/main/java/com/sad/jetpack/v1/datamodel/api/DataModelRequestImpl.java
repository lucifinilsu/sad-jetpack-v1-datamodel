package com.sad.jetpack.v1.datamodel.api;


import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;

public class DataModelRequestImpl<B> implements IDataModelRequest<B>, IDataModelRequest.Creator<B> {
    private static final int DEFAULT_TIMEOUT=5000;
    private B body =null;
    private Map<String,String> headers=new HashMap<>();
    private long timeout=DEFAULT_TIMEOUT;
    private String url="";
    private Method method=Method.GET;
    private String tag= RandomStringUtils.randomAlphabetic(8);
    
    private DataModelRequestImpl(){}
    
    public static <B> Creator<B> newCreator(){
        return new DataModelRequestImpl<B>();
    }

    @Override
    public String tag() {
        return this.tag;
    }

    @Override
    public String url() {
        return this.url;
    }

    @Override
    public B body() {
        return body;
    }

    @Override
    public Map<String, String> headers() {
        return headers;
    }

    @Override
    public long timeout() {
        return this.timeout;
    }

    @Override
    public Method method() {
        return this.method;
    }

    @Override
    public Creator<B> toCreator() {
        return this;
    }

    @Override
    public Creator<B> url(String url) {
        this.url=url;
        return this;
    }

    @Override
    public Creator<B> body(B body) {
        this.body = body;
        return this;
    }

    @Override
    public Creator<B> headers(Map<String, String> headers) {
        this.headers=headers;
        return this;
    }

    @Override
    public Creator<B> addHeader(String key, String value) {
        this.headers.put(key,value);
        return this;
    }

    @Override
    public Creator<B> timeout(long timeout) {
        this.timeout=timeout;
        return this;
    }

    @Override
    public Creator<B> method(Method method) {
        this.method=method;
        return this;
    }

    @Override
    public Creator<B> tag(String tag) {
        this.tag=tag;
        return this;
    }

    @Override
    public DataModelRequestImpl<B> create() {
        return this;
    }
}
