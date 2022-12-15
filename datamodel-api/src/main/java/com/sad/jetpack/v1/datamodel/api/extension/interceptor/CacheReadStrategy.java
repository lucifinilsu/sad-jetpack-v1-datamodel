package com.sad.jetpack.v1.datamodel.api.extension.interceptor;
@Deprecated
public enum CacheReadStrategy {

    ONLY_CACHE(true,false),
    ONLY_ORIGINAL(false,true);

    private boolean readCache=true;
    private boolean readOrgSource=true;
    private boolean sequential=true;

    private CacheReadStrategy(boolean readCache,boolean readOrgSource){

    }


}
