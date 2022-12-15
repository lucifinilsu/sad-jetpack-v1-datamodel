package com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache;

import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.disk.DiskBasedCacheApi;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.disk.DiskWithMemoryCacheApi;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.interfaces.CacheApi;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.interfaces.Transcoder;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.memory.MemoryCacheApi;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.transcoder.StringTranscoder;

import java.io.File;

public class CacheBuilder {
	public enum Type {
		DISK, MEMORY, DISK_WITH_MEMORY
	}

	public static Builder newBuilder() {
		return new Builder(Type.DISK_WITH_MEMORY);
	}

	public static Builder newBuilderForMemory() {
		return new Builder(Type.MEMORY);
	}

	public static Builder newBuilderForDisk() {
		return new Builder(Type.DISK);
	}

	public static class Builder {
		private Type type = Type.DISK_WITH_MEMORY;
		private CacheConfig cacheConfig = new CacheConfig();
		private CacheApi gcache = null;

		public Builder(Type type) {
			this.type = type;
		}

		public Builder withTranscoder(Transcoder transcoder) {
			switch (type) {
			case DISK_WITH_MEMORY:
				this.gcache = new DiskWithMemoryCacheApi(transcoder);
				break;
			case MEMORY:
				this.gcache = new MemoryCacheApi(transcoder);
				break;
			case DISK:
				this.gcache = new DiskBasedCacheApi(transcoder);
				break;
			}
			return this;
		}

		public Builder maxMemoryUsageBytes(long memoryUsageBytes) {
			cacheConfig.setMemoryUsageBytes(memoryUsageBytes);
			return this;
		}

		public Builder maxDiskUsageBytes(long diskUsageBytes) {
			cacheConfig.setDiskUsageBytes(diskUsageBytes);
			return this;
		}

		public Builder minCacheTime(long minCacheTime) {
			cacheConfig.setMinCacheTime(minCacheTime);
			return this;
		}

		public Builder maxCacheTime(long maxCacheTime) {
			cacheConfig.setMaxCacheTime(maxCacheTime);
			return this;
		}

		public Builder defaultCacheTime(long defaultCacheTime) {
			cacheConfig.setDefaultCacheTime(defaultCacheTime);
			return this;
		}

		public Builder withCacheRootDirectory(File cacheRootDirectory) {
			cacheConfig.setCacheRootDirectory(cacheRootDirectory);
			return this;
		}

		public CacheApi build() {
			this.gcache.config(cacheConfig);
			this.gcache.initialize();
			return this.gcache;
		}
	}

	public static void main(String[] args) {
		CacheApi gc = CacheBuilder.newBuilder()
				.withTranscoder(new StringTranscoder())
				.maxMemoryUsageBytes(20000)
				.maxDiskUsageBytes(40000).maxCacheTime(3000)
				.minCacheTime(1000).defaultCacheTime(1000)
				.build();
		CacheApi gc2 = CacheBuilder.newBuilderForMemory()
				.withTranscoder(new StringTranscoder())
				.maxMemoryUsageBytes(20000).maxCacheTime(3000)
				.minCacheTime(1000).defaultCacheTime(1000)
				.build();
		CacheApi gc3 = CacheBuilder.newBuilderForDisk()
				.withTranscoder(new StringTranscoder())
				.maxDiskUsageBytes(40000).maxCacheTime(3000)
				.minCacheTime(1000).defaultCacheTime(1000)
				.build();
	}
}
