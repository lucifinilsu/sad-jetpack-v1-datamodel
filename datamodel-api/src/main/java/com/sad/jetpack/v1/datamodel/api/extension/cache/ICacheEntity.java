package com.sad.jetpack.v1.datamodel.api.extension.cache;

public interface ICacheEntity<C> {

    long createTime();

    C cacheValue();

}
