package com.sad.jetpack.v1.datamodel.api;


import java.util.List;

public interface IDataModelProcessorMaster<RQ,RP> {

    IDataModelProcessorMaster<RQ,RP> request(IDataModelRequest<RQ> request);

    IDataModelProcessorMaster<RQ,RP> addInputInterceptor(IDataModelInterceptorInput<RQ,RP> input);

    IDataModelProcessorMaster<RQ,RP> interceptorInputs(List<IDataModelInterceptorInput<RQ,RP>> interceptorInputs);

    IDataModelProcessorMaster<RQ,RP> addOutputInterceptor(IDataModelInterceptorOutput<RQ,RP> output);

    IDataModelProcessorMaster<RQ,RP> interceptorOutputs(List<IDataModelInterceptorOutput<RQ,RP>> interceptorOutputs);

    IDataModelProcessorMaster<RQ,RP> callback(IDataModelObtainedCallback<RQ,RP> callback);

    IDataModelProcessorMaster<RQ,RP> exceptionListener(IDataModelObtainedExceptionListener<RQ> exceptionListener);

    IDataModelProcessorMaster<RQ,RP> engine(IDataModelProductEngine<RQ,RP> engine);

    void execute();

}
