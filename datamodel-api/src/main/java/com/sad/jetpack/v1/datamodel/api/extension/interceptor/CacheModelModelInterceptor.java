package com.sad.jetpack.v1.datamodel.api.extension.interceptor;

import com.sad.jetpack.v1.datamodel.api.IDataModelChainInput;
import com.sad.jetpack.v1.datamodel.api.IDataModelChainOutput;
import com.sad.jetpack.v1.datamodel.api.IDataModelInterceptorInput;
import com.sad.jetpack.v1.datamodel.api.IDataModelInterceptorOutput;

public abstract class CacheModelModelInterceptor<RQ,String> implements IDataModelInterceptorInput<RQ,String>, IDataModelInterceptorOutput<RQ,String> {
    @Override
    public void onInterceptedInput(IDataModelChainInput<RQ, String> chainInput) throws Exception {

    }

    @Override
    public void onInterceptedOutput(IDataModelChainOutput<RQ, String> chainOutput) throws Exception {

    }
}
