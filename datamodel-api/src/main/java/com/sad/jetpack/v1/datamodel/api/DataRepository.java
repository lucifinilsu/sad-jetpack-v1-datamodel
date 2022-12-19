package com.sad.jetpack.v1.datamodel.api;


import java.util.LinkedHashMap;
import java.util.Observable;
import java.util.Observer;

public class DataRepository extends Observable{

    private LinkedHashMap<String, IDataModelResponse>  responseRepository= new LinkedHashMap<>();

    public static void main(String[] args) {
        DataRepository repository=new DataRepository();
        repository.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
            }
        });
    }

    public void setValue(String tag,IDataModelResponse newResponse){
        IDataModelResponse oldResponse=responseRepository.get(tag);
        if (oldResponse!=null && newResponse!=null){
            responseRepository.put(tag,newResponse);
            setChanged();
        }
    }

    private boolean checkNeedUpdate(IDataModelResponse oldResponse,IDataModelResponse newResponse){
        if (oldResponse.dataSource()!=newResponse.dataSource()){
            return true;
        }
        if (oldResponse.code()!=newResponse.code()){
            return true;
        }
        if (!oldResponse.body().equals(newResponse.body())){
            return true;
        }
        IDataModelRequest oldRequest=oldResponse.request();
        IDataModelRequest newRequest= newResponse.request();
        if (oldRequest!=null && newRequest!=null){
            oldRequest.tag();
        }
        return false;
    }
}
