package com.sad.jetpack.v1.datamodel.api;

public interface IDataModelObtainedExceptionListener {

    void onDataObtainedException(IDataModelRequest request, Throwable throwable);

}
