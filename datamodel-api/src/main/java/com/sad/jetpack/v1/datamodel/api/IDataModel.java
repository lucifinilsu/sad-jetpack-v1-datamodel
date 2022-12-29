package com.sad.jetpack.v1.datamodel.api;

import java.util.Observer;

public interface IDataModel {

    <RP> IDataModelResponse<RP> get(String tag);

    void request(String tag);

    IDataModel setValue(String tag,IDataModelResponse newResponse);

    IDataModel producerFactory(IDataModelProducerFactory producerFactory);

    void addObserver(Observer o);
    void deleteObserver(Observer o) ;
    void notifyObservers() ;
    void notifyObservers(Object arg);
    void deleteObservers() ;
    void setChanged() ;
    void clearChanged();
    boolean hasChanged();
    int countObservers();
}
