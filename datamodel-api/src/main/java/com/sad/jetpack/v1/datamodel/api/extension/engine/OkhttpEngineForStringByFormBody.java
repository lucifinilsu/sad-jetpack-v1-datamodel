package com.sad.jetpack.v1.datamodel.api.extension.engine;

import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;
import com.sad.jetpack.v1.datamodel.api.utils.MapTraverseUtils;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Iterator;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.Request;

public class OkhttpEngineForStringByFormBody extends OkhttpEngineForString<Map<String,String>> {
    @Override
    public void onRestOkhttpRequest(IDataModelRequest<Map<String,String>> request, Request.Builder okhttpRequestBuilder) {
        if (request.method()== IDataModelRequest.Method.POST){
            Map<String,String> orgRequestBody=request.body();
            MultipartBody.Builder builder=new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            MapTraverseUtils.traverseGroup(orgRequestBody, new MapTraverseUtils.ITraverseAction<String, String>() {
                @Override
                public void onTraversed(String s, String s2) {
                    builder.addFormDataPart(s,s2);
                }
            });
            MultipartBody formBody = builder.build();
            okhttpRequestBuilder=okhttpRequestBuilder.post(formBody);
        }
    }


}
