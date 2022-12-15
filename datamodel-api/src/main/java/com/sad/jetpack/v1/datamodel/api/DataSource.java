package com.sad.jetpack.v1.datamodel.api;

public enum DataSource {

    NET(0),CACHE(1),LOCAL(2),OTHER(3);

    private int type=0;
    public int type(){
        return type;
    }
    DataSource(int type){
        this.type=type;
    }

}
