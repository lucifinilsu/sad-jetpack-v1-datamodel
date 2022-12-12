package com.sad.jetpack.v1.datamodel.api;

public interface IDataModelChainInput<RQ,RP> {

    IDataModelRequest<RQ> request();

    void proceed(IDataModelRequest<RQ> request);

    IDataModelObtainedCallback<RQ,RP> callback();

    IDataModelObtainedExceptionListener<RQ> exceptionListener();

    default void proceed(){
        proceed(request());
    }
}
