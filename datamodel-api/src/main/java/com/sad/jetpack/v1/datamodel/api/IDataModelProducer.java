package com.sad.jetpack.v1.datamodel.api;


import java.util.List;

public interface IDataModelProducer<RP> {

    IDataModelProducer<RP> request(IDataModelRequest request);

    IDataModelProducer<RP> addInputInterceptor(IDataModelInterceptorInput<RP> input);

    IDataModelProducer<RP> interceptorInputs(List<IDataModelInterceptorInput<RP>> interceptorInputs);

    IDataModelProducer<RP> addOutputInterceptor(IDataModelInterceptorOutput<RP> output);

    IDataModelProducer<RP> interceptorOutputs(List<IDataModelInterceptorOutput<RP>> interceptorOutputs);

    IDataModelProducer<RP> callback(IDataModelObtainedCallback<RP> callback);

    IDataModelProducer<RP> exceptionListener(IDataModelObtainedExceptionListener exceptionListener);

    IDataModelProducer<RP> engine(IDataModelProductEngine<RP> engine);

    IDataModelProducer<RP> dataModel(IDataModel dataModel);

    void execute();

}
