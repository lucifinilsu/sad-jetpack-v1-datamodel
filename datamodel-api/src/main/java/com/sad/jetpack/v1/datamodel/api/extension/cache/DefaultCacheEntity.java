package com.sad.jetpack.v1.datamodel.api.extension.cache;

public class DefaultCacheEntity<C> implements ICacheEntity<C>{

    private long futureTime=0;
    private C cacheValue=null;

    public long getFutureTime() {
        return futureTime;
    }

    public void setFutureTime(long futureTime) {
        this.futureTime = futureTime;
    }

    public C getCacheValue() {
        return cacheValue;
    }

    public void setCacheValue(C cacheValue) {
        this.cacheValue = cacheValue;
    }

    @Override
    public long createTime() {
        return futureTime;
    }

    @Override
    public C cacheValue() {
        return this.cacheValue;
    }
}
