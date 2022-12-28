package com.sad.jetpack.v1.datamodel.api;

import java.util.LinkedHashMap;

public class DataModelProviders {
    private final static LinkedHashMap<String,IDataModel> modelRepository=new LinkedHashMap<>();
    public static IDataModel get(String dataModelId){
        return modelRepository.get(dataModelId);
    }
    public void register(String dataModelId,IDataModel model){
        if (model!=null){
            modelRepository.put(dataModelId,model);
        }
    }
}
