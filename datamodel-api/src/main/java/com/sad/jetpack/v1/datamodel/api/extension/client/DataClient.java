package com.sad.jetpack.v1.datamodel.api.extension.client;

import android.content.Context;

import com.sad.jetpack.v1.datamodel.api.DataModelRequestImpl;
import com.sad.jetpack.v1.datamodel.api.IDataModelProducer;
import com.sad.jetpack.v1.datamodel.api.IDataModelProducerFactory;
import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;

public abstract class DataClient<D extends DataClient<D>> implements IDataModelProducerFactory {

    protected IDataModelRequest.Creator requestCreator=null;
    private boolean internalLog=true;

    public boolean isInternalLog() {
        return internalLog;
    }

    public D log(boolean internalLog){
        this.internalLog=internalLog;
        return (D) this;
    }


    public IDataModelRequest.Creator getRequestCreator() {
        return requestCreator;
    }

    protected DataClient(){
        this.requestCreator= DataModelRequestImpl.newCreator();
    }


    public D url(String url){
        this.requestCreator.url(url);
        return (D) this;
    }

    public D method(IDataModelRequest.Method method){
        this.requestCreator.method(method);
        return (D) this;
    }

    public D params(Object s){
        this.requestCreator.body(s);
        return (D) this;
    }

    public D request(IDataModelRequest request){
        this.requestCreator=request.toCreator();
        return (D) this;
    }
}
