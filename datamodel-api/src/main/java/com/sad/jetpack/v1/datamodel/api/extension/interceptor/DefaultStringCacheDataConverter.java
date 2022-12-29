package com.sad.jetpack.v1.datamodel.api.extension.interceptor;

import android.text.TextUtils;

import com.sad.jetpack.v1.datamodel.api.DataModelResponseImpl;
import com.sad.jetpack.v1.datamodel.api.DataSource;
import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;
import com.sad.jetpack.v1.datamodel.api.IDataModelResponse;
import com.sad.jetpack.v1.datamodel.api.utils.MapTraverseUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DefaultStringCacheDataConverter implements IStringCacheDataConverter<String>{
    @Override
    public String serializeResponse(IDataModelResponse< String> response) throws Exception {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("createTime",System.currentTimeMillis()+"");
        jsonObject.put("body",response.body());
        jsonObject.put("code",response.code());
        JSONObject json_header=new JSONObject();
        MapTraverseUtils.traverseGroup(response.headers(), new MapTraverseUtils.ITraverseAction<String, String>() {
            @Override
            public void onTraversed(String s, String s2) throws Exception{
                json_header.put(s,s2);
            }
        });
        jsonObject.put("headers",json_header);
        jsonObject.put("dataSource",response.dataSource().type());
        return jsonObject.toString();
    }

    @Override
    public IDataModelResponse< String> deserializeString(IDataModelRequest request, String value) throws Exception {
        if (TextUtils.isEmpty(value)){
            return null;
        }
        JSONObject jsonObject=new JSONObject(value);
        IDataModelResponse.Creator<String> responseCreator= DataModelResponseImpl.<String>newCreator()
                .body(jsonObject.optString("body"))
                .dataSource(DataSource.CACHE/*EnumUtils.valueOf(DataSource.class,jsonObject.optInt("dataSource"))*/)
                .request(request)
                .code(jsonObject.optInt("code"))
                ;
        Map<String,String> h=new HashMap<>();
        Iterator<String> iterator=jsonObject.optJSONObject("headers").keys();
        while (iterator.hasNext()){
            String k=iterator.next();
            h.put(k,jsonObject.optJSONObject("headers").optString(k));
        }
        responseCreator.headers(h);
        return responseCreator.create();
    }

    @Override
    public String createKeyFromRequest(IDataModelRequest request) throws Exception{
        return request.tag();
    }
}
