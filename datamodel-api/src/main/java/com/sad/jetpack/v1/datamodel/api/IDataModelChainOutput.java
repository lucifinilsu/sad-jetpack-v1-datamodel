package com.sad.jetpack.v1.datamodel.api;


import androidx.annotation.NonNull;

public interface IDataModelChainOutput<RQ,RP> {

    @NonNull
    IDataModelResponse<RQ,RP> response();

    IDataModelObtainedCallback<RQ,RP> callback();

    IDataModelObtainedExceptionListener<RQ> exceptionListener();

    void proceed(IDataModelResponse<RQ,RP> response);

    default void proceed(){
        proceed(response());
    }
}
