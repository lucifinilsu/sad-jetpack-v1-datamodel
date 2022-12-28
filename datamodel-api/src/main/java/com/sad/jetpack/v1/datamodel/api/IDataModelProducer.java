package com.sad.jetpack.v1.datamodel.api;


import java.util.List;

public interface IDataModelProducer<RQ,RP> {

    IDataModelProducer<RQ,RP> request(IDataModelRequest<RQ> request);

    IDataModelProducer<RQ,RP> addInputInterceptor(IDataModelInterceptorInput<RQ,RP> input);

    IDataModelProducer<RQ,RP> interceptorInputs(List<IDataModelInterceptorInput<RQ,RP>> interceptorInputs);

    IDataModelProducer<RQ,RP> addOutputInterceptor(IDataModelInterceptorOutput<RQ,RP> output);

    IDataModelProducer<RQ,RP> interceptorOutputs(List<IDataModelInterceptorOutput<RQ,RP>> interceptorOutputs);

    IDataModelProducer<RQ,RP> callback(IDataModelObtainedCallback<RQ,RP> callback);

    IDataModelProducer<RQ,RP> exceptionListener(IDataModelObtainedExceptionListener<RQ> exceptionListener);

    IDataModelProducer<RQ,RP> engine(IDataModelProductEngine<RQ,RP> engine);

    IDataModelProducer<RQ,RP> dataModel(IDataModel dataModel);

    void execute();

}
