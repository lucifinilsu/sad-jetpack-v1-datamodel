package com.sad.jetpack.v1.datamodel.api;

import java.util.ArrayList;
import java.util.List;

public class InternalDataModelChainOutput<RQ,RP> implements IDataModelChainOutput<RQ,RP> {
    private IDataModelResponse<RQ, RP> response;
    private List<IDataModelInterceptorOutput<RQ,RP>> interceptorOutputs=new ArrayList<>();
    private int currIndex=-1;
    private IDataModelObtainedCallback<RQ,RP> callback;
    private IDataModelObtainedExceptionListener<RQ> exceptionListener;

    public InternalDataModelChainOutput(
            List<IDataModelInterceptorOutput<RQ, RP>> interceptorOutputs,
            IDataModelObtainedCallback<RQ, RP> callback,
            IDataModelObtainedExceptionListener<RQ> exceptionListener
    ) {
        this.interceptorOutputs = interceptorOutputs;
        this.callback = callback;
        this.exceptionListener=exceptionListener;
    }

    @Override
    public IDataModelResponse<RQ, RP> response() {
        return response;
    }

    @Override
    public IDataModelObtainedCallback<RQ, RP> callback() {
        return this.callback;
    }

    @Override
    public IDataModelObtainedExceptionListener<RQ> exceptionListener() {
        return this.exceptionListener;
    }


    @Override
    public void proceed(IDataModelResponse<RQ, RP> response) {
        this.response=response;
        this.currIndex++;
        if (currIndex>interceptorOutputs.size()-1){
            if (callback!=null){
                callback.onDataObtainedCompleted(response);
            }
        }
        else {
            IDataModelInterceptorOutput<RQ,RP> interceptorOutput=interceptorOutputs.get(currIndex);
            try {
                interceptorOutput.onInterceptedOutput(this);
            }catch (Exception e){
                e.printStackTrace();
                if (exceptionListener!=null){
                    exceptionListener.onDataObtainedException(response!=null?response.request():null,e);
                }
            }

        }
    }
}
