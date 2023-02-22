package com.sad.jetpack.v1.datamodel.api;


public interface IDataModelProducerFactory {

    IDataModelProducer dataModelProducer(String tagAndClientKey);

}
