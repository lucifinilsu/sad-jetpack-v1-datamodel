package com.sad.jetpack.v1.datamodel.api;

import java.util.Observer;

public interface IDataModel {

    <RQ,RP> IDataModelResponse<RQ,RP> get(String tag);

    <RQ, RP> IDataModelResponse<RQ, RP> getAndRequest(String tag);

    void setValue(String tag,IDataModelResponse newResponse);

    void producerFactory(String tag,IDataModelProducerFactory producerFactory);

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
