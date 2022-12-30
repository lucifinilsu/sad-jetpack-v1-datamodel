package com.sad.jetpack.v1.datamodel.api;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class InternalDataModelChainInput<RP> implements IDataModelChainInput<RP> {
    private IDataModelRequest request;
    private List<IDataModelInterceptorInput<RP>> interceptorInputs=new ArrayList<>();
    private int currIndex=-1;
    private IDataModelObtainedCallback<RP> callback;
    private IDataModelObtainedExceptionListener exceptionListener;
    private IDataModelResponse<RP> cacheResponse;

    public InternalDataModelChainInput(
            List<IDataModelInterceptorInput<RP>> interceptorInputs,
            IDataModelObtainedCallback<RP> callback,
            IDataModelObtainedExceptionListener exceptionListener
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
    public IDataModelRequest request() {
        return this.request;
    }

    @Override
    public IDataModelResponse< RP> cacheResponse() {
        return this.cacheResponse;
    }


    @Override
    public void proceed(@NonNull IDataModelRequest request, IDataModelResponse< RP> cacheResponse, int index) {
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
            IDataModelInterceptorInput<RP> interceptorInput=interceptorInputs.get(currIndex);
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
    public IDataModelObtainedCallback<RP> callback() {
        return this.callback;
    }

    @Override
    public IDataModelObtainedExceptionListener exceptionListener() {
        return this.exceptionListener;
    }

}
