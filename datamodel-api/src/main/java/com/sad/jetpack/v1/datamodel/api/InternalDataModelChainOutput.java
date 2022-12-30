package com.sad.jetpack.v1.datamodel.api;

import java.util.ArrayList;
import java.util.List;

public class InternalDataModelChainOutput<RP> implements IDataModelChainOutput<RP> {
    private IDataModelResponse<RP> response;
    private List<IDataModelInterceptorOutput<RP>> interceptorOutputs=new ArrayList<>();
    private int currIndex=-1;
    private IDataModelObtainedCallback<RP> callback;
    private IDataModelObtainedExceptionListener exceptionListener;
    private IDataModel dataModel;

    public InternalDataModelChainOutput(
            List<IDataModelInterceptorOutput<RP>> interceptorOutputs,
            IDataModelObtainedCallback<RP> callback,
            IDataModelObtainedExceptionListener exceptionListener,
            IDataModel dataModel
    ) {
        this.interceptorOutputs = interceptorOutputs;
        this.callback = callback;
        this.exceptionListener=exceptionListener;
        this.dataModel=dataModel;
    }

    @Override
    public IDataModelResponse<RP> response() {
        return response;
    }

    @Override
    public IDataModelObtainedCallback<RP> callback() {
        return this.callback;
    }

    @Override
    public IDataModelObtainedExceptionListener exceptionListener() {
        return this.exceptionListener;
    }


    @Override
    public void proceed(IDataModelResponse<RP> response) {
        this.response=response;
        this.currIndex++;
        if (currIndex>interceptorOutputs.size()-1){
            if (dataModel!=null){
                dataModel.setValue(response.request().tag(),response);
            }
            if (callback!=null){
                callback.onDataObtainedCompleted(response);
            }
            currIndex=-1;
        }
        else {
            IDataModelInterceptorOutput<RP> interceptorOutput=interceptorOutputs.get(currIndex);
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
