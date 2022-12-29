package com.sad.jetpack.v1.datamodel.api;


import androidx.annotation.NonNull;

public interface IDataModelChainOutput<RP> {

    @NonNull
    IDataModelResponse<RP> response();

    IDataModelObtainedCallback<RP> callback();

    IDataModelObtainedExceptionListener exceptionListener();

    void proceed(IDataModelResponse<RP> response);

    default void proceed(){
        proceed(response());
    }
}
