package com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.interfaces;

import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.CacheConfig;

/**
 * 缓存接口
 */
public interface Cache  {

	/**
	 * 缓存配置
	 * 
	 * @param cacheConfig
	 * @return
	 */
	public void config(CacheConfig cacheConfig);

	/**
	 * 初始化缓存
	 */
	public void initialize();

	/**
	 * 使缓存失效
	 * 
	 * @param key
	 */
	public <K> void invalidate(K key);

	/**
	 * 把数据放入缓存中
	 * 
	 * @param key
	 * @param entry
	 */
	public <K> void putEntry(K key, Entry entry);

	/**
	 * 获取缓存数据
	 * 
	 * @param key
	 * @return
	 */
	public <K> Entry getEntry(K key);

	/**
	 * 是否存在该缓存
	 * 
	 * @param key
	 * @return
	 */
	public <K> boolean contains(K key);

	/**
	 * 删除缓存数据
	 * 
	 * @param key
	 */
	public <K> void remove(K key);

	/**
	 * 清除所有缓存
	 */
	public void clear();

	/**
	 * Cache数据信息
	 */
	public static class Entry {
		/**
		 * 缓存数据
		 */
		private byte[] data;

		/**
		 * 生存周期 毫秒
		 */
		private long ttl;

		public Entry() {
		}

		/**
		 * 缓存数据、生存周期
		 * 
		 * @param data
		 * @param ttl
		 */
		public Entry(byte[] data, long ttl) {
			this.data = data;
			setTtl(ttl,true);
		}

		/**
		 * 获取缓存数据
		 * 
		 * @return
		 */
		public byte[] getData() {
			return data;
		}

		/**
		 * 设置缓存数据
		 * 
		 * @param data
		 */
		public void setData(byte[] data) {
			this.data = data;
		}

		/**
		 * 获取生存时间
		 * 
		 * @return
		 */
		public long getTtl() {
			return ttl;
		}

		/**
		 * 累加当前当前时间
		 * 
		 * @param ttl
		 */
		public void setTtl(long ttl,boolean addCurr) {
			this.ttl = ttl + (addCurr?System.currentTimeMillis():0);
		}

		/**
		 * 缓存是否已经过期
		 * 
		 * @return
		 */
		public boolean isExpired() {
			return this.ttl < System.currentTimeMillis();
		}

		/**
		 * 缓存数据大小
		 * 
		 * @return
		 */
		public int size() {
			return data.length;
		}

	}

}
