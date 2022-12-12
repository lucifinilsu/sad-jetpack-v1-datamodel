package com.sad.jetpack.v1.datamodel.api;

public interface IDataModelInterceptorInput<RQ,RP> {

    void onInterceptedInput(IDataModelChainInput<RQ,RP> chainInput) throws Exception;

}
