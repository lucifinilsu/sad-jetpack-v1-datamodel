package com.sad.jetpack.v1.datamodel.api;

public interface IDataModelProductEngine<RQ,RP> {

    void onEngineExecute(IDataModelRequest<RQ> request, IDataModelChainOutput<RQ,RP> chainOutput) throws Exception;

}
