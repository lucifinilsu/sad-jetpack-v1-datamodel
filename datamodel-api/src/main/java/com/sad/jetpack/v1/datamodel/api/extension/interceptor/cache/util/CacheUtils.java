package com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.util;

import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.CacheConfig;

import java.io.File;

public class CacheUtils {

	/**
	 * 得到KEY的唯一码
	 * 
	 * @param key
	 * @return
	 */
	public static String getHashForKey(String key) {
		int firstHalfLength = key.length() / 2;
		String hashKey = String.valueOf(key.substring(0, firstHalfLength).hashCode());
		hashKey += String.valueOf(key.substring(firstHalfLength).hashCode());
		return CacheConfig.CACHE_FILE_PREFIX + hashKey;
	}

	public static File getFileForKey(File rootDirectory, String key) {
		return new File(rootDirectory, getHashForKey(key));
	}

}
