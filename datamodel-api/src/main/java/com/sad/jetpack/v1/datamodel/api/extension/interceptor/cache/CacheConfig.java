package com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache;

import java.io.File;

public class CacheConfig {
	/**
	 * 计算单位 KB
	 */
	public static final long BYTES_KB = 1024;

	/**
	 * 计算单位 MB
	 */
	public static final long BYTES_MB = 1024 * 1024;

	/**
	 * Default maximum disk usage in bytes.
	 */
	public static final long DEFAULT_DISK_USAGE_BYTES = 10 * BYTES_MB;

	/**
	 * Default maximum disk memory in bytes.
	 */
	public static final long DEFAULT_MEMORY_USAGE_BYTES = 5 * BYTES_MB;

	/**
	 * High water mark percentage for the cache
	 */
	public static final float HYSTERESIS_FACTOR = 0.9f;

	public static final String CACHE_FILE_PREFIX = "cache_";

	/**
	 * 磁盘最大占用空间
	 */
	private long diskUsageBytes = DEFAULT_DISK_USAGE_BYTES;

	/**
	 * 内存最大占用空间
	 */
	private long memoryUsageBytes = DEFAULT_MEMORY_USAGE_BYTES;

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
	 * 文件系统缓存根目录
	 */
	private File cacheRootDirectory;

	public long getDiskUsageBytes() {
		return diskUsageBytes;
	}

	public void setDiskUsageBytes(long diskUsageBytes) {
		this.diskUsageBytes = diskUsageBytes;
	}

	public long getMemoryUsageBytes() {
		return memoryUsageBytes;
	}

	public void setMemoryUsageBytes(long memoryUsageBytes) {
		this.memoryUsageBytes = memoryUsageBytes;
	}

	public long getDefaultCacheTime() {
		return defaultCacheTime;
	}

	public void setDefaultCacheTime(long defaultCacheTime) {
		this.defaultCacheTime = defaultCacheTime;
	}

	public long getMinCacheTime() {
		return minCacheTime;
	}

	public void setMinCacheTime(long minCacheTime) {
		this.minCacheTime = minCacheTime;
	}

	public long getMaxCacheTime() {
		return maxCacheTime;
	}

	public void setMaxCacheTime(long maxCacheTime) {
		this.maxCacheTime = maxCacheTime;
	}

	public File getCacheRootDirectory() {
		return cacheRootDirectory;
	}

	public void setCacheRootDirectory(File cacheRootDirectory) {
		this.cacheRootDirectory = cacheRootDirectory;
	}

}
