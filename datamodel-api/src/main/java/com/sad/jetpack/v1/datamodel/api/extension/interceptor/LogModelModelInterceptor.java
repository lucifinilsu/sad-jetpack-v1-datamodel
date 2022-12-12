package com.sad.jetpack.v1.datamodel.api.extension.interceptor;

import com.sad.jetpack.v1.datamodel.api.IDataModelChainInput;
import com.sad.jetpack.v1.datamodel.api.IDataModelChainOutput;
import com.sad.jetpack.v1.datamodel.api.IDataModelInterceptorInput;
import com.sad.jetpack.v1.datamodel.api.IDataModelInterceptorOutput;
import com.sad.jetpack.v1.datamodel.api.extension.LogcatUtils;

public class LogModelModelInterceptor implements IDataModelInterceptorInput<String,String>, IDataModelInterceptorOutput<String,String> {

    @Override
    public void onInterceptedInput(IDataModelChainInput<String, String> chainInput) throws Exception {
        LogcatUtils.e("DataCenter","[request-url]"+chainInput.request().url());
        LogcatUtils.e("DataCenter","[request-body]"+chainInput.request().body());
        LogcatUtils.e("DataCenter","[request-method]"+chainInput.request().method());
        LogcatUtils.e("DataCenter","[request-header]"+chainInput.request().headers());
        LogcatUtils.e("DataCenter","[request-tag]"+chainInput.request().tag());
        LogcatUtils.e("DataCenter","[request-timeout]"+chainInput.request().timeout());
        chainInput.proceed();
    }

    @Override
    public void onInterceptedOutput(IDataModelChainOutput<String, String> chainOutput) throws Exception {
        LogcatUtils.e("DataCenter","[response-url]"+ chainOutput.response().request().url());
        LogcatUtils.e("DataCenter","[response-body]"+ chainOutput.response().body());
        LogcatUtils.e("DataCenter","[response-code]"+ chainOutput.response().code());
        LogcatUtils.e("DataCenter","[response-source]"+ chainOutput.response().dataSource());
        LogcatUtils.e("DataCenter","[response-header]"+ chainOutput.response().headers());
        chainOutput.proceed();
    }
}
