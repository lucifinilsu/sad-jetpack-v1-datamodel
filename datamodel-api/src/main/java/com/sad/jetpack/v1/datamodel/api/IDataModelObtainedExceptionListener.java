package com.sad.jetpack.v1.datamodel.api;

public interface IDataModelObtainedExceptionListener<RQ> {

    void onDataObtainedException(IDataModelRequest<RQ> request, Throwable throwable);

}
