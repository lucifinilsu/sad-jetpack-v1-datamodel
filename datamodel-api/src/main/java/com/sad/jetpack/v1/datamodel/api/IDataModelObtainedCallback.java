package com.sad.jetpack.v1.datamodel.api;

public interface IDataModelObtainedCallback<RQ,RP> {

    void onDataObtainedCompleted(IDataModelResponse<RQ,RP> response);

}
