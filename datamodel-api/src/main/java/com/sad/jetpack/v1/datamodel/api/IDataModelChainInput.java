package com.sad.jetpack.v1.datamodel.api;

import androidx.annotation.NonNull;

public interface IDataModelChainInput<RP> {

    int currIndex();

    IDataModelRequest request();

    IDataModelResponse<RP> cacheResponse();

    void proceed(@NonNull IDataModelRequest request, IDataModelResponse<RP> cacheResponse,int index);

    IDataModelObtainedCallback<RP> callback();

    IDataModelObtainedExceptionListener exceptionListener();

    default void proceed(){
        proceed(request(),cacheResponse(),currIndex());
    }
}
