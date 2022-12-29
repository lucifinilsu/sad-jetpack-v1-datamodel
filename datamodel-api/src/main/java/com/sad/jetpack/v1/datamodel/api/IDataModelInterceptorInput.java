package com.sad.jetpack.v1.datamodel.api;

public interface IDataModelInterceptorInput<RP> {

    void onInterceptedInput(IDataModelChainInput<RP> chainInput) throws Exception;

}
