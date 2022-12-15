package com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.interfaces;

import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.CacheConfig;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.util.CacheLog;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@SuppressWarnings("unchecked")
public abstract class CacheApi implements Cache {
	private static final String TAG = CacheApi.class.getName();

	/**
	 * 默认放入缓存时间
	 */
	private long defaultCacheTime = 0;

	/**
	 * 放入缓存最小过期时间
	 */
	private long minCacheTime = 0;

	/**
	 * 放入缓存最大过期时间
	 */
	private long maxCacheTime = 0;

	/**
	 * 并发读写锁
	 */
	private ReadWriteLock lock = new ReentrantReadWriteLock();

	/**
	 * 缓存类型解码器
	 */
	protected Transcoder transcoder = null;

	public CacheApi(Transcoder transcoder) {
		this.transcoder = transcoder;
	}

	/**
	 * 初始化配置
	 * 
	 */
	@Override
	public void config(CacheConfig cacheConfig) {
		this.minCacheTime = cacheConfig.getMinCacheTime();
		this.maxCacheTime = cacheConfig.getMaxCacheTime();
		this.defaultCacheTime = cacheConfig.getDefaultCacheTime();

		CacheLog.d(TAG, "minCacheTime=" + minCacheTime + " maxCacheTime="
				+ maxCacheTime + " defaultCacheTime="
				+ defaultCacheTime);
	}

	/**
	 * 读取缓存
	 * 
	 * @param key
	 * @return
	 */
	public <K, V> V get(K key) {
		if (key == null)
			return null;
		if (!contains(keyToString(key)))
			return null;
		lock.readLock().lock();
		try {
			Entry e = getEntry(keyToString(key));
			if(e == null) return null;
			return (V) transcoder.decode(e);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * 写入缓存
	 * 
	 * @param key
	 * @param value
	 */
	public <K, V> void put(K key, V value) {
		if (key == null || value == null)
			return;
		lock.writeLock().lock();
		try {
			putEntry(transcoder.decodeKey(key),
						transcoder.encode(value,
							defaultCacheTime));
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * 写入缓存
	 * 
	 * @param key
	 * @param value
	 * @param ttl
	 */
	public <K, V> void put(K key, V value, long ttl) {
		if (key == null || value == null)
			return;
		// 最小缓存时间控制
		if (minCacheTime > 0 && ttl < minCacheTime) {
			ttl = minCacheTime;
		}
		// 最大缓存时间控制
		if (maxCacheTime > 0 && ttl > maxCacheTime) {
			ttl = maxCacheTime;
		}
		lock.writeLock().lock();
		try {
			putEntry(keyToString(key), transcoder.encode(value, ttl));
		} finally {
			lock.writeLock().unlock();
		}
	}

	public <K> String keyToString(K key) {
		return transcoder.decodeKey(key);
	}
}
