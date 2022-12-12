package com.sad.jetpack.v1.datamodel.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InternalTerminalDataModelInterceptorInput<RQ,RP> implements IDataModelInterceptorInput<RQ,RP> {

    private List<IDataModelInterceptorOutput<RQ,RP>> interceptorOutputs=new ArrayList<>();
    private IDataModelObtainedCallback<RQ,RP> callback;
    private IDataModelObtainedExceptionListener<RQ> exceptionListener;
    private IDataModelProductEngine<RQ,RP> engine;

    public InternalTerminalDataModelInterceptorInput(List<IDataModelInterceptorOutput<RQ, RP>> interceptorOutputs, IDataModelObtainedCallback<RQ, RP> callback, IDataModelProductEngine<RQ, RP> engine) {
        this.interceptorOutputs = interceptorOutputs;
        this.callback = callback;
        this.engine = engine;
    }

    @Override
    public void onInterceptedInput(IDataModelChainInput<RQ,RP> chainInput) throws Exception {
        IDataModelRequest<RQ> request=chainInput.request();
        this.exceptionListener=chainInput.exceptionListener();
        IDataModelInterceptorOutput<RQ,RP>[] units_reSort=new IDataModelInterceptorOutput[interceptorOutputs.size()];
        //重新倒序
        for (IDataModelInterceptorOutput<RQ,RP> interceptorOutput:interceptorOutputs
        ) {
            units_reSort[interceptorOutputs.size()-1-interceptorOutputs.indexOf(interceptorOutput)]=interceptorOutput;
        }
        List<IDataModelInterceptorOutput<RQ,RP>> units_reSort_list=new ArrayList<>(Arrays.asList(units_reSort));
        IDataModelChainOutput<RQ,RP> chainOutput=new InternalDataModelChainOutput<RQ,RP>(units_reSort_list,callback,exceptionListener);
        if (engine!=null){
            engine.onEngineExecute(request,chainOutput);
        }
        else {
            throw new Exception("no data engine!!!!!!!");
        }
    }
}