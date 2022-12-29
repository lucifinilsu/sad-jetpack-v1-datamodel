package com.sad.jetpack.v1.datamodel.api;

import java.util.LinkedHashMap;
import java.util.Observable;

public class DefaultDataModel extends Observable implements IDataModel {

    private LinkedHashMap<String, IDataModelResponse> responseRepository = new LinkedHashMap<>();
    private IDataModelProducerFactory producerFactory;

    @Override
    public <RP> IDataModelResponse<RP> get(String tag) {
        return responseRepository.get(tag);
    }

    public void request(String tag){
        if (producerFactory!=null){
            IDataModelProducer  producer=producerFactory.onCreateProducer(tag);
            producer.dataModel(this);
            producer.execute();
        }
    }

    @Override
    public IDataModel setValue(String tag, IDataModelResponse newResponse) {
        responseRepository.put(tag,newResponse);
        setChanged();
        notifyObservers(newResponse);
        return this;
    }

    @Override
    public IDataModel producerFactory(IDataModelProducerFactory producerFactory) {
        this.producerFactory=producerFactory;
        return this;
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
