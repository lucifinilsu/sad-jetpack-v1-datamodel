package com.sad.jetpack.v1.datamodel.api.extension.interceptor;

import android.content.Context;

import com.sad.jetpack.v1.datamodel.api.extension.cache.ACache;
import com.sad.jetpack.v1.datamodel.api.extension.cache.CacheUtil;
import com.sad.jetpack.v1.datamodel.api.extension.cache.CacheUtilConfig;
import com.sad.jetpack.v1.datamodel.api.extension.cache.ICacheEntity;
import com.sad.jetpack.v1.datamodel.api.extension.cache.strategy.Des3EncryptStrategy;
import com.sad.jetpack.v1.datamodel.api.utils.LogcatUtils;

public class DefaultCacheLoader implements ICacheLoader<String>{

    private Context context;

    public DefaultCacheLoader(Context context) {
        this.context = context;
    }

    @Override
    public ICacheEntity<String> readCache(String key) {
        ICacheEntity<String> cacheEntity=CacheUtil.get(key,true);
        return cacheEntity;
    }

    @Override
    public void writeCache(String key,String cacheValue) {
        CacheUtil.put(key,cacheValue,CACHE_MAX_LIFE,true);
        LogcatUtils.e("----->写入缓存：key="+key+",value="+cacheValue);

    }

    public static void initCacheLoader(Context context){
        CacheUtilConfig cc = CacheUtilConfig.builder(context)
                .allowMemoryCache(false)//是否允许保存到内存
                .allowEncrypt(true)//是否允许加密
                .allowKeyEncrypt(true)//是否允许Key加密
                .preventPowerDelete(true)//强力防止删除，将缓存数据存储在app数据库目录下的cachemanage文件夹下
                .setACache(ACache.get(context.getExternalCacheDir()))//自定义ACache，file1为缓存自定义存储文件夹,设置该项，preventPowerDelete失效
                .setAlias("lux")//默认KeyStore加密算法私钥，建议设置.自定义加密算法，该功能失效
                .setIEncryptStrategy(
                        new Des3EncryptStrategy(context, "WLIJkjdsfIlI789sd87dnu==",
                                "lux_1234"))//自定义des3加密
                .build();
        CacheUtil.init(cc);//初始化，必须调用
    }
}
