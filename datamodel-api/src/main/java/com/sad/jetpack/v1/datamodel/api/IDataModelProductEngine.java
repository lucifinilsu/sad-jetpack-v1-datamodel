package com.sad.jetpack.v1.datamodel.api;

public interface IDataModelProductEngine<RP> {

    void onEngineExecute(IDataModelRequest request, IDataModelChainOutput<RP> chainOutput) throws Exception;

}
