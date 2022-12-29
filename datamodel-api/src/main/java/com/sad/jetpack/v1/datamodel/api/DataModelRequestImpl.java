package com.sad.jetpack.v1.datamodel.api;


import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;

public class DataModelRequestImpl implements IDataModelRequest, IDataModelRequest.Creator {
    private static final int DEFAULT_TIMEOUT=5000;
    private Object body =null;
    private Map<String,String> headers=new HashMap<>();
    private long timeout=DEFAULT_TIMEOUT;
    private String url="";
    private Method method=Method.GET;
    private String tag= RandomStringUtils.randomAlphabetic(8);
    
    private DataModelRequestImpl(){}
    
    public static Creator newCreator(){
        return new DataModelRequestImpl();
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
    public<B> B body() {
        return (B) body;
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
    public Creator toCreator() {
        return this;
    }

    @Override
    public Creator url(String url) {
        this.url=url;
        return this;
    }

    @Override
    public Creator body(Object body) {
        this.body = body;
        return this;
    }

    @Override
    public Creator headers(Map<String, String> headers) {
        this.headers=headers;
        return this;
    }

    @Override
    public Creator addHeader(String key, String value) {
        this.headers.put(key,value);
        return this;
    }

    @Override
    public Creator timeout(long timeout) {
        this.timeout=timeout;
        return this;
    }

    @Override
    public Creator method(Method method) {
        this.method=method;
        return this;
    }

    @Override
    public Creator tag(String tag) {
        this.tag=tag;
        return this;
    }

    @Override
    public DataModelRequestImpl create() {
        return this;
    }
}
