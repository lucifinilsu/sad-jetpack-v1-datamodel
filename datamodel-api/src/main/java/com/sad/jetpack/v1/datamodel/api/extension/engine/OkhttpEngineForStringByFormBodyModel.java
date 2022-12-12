package com.sad.jetpack.v1.datamodel.api.extension.engine;

import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Iterator;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.Request;

public class OkhttpEngineForStringByFormBodyModel extends OkhttpEngineForStringModel<Map<String,String>> {
    @Override
    public void onRestOkhttpRequest(IDataModelRequest<Map<String,String>> request, Request.Builder okhttpRequestBuilder) {
        if (request.method()== IDataModelRequest.Method.POST){
            Map<String,String> orgRequestBody=request.body();
            MultipartBody.Builder builder=new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            traverseGroup(orgRequestBody, new ITraverseAction<String, String>() {
                @Override
                public void onTraversed(String s, String s2) {
                    builder.addFormDataPart(s,s2);
                }
            });
            MultipartBody formBody = builder.build();
            okhttpRequestBuilder=okhttpRequestBuilder.post(formBody);
        }
    }
    protected interface ITraverseAction<K,V>{
        void onTraversed(K k, V v);
    }
    protected <K,V> void traverseGroup(Map<K,V> map, ITraverseAction<K,V>... actions){
        if (!ObjectUtils.isEmpty(map)){
            Iterator<Map.Entry<K, V>> iterator=map.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<K,V> entityEntry=iterator.next();
                K k=entityEntry.getKey();
                V v=entityEntry.getValue();
                if (v!=null){
                    if (ObjectUtils.isNotEmpty(actions)){
                        for (ITraverseAction<K,V> action:actions
                        ) {
                            action.onTraversed(k,v);
                        }
                    }
                }
            }
        }
    }

}
