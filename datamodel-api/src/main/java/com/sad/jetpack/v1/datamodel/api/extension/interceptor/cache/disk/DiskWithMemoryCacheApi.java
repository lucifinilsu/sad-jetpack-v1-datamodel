package com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.disk;

import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.CacheConfig;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.interfaces.Transcoder;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.memory.MemoryCacheApi;

import java.io.File;

public class DiskWithMemoryCacheApi extends DiskBasedCacheApi {

	/**
	 * 内存缓存容器
	 */
	private MemoryCacheApi memoryCache;

	/**
	 * 需要文件缓存的根目录 最多支持存放多少数据
	 * 
	 * @param rootDirectory
	 */
	public DiskWithMemoryCacheApi(File rootDirectory) {
		super(rootDirectory);
		memoryCache = new MemoryCacheApi();
	}

	/**
	 * put/get 解码器
	 * 
	 * @param transcoder
	 */
	public DiskWithMemoryCacheApi(Transcoder transcoder) {
		super(transcoder);
		memoryCache = new MemoryCacheApi(transcoder);
	}

	/**
	 * 缓存配置
	 * 
	 * @param cacheConfig
	 * @return
	 */
	@Override
	public void config(CacheConfig cacheConfig) {
		memoryCache.config(cacheConfig);
		super.config(cacheConfig);
	}

	/**
	 * 初始化缓存
	 */
	@Override
	public void initialize() {
		memoryCache.initialize();
		super.initialize();
	}

	/**
	 * 使缓存失效
	 * 
	 * @param key
	 */
	@Override
	public synchronized <K> void invalidate(K key) {
		memoryCache.invalidate(key);
		super.invalidate(key);
	}

	/**
	 * 把数据放入缓存中
	 * 
	 * @param key
	 * @param entry
	 */
	@Override
	public <K> void putEntry(K key, Entry entry) {
		memoryCache.putEntry(key, entry);
		super.putEntry(key, entry);
	}

	/**
	 * 获取缓存数据
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public <K> Entry getEntry(K key) {
		Entry entry = memoryCache.getEntry(key);
		if (entry == null) {
			entry = super.getEntry(key);
		}
		return entry;
	}

	/**
	 * 是否存在该缓存
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public <K> boolean contains(K key) {
		boolean isCon = memoryCache.contains(key);
		if (isCon) {
			return isCon;
		}
		return super.contains(key);
	}

	/**
	 * 删除缓存数据
	 * 
	 * @param key
	 */
	@Override
	public synchronized <K> void remove(K key) {
		memoryCache.remove(key);
		super.remove(key);
	}

	/**
	 * 清除所有缓存
	 */
	@Override
	public synchronized void clear() {
		memoryCache.clear();
		super.clear();
	}
}
