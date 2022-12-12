package com.sad.jetpack.v1.datamodel.api;

public interface IDataModelInterceptorOutput<RQ,RP> {

    void onInterceptedOutput(IDataModelChainOutput<RQ,RP> chainOutput) throws Exception;

}
