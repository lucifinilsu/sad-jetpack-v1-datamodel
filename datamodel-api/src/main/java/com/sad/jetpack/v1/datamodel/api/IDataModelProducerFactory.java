package com.sad.jetpack.v1.datamodel.api;

public interface IDataModelProducerFactory {

    <RQ,RP> IDataModelProducer<RQ,RP> onCreateProducer(String tag);

}
