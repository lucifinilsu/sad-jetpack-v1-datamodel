package com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.interfaces;

import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.interfaces.Cache.Entry;


public interface Transcoder<K, V> {
	
	public String decodeKey(K key);
	
	public V decode(Entry entry);

	public Entry encode(V value, long ttl);
}
