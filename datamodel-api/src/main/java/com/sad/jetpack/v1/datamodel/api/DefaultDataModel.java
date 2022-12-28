package com.sad.jetpack.v1.datamodel.api;

import java.util.LinkedHashMap;
import java.util.Observable;

public class DefaultDataModel extends Observable implements IDataModel {

    private LinkedHashMap<String, IDataModelResponse> responseRepository = new LinkedHashMap<>();
    private IDataModelProducerFactory producerFactory;

    @Override
    public <RQ, RP> IDataModelResponse<RQ, RP> get(String tag) {
        return responseRepository.get(tag);
    }

    public <RQ, RP> IDataModelResponse<RQ, RP> getAndRequest(String tag){
        IDataModelResponse<RQ, RP> response=get(tag);
        if (response==null){
            if (producerFactory!=null){
                IDataModelProducer<RQ,RP> producer=producerFactory.onCreateProducer(tag);
                producer.dataModel(this);
                producer.execute();
            }
        }
        return response;
    }

    @Override
    public void setValue(String tag, IDataModelResponse newResponse) {
        responseRepository.put(tag,newResponse);
        setChanged();
        notifyObservers(newResponse);
    }

    @Override
    public void producerFactory(String tag, IDataModelProducerFactory producerFactory) {
        this.producerFactory=producerFactory;
    }


    @Override
    public void setChanged() {
        super.setChanged();
    }

    @Override
    public void clearChanged() {
        super.clearChanged();
    }
}
