package com.sad.jetpack.v1.datamodel.api;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class InternalDataModelChainInput<RQ,RP> implements IDataModelChainInput<RQ,RP> {
    private IDataModelRequest<RQ> request;
    private List<IDataModelInterceptorInput<RQ,RP>> interceptorInputs=new ArrayList<>();
    private int currIndex=-1;
    private IDataModelObtainedCallback<RQ,RP> callback;
    private IDataModelObtainedExceptionListener<RQ> exceptionListener;
    private IDataModelResponse<RQ,RP> cacheResponse;

    public InternalDataModelChainInput(
            List<IDataModelInterceptorInput<RQ,RP>> interceptorInputs,
            IDataModelObtainedCallback<RQ, RP> callback,
            IDataModelObtainedExceptionListener<RQ> exceptionListener
    ) {
        this.interceptorInputs = interceptorInputs;
        this.callback = callback;
        this.exceptionListener=exceptionListener;
    }


    @Override
    public int currIndex() {
        return currIndex;
    }

    @Override
    public IDataModelRequest<RQ> request() {
        return this.request;
    }

    @Override
    public IDataModelResponse<RQ, RP> cacheResponse() {
        return this.cacheResponse;
    }


    @Override
    public void proceed(@NonNull IDataModelRequest<RQ> request, IDataModelResponse<RQ, RP> cacheResponse, int index) {
        this.request=request;
        this.cacheResponse=cacheResponse;
        this.currIndex=index;
        doProceed();
    }

    private void doProceed(){
        this.currIndex++;
        if (currIndex>interceptorInputs.size()-1){
            //几乎不会到达这里，因为最后一个是默认内部拦截器（引擎）
            Log.e("sad-jetpack-v1","---------------->输入链超界");
            return;
        }
        else {
            IDataModelInterceptorInput<RQ,RP> interceptorInput=interceptorInputs.get(currIndex);
            Log.e("sad-jetpack-v1","---------------->内部输入链执行："+interceptorInput);
            try {
                interceptorInput.onInterceptedInput(this);
            }catch (Exception e){
                e.printStackTrace();
                if (exceptionListener!=null){
                    exceptionListener.onDataObtainedException(request,e);
                }
            }

        }
    }

    @Override
    public IDataModelObtainedCallback<RQ, RP> callback() {
        return this.callback;
    }

    @Override
    public IDataModelObtainedExceptionListener<RQ> exceptionListener() {
        return this.exceptionListener;
    }

}
