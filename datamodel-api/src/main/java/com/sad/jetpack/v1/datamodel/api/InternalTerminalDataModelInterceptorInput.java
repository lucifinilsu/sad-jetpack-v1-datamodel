package com.sad.jetpack.v1.datamodel.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InternalTerminalDataModelInterceptorInput<RP> implements IDataModelInterceptorInput<RP> {

    private List<IDataModelInterceptorOutput<RP>> interceptorOutputs=new ArrayList<>();
    private IDataModelProductEngine<RP> engine;
    private IDataModel dataModel;

    public InternalTerminalDataModelInterceptorInput(List<IDataModelInterceptorOutput< RP>> interceptorOutputs,IDataModelProductEngine< RP> engine,IDataModel dataModel) {
        this.interceptorOutputs = interceptorOutputs;
        this.engine = engine;
        this.dataModel=dataModel;
    }

    @Override
    public void onInterceptedInput(IDataModelChainInput<RP> chainInput) throws Exception {
        IDataModelRequest request=chainInput.request();
        IDataModelInterceptorOutput<RP>[] units_reSort=new IDataModelInterceptorOutput[interceptorOutputs.size()];
        //重新倒序
        for (IDataModelInterceptorOutput<RP> interceptorOutput:interceptorOutputs
        ) {
            units_reSort[interceptorOutputs.size()-1-interceptorOutputs.indexOf(interceptorOutput)]=interceptorOutput;
        }
        List<IDataModelInterceptorOutput<RP>> units_reSort_list=new ArrayList<>(Arrays.asList(units_reSort));
        IDataModelChainOutput<RP> chainOutput=new InternalDataModelChainOutput<RP>(units_reSort_list,chainInput.callback(),chainInput.exceptionListener(),dataModel);
        IDataModelResponse<RP> cacheResponse=chainInput.cacheResponse();
        if (cacheResponse!=null){
            chainOutput.proceed(cacheResponse);
        }
        else {
            if (engine!=null){
                engine.onEngineExecute(request,chainOutput);
            }
            else {
                throw new Exception("no data engine!!!!!!!");
            }
        }

    }
}
