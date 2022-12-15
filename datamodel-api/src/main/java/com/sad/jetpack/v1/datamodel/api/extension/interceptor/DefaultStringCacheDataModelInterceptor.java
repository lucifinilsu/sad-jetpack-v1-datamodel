package com.sad.jetpack.v1.datamodel.api.extension.interceptor;

import android.content.Context;
import android.text.TextUtils;

import com.sad.jetpack.v1.datamodel.api.DataModelResponseImpl;
import com.sad.jetpack.v1.datamodel.api.DataSource;
import com.sad.jetpack.v1.datamodel.api.IDataModelChainInput;
import com.sad.jetpack.v1.datamodel.api.IDataModelChainOutput;
import com.sad.jetpack.v1.datamodel.api.IDataModelInterceptorInput;
import com.sad.jetpack.v1.datamodel.api.IDataModelInterceptorOutput;
import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;
import com.sad.jetpack.v1.datamodel.api.IDataModelResponse;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.CacheBuilder;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.interfaces.Cache;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.interfaces.CacheApi;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.transcoder.StringTranscoder;
import com.sad.jetpack.v1.datamodel.api.utils.LogcatUtils;
import com.sad.jetpack.v1.datamodel.api.utils.MapTraverseUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DefaultStringCacheDataModelInterceptor<RQ> implements IDataModelInterceptorInput<RQ,String>, IDataModelInterceptorOutput<RQ,String> {
    private Context context;
    private IStringCacheDataConverter<RQ,String> cacheDataConverter;
    private ICacheDataChainAction<RQ,String> cacheDataChainAction;
    private final static long CACHE_MAX_LIFE=72*60*60*1000;//缓存的有效期
    private final static long CACHE_F=10*1000;//高频访问分界点
    private final static long CACHE_ADV=15*1000;/*1*60*60*1000;*///缓存、数据源顺序显示分界点

    public DefaultStringCacheDataModelInterceptor(Context context) {
        this(context,new DefaultStringCacheDataConverter<RQ>(),null);
    }
    public DefaultStringCacheDataModelInterceptor(Context context, IStringCacheDataConverter<RQ,String> cacheDataConverter,ICacheDataChainAction<RQ,String> cacheDataChainAction) {
        this.context = context;
        this.cacheDataConverter=cacheDataConverter;
        this.cacheDataChainAction=cacheDataChainAction;
    }

    @Override
    public void onInterceptedInput(IDataModelChainInput<RQ, String> chainInput) throws Exception {
        //策略：
        // 内容返回型接口：
        //                     10s内：仅返回缓存。缓存空则访问数据源。
        //                     10s-3600s：直接访问数据源。
        //                     3600s以上：先取缓存，缓存不空先使用缓存。接着访问数据源
         //提交数据型接口：直接请求数据源，缓存不读不写。
        try {
            CacheApi cacheApi = CacheBuilder.newBuilderForDisk()
                    .defaultCacheTime(CACHE_MAX_LIFE)
                    .withTranscoder(new StringTranscoder())
                    .withCacheRootDirectory(context.getExternalCacheDir())
                    .build()
                    ;
            String key=cacheDataConverter.createKeyFromRequest(chainInput.request());
            Cache.Entry cacheEntry=cacheApi.getEntry(key);
            if (cacheEntry==null){
                //无缓存,直接走下一步
                chainInput.proceed();
            }
            else {
                //开始根据策略判断执行
                long futureTime=cacheEntry.getTtl();
                long cx=futureTime-System.currentTimeMillis();
                LogcatUtils.e("----->读取缓存：futureTime="+futureTime+",cx="+cx);
                String cacheValue=cacheApi.get(key);
                if (!TextUtils.isEmpty(cacheValue)){
                    IDataModelResponse<RQ,String> cacheResponse=cacheDataConverter.deserializeString(chainInput.request(), cacheValue);
                    LogcatUtils.e("----->读取缓存：key="+key+",value="+cacheResponse);
                    if (cx<=CACHE_F){
                        LogcatUtils.e("----->读取缓存：10s内");
                        //10s内重复访问，直接返回缓存，无需再访问数据源
                        chainInput.proceed(chainInput.request(), cacheResponse);
                    }
                    else if (cx>CACHE_F && cx <=CACHE_ADV){
                        LogcatUtils.e("----->读取缓存：10s-15s内");
                        //10s-3600s，直接访问数据源
                        chainInput.proceed();
                    }
                    else {
                        LogcatUtils.e("----->读取缓存：15s以上");
                        //3600s-以上，先取缓存，缓存不空先使用缓存。接着访问数据源
                        //这里开放给外部一个接口，可由外部控制合适的实机进行数据源访问，如果外部不定义，或者方法返回false，那么直接再次访问数据源
                        chainInput.proceed(chainInput.request(), cacheResponse);
                        if (cacheDataChainAction!=null){
                            boolean dispatch=cacheDataChainAction.onReProceedDataOrgSourceAction(chainInput);
                            if (!dispatch){
                                chainInput.proceed(chainInput.request(), null);
                            }
                        }
                        else {
                            chainInput.proceed();
                        }
                    }
                }
                else {
                    chainInput.proceed();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            chainInput.proceed();
        }
    }

    @Override
    public void onInterceptedOutput(IDataModelChainOutput<RQ, String> chainOutput) throws Exception {
        try{
            if (chainOutput.response().dataSource()!=DataSource.CACHE){
                CacheApi cacheApi = CacheBuilder.newBuilderForDisk()
                        .defaultCacheTime(CACHE_MAX_LIFE)
                        .withTranscoder(new StringTranscoder())
                        .withCacheRootDirectory(context.getExternalCacheDir())
                        .build()
                        ;
                String key=cacheDataConverter.createKeyFromRequest(chainOutput.response().request());
                String value=cacheDataConverter.serializeResponse(chainOutput.response());
                LogcatUtils.e("----->写入缓存：key="+key+",value="+value);
                cacheApi.put(key,value);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        chainOutput.proceed();
    }




}
