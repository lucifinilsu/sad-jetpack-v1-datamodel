package com.sad.jetpack.v1.datamodel.api;

public interface IDataModelInterceptorOutput<RP> {

    void onInterceptedOutput(IDataModelChainOutput<RP> chainOutput) throws Exception;

}
