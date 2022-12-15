package com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.disk;

import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.interfaces.Cache.Entry;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.util.CacheLog;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 缓存头部信息
 *
 */
public class CacheHeader {
	private static final String TAG = CacheHeader.class.getName();
	/**
	 * Magic number for current version of cache file format.
	 */
	public static final int CACHE_MAGIC = 0xCAC00001;

	/**
	 * 生存周期 毫秒
	 */
	public long ttl;

	/**
	 * 缓存数据文件长度
	 */
	public long size;

	/**
	 * 缓存KEY
	 */
	public String key;

	private CacheHeader() {
	}

	/**
	 * 包装数据
	 * 
	 * @param entry
	 */
	public CacheHeader(String key, Entry entry) {
		this.ttl = entry.getTtl();
		this.size = entry.size();
		this.key = key;
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
	 * Reads the header off of an InputStream and returns a CacheHeader
	 * object.
	 * 
	 * @param is
	 *                The InputStream to read from.
	 * @throws IOException
	 */
	public static CacheHeader readHeader(InputStream is) throws IOException {
		CacheHeader entry = new CacheHeader();
		int magic = StreamUtils.readInt(is);
		if (magic != CACHE_MAGIC) {
			// don't bother deleting, it'll get pruned eventually
			throw new IOException();
		}
		entry.key = StreamUtils.readString(is);
		entry.size = StreamUtils.readLong(is);
		entry.ttl = StreamUtils.readLong(is);
		return entry;
	}

	/**
	 * Creates a cache entry for the specified data.
	 */
	public Entry toCacheEntry(byte[] data) {
		Entry e = new Entry();
		e.setData(data);
		e.setTtl(ttl,false);
		return e;
	}

	/**
	 * Writes the contents of this CacheHeader to the specified
	 * OutputStream.
	 */
	public boolean writeHeader(OutputStream os) {
		try {
			StreamUtils.writeInt(os, CACHE_MAGIC);
			StreamUtils.writeString(os, key);
			StreamUtils.writeLong(os, size);
			StreamUtils.writeLong(os, ttl);
			os.flush();
			return true;
		} catch (IOException e) {
			CacheLog.w(TAG, "write Header error!", e);
			return false;
		}
	}

}
