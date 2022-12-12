package com.sad.jetpack.v1.datamodel.api;


import java.util.ArrayList;
import java.util.List;

public class DataModelProcessorMasterImpl<RQ,RP> implements IDataModelProcessorMaster<RQ,RP> {

    private IDataModelRequest<RQ> request;
    private IDataModelObtainedCallback<RQ,RP> callback;
    private IDataModelObtainedExceptionListener<RQ> exceptionListener;
    private IDataModelProductEngine<RQ,RP> engine;
    private List<IDataModelInterceptorInput<RQ,RP>> interceptorInputs=new ArrayList<>();
    private List<IDataModelInterceptorOutput<RQ,RP>> interceptorOutputs=new ArrayList<>();

    private DataModelProcessorMasterImpl(){

    }

    public static <RQ,RP> IDataModelProcessorMaster<RQ,RP> newInstance(){
        return new DataModelProcessorMasterImpl<RQ,RP>();
    }

    @Override
    public IDataModelProcessorMaster<RQ,RP> request(IDataModelRequest request) {
        this.request=request;
        return this;
    }

    @Override
    public IDataModelProcessorMaster<RQ,RP> addInputInterceptor(IDataModelInterceptorInput input) {
        this.interceptorInputs.add(input);
        return this;
    }

    @Override
    public IDataModelProcessorMaster<RQ,RP> addOutputInterceptor(IDataModelInterceptorOutput output) {
        interceptorOutputs.add(output);
        return this;
    }

    @Override
    public IDataModelProcessorMaster<RQ,RP> callback(IDataModelObtainedCallback callback) {
        this.callback=callback;
        return this;
    }

    @Override
    public IDataModelProcessorMaster<RQ,RP> exceptionListener(IDataModelObtainedExceptionListener<RQ> exceptionListener) {
        this.exceptionListener=exceptionListener;
        return this;
    }

    @Override
    public IDataModelProcessorMaster<RQ,RP> engine(IDataModelProductEngine<RQ,RP> engine) {
        this.engine=engine;
        return this;
    }

    @Override
    public void execute() {
        addInputInterceptor(new InternalTerminalDataModelInterceptorInput(interceptorOutputs,callback,engine));
        InternalDataModelChainInput<RQ,RP> chainInput=new InternalDataModelChainInput<>(interceptorInputs,callback,exceptionListener);
        chainInput.proceed(request);
    }

    @Override
    public IDataModelProcessorMaster<RQ,RP> interceptorOutputs(List<IDataModelInterceptorOutput<RQ,RP>> list) {
        this.interceptorOutputs=list;
        return this;
    }

    @Override
    public IDataModelProcessorMaster<RQ,RP> interceptorInputs(List<IDataModelInterceptorInput<RQ,RP>> list) {
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
