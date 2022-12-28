package com.sad.jetpack.v1.datamodel.api;


import java.util.ArrayList;
import java.util.List;

public class DataModelProducerImpl<RQ,RP> implements IDataModelProducer<RQ,RP> {

    private IDataModelRequest<RQ> request;
    private IDataModelObtainedCallback<RQ,RP> callback;
    private IDataModelObtainedExceptionListener<RQ> exceptionListener;
    private IDataModelProductEngine<RQ,RP> engine;
    private List<IDataModelInterceptorInput<RQ,RP>> interceptorInputs=new ArrayList<>();
    private List<IDataModelInterceptorOutput<RQ,RP>> interceptorOutputs=new ArrayList<>();
    private IDataModel dataModel;

    private DataModelProducerImpl(){

    }

    public static <RQ,RP> IDataModelProducer<RQ,RP> newInstance(){
        return new DataModelProducerImpl<RQ,RP>();
    }

    @Override
    public IDataModelProducer<RQ,RP> request(IDataModelRequest request) {
        this.request=request;
        return this;
    }

    @Override
    public IDataModelProducer<RQ,RP> addInputInterceptor(IDataModelInterceptorInput input) {
        this.interceptorInputs.add(input);
        return this;
    }

    @Override
    public IDataModelProducer<RQ,RP> addOutputInterceptor(IDataModelInterceptorOutput output) {
        interceptorOutputs.add(output);
        return this;
    }

    @Override
    public IDataModelProducer<RQ,RP> callback(IDataModelObtainedCallback callback) {
        this.callback=callback;
        return this;
    }

    @Override
    public IDataModelProducer<RQ,RP> exceptionListener(IDataModelObtainedExceptionListener<RQ> exceptionListener) {
        this.exceptionListener=exceptionListener;
        return this;
    }

    @Override
    public IDataModelProducer<RQ,RP> engine(IDataModelProductEngine<RQ,RP> engine) {
        this.engine=engine;
        return this;
    }

    @Override
    public IDataModelProducer<RQ, RP> dataModel(IDataModel dataModel) {
        this.dataModel=dataModel;
        return this;
    }

    @Override
    public void execute() {
        addInputInterceptor(new InternalTerminalDataModelInterceptorInput(interceptorOutputs,engine,dataModel));
        InternalDataModelChainInput<RQ,RP> chainInput=new InternalDataModelChainInput<>(interceptorInputs,callback,exceptionListener);
        chainInput.proceed(request,null,chainInput.currIndex());
    }

    @Override
    public IDataModelProducer<RQ,RP> interceptorOutputs(List<IDataModelInterceptorOutput<RQ,RP>> list) {
        this.interceptorOutputs=list;
        return this;
    }

    @Override
    public IDataModelProducer<RQ,RP> interceptorInputs(List<IDataModelInterceptorInput<RQ,RP>> list) {
        this.interceptorInputs=list;
        return this;
    }



    /*@Override
    public INetDataProcessorMaster request(INetDataRequest request) {
        this.request=request;
        return this;
    }

    @Override
    public INetDataProcessorMaster callback(INetDataObtainedCallback callback) {
        this.callback=callback;
        return this;
    }

    @Override
    public INetDataProcessorMaster engine(IEngine engine) {
        this.engine=engine;
        return this;
    }

    @Override
    public void execute() {
        if (this.engine==null){
            this.engine=new JsoupNetDataEngine() {
                @Override
                public void onResetJsoupConnection(INetDataRequest request, Connection connection) {

                }
            };
        }
        this.engine.onDoExecute(this);
    }

    @Override
    public INetDataRequest request() {
        return this.request;
    }

    @Override
    public INetDataObtainedCallback callback() {
        return this.callback;
    }

    public IEngine engine() {
        return this.engine;
    }*/
}
