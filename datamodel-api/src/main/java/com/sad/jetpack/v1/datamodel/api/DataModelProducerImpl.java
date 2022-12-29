package com.sad.jetpack.v1.datamodel.api;


import java.util.ArrayList;
import java.util.List;

public class DataModelProducerImpl<RP> implements IDataModelProducer<RP> {

    private IDataModelRequest request;
    private IDataModelObtainedCallback<RP> callback;
    private IDataModelObtainedExceptionListener exceptionListener;
    private IDataModelProductEngine<RP> engine;
    private List<IDataModelInterceptorInput<RP>> interceptorInputs=new ArrayList<>();
    private List<IDataModelInterceptorOutput<RP>> interceptorOutputs=new ArrayList<>();
    private IDataModel dataModel;

    private DataModelProducerImpl(){

    }

    public static <RP> IDataModelProducer<RP> newInstance(){
        return new DataModelProducerImpl<RP>();
    }

    @Override
    public IDataModelProducer<RP> request(IDataModelRequest request) {
        this.request=request;
        return this;
    }

    @Override
    public IDataModelProducer<RP> addInputInterceptor(IDataModelInterceptorInput input) {
        this.interceptorInputs.add(input);
        return this;
    }

    @Override
    public IDataModelProducer<RP> addOutputInterceptor(IDataModelInterceptorOutput output) {
        interceptorOutputs.add(output);
        return this;
    }

    @Override
    public IDataModelProducer<RP> callback(IDataModelObtainedCallback callback) {
        this.callback=callback;
        return this;
    }

    @Override
    public IDataModelProducer<RP> exceptionListener(IDataModelObtainedExceptionListener exceptionListener) {
        this.exceptionListener=exceptionListener;
        return this;
    }

    @Override
    public IDataModelProducer<RP> engine(IDataModelProductEngine<RP> engine) {
        this.engine=engine;
        return this;
    }

    @Override
    public IDataModelProducer< RP> dataModel(IDataModel dataModel) {
        this.dataModel=dataModel;
        return this;
    }

    @Override
    public void execute() {
        addInputInterceptor(new InternalTerminalDataModelInterceptorInput(interceptorOutputs,engine,dataModel));
        InternalDataModelChainInput<RP> chainInput=new InternalDataModelChainInput<>(interceptorInputs,callback,exceptionListener);
        chainInput.proceed(request,null,chainInput.currIndex());
    }

    @Override
    public IDataModelProducer<RP> interceptorOutputs(List<IDataModelInterceptorOutput<RP>> list) {
        this.interceptorOutputs=list;
        return this;
    }

    @Override
    public IDataModelProducer<RP> interceptorInputs(List<IDataModelInterceptorInput<RP>> list) {
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
