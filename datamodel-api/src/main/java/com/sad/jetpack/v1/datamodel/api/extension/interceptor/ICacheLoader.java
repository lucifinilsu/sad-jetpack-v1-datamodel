package com.sad.jetpack.v1.datamodel.api.extension.interceptor;

import com.sad.jetpack.v1.datamodel.api.extension.cache.ICacheEntity;

public interface ICacheLoader<C> {
    final static int CACHE_MAX_LIFE=72*60*60;//缓存的有效期(单位s)
    final static int CACHE_F=10;//高频访问分界点(单位s)
    final static int CACHE_ADV=15;/*1*60*60*1000;*///缓存、数据源顺序显示分界点(单位s)

    ICacheEntity<C> readCache(String key);

    void writeCache(String key,C cacheValue);

}
