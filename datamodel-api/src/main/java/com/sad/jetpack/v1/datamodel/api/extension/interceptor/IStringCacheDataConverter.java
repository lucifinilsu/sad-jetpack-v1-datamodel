package com.sad.jetpack.v1.datamodel.api.extension.interceptor;

import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;
import com.sad.jetpack.v1.datamodel.api.IDataModelResponse;

public interface IStringCacheDataConverter<RQ,RP> {

    String serializeResponse(IDataModelResponse<RQ,RP> response) throws Exception;

    IDataModelResponse<RQ,RP> deserializeString(IDataModelRequest<RQ> request,String value) throws Exception;

    String createKeyFromRequest(IDataModelRequest<RQ> request) throws Exception;

}
