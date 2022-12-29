package com.sad.jetpack.v1.datamodel.api;

public interface IDataModelObtainedCallback<RP> {

    void onDataObtainedCompleted(IDataModelResponse<RP> response);

}
