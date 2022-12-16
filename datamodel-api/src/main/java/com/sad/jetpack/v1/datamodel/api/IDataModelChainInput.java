package com.sad.jetpack.v1.datamodel.api;

import androidx.annotation.NonNull;

public interface IDataModelChainInput<RQ,RP> {

    int currIndex();

    IDataModelRequest<RQ> request();

    IDataModelResponse<RQ,RP> cacheResponse();

    void proceed(@NonNull IDataModelRequest<RQ> request, IDataModelResponse<RQ,RP> cacheResponse,int index);

    IDataModelObtainedCallback<RQ,RP> callback();

    IDataModelObtainedExceptionListener<RQ> exceptionListener();

    default void proceed(){
        proceed(request(),cacheResponse(),currIndex());
    }
}
