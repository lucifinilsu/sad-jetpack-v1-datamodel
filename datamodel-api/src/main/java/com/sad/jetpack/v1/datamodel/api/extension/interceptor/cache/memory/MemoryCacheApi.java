package com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.memory;

import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.CacheConfig;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.interfaces.CacheApi;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.interfaces.Transcoder;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.util.CacheLog;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 内存缓存
 */
public class MemoryCacheApi extends CacheApi {
	private static final String TAG = MemoryCacheApi.class.getName();
	
	/**
	 * K,V 缓存 使用LRU特性 accessOrder true
	 */
	private final Map<String, Entry> entries = new LinkedHashMap<String, Entry>(16, .75f, true);

	/**
	 * 缓存占用总大小大小
	 */
	private long totalSize = 0;

	/**
	 * 允许最大的缓存大小
	 */
	private long maxCacheSizeInBytes = CacheConfig.DEFAULT_MEMORY_USAGE_BYTES;

	/**
	 * 使用默认配置缓存容量
	 *
	 */
	public MemoryCacheApi() {
		this(CacheConfig.DEFAULT_MEMORY_USAGE_BYTES);
	}
	
	/**
	 * 配置缓存容量
	 * 
	 * @param maxCacheSizeInBytes
	 */
	public MemoryCacheApi(long maxCacheSizeInBytes) {
		this(null, maxCacheSizeInBytes);
	}

	/**
	 * 使用默认配置缓存容量
	 *
	 */
	public MemoryCacheApi(Transcoder transcoder) {
		this(transcoder, CacheConfig.DEFAULT_MEMORY_USAGE_BYTES);
	}
	
	/**
	 * 使用设定配置缓存容量
	 * 
	 * @param maxCacheSizeInBytes
	 */
	public MemoryCacheApi(Transcoder transcoder, long maxCacheSizeInBytes) {
		super(transcoder);
		this.maxCacheSizeInBytes = maxCacheSizeInBytes;
	}
	
	/**
	 * 缓存配置
	 * 
	 * @param cacheConfig
	 * @return
	 */
	@Override
	public void config(CacheConfig cacheConfig) {
		super.config(cacheConfig);
		if(cacheConfig.getMemoryUsageBytes() > 0) {
			this.maxCacheSizeInBytes = cacheConfig.getMemoryUsageBytes();
		}
	}

	/**
	 * 初始化缓存
	 */
	@Override
	public void initialize() {

	}

	/**
	 * 使缓存失效
	 * 
	 * @param key
	 */
	@Override
	public <K> void invalidate(K key) {
		Entry entry = getEntry(keyToString(key));
		entry.setTtl(0,true);
	}

	/**
	 * 把数据放入缓存中
	 * 
	 * @param key
	 * @param entry
	 */
	@Override
	public <K> void putEntry(K key, Entry entry) {
		pruneIfNeeded(entry.size());

		if (!entries.containsKey(keyToString(key))) {
			totalSize += entry.size();
		} else {
			Entry oldEntry = entries.get(keyToString(key));
			totalSize -= oldEntry.size();
		}
		entries.put(keyToString(key), entry);
	}
	
	/**
	 * 获取缓存数据
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public <K> Entry getEntry(K key) {
		Entry entry = entries.get(keyToString(key));

		if (entry != null && entry.isExpired()) {
			remove(key);
			return null;
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
		Entry entry = entries.get(keyToString(key));
		if (entry != null && !entry.isExpired()) {
			return true;
		}
		return false;
	}

	/**
	 * 删除缓存数据
	 * 
	 * @param key
	 */
	@Override
	public <K> void remove(K key) {
		Entry entry = entries.get(keyToString(key));
		if (entry != null) {
			totalSize -= entry.size();
			entries.remove(keyToString(key));
		}
	}

	/**
	 * 清除所有缓存
	 */
	@Override
	public synchronized void clear() {
		entries.clear();
		totalSize = 0;
	}

	/**
	 * 是否够用空间，如果不够用，清除不常用的
	 * 
	 * @param neededSpace
	 */
	private void pruneIfNeeded(int neededSpace) {
		if ((totalSize + neededSpace) < maxCacheSizeInBytes) {
			return;
		}

		long before = totalSize;
		int prunedFiles = 0;
		long startTime = System.currentTimeMillis();

		// 使用LRU特性，删除最前面，最不常用的
		Iterator<Map.Entry<String, Entry>> iterator = entries.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Entry> entry = iterator.next();
			Entry e = entry.getValue();
			totalSize -= e.size();
			iterator.remove();
			prunedFiles++;

			if ((totalSize + neededSpace) < maxCacheSizeInBytes * CacheConfig.HYSTERESIS_FACTOR) {
				break;
			}
		}
		CacheLog.d(TAG, String.format("pruned %d files, %d bytes, %d ms",
				prunedFiles, (totalSize - before),
				System.currentTimeMillis() - startTime));

	}
}
