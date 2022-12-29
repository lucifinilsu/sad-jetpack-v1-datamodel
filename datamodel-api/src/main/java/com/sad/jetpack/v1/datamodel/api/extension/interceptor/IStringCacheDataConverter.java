package com.sad.jetpack.v1.datamodel.api.extension.interceptor;

import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;
import com.sad.jetpack.v1.datamodel.api.IDataModelResponse;

public interface IStringCacheDataConverter<RP> {

    String serializeResponse(IDataModelResponse<RP> response) throws Exception;

    IDataModelResponse<RP> deserializeString(IDataModelRequest request,String value) throws Exception;

    String createKeyFromRequest(IDataModelRequest request) throws Exception;

}
