package com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.transcoder;

import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.interfaces.Cache.Entry;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.interfaces.Transcoder;

public class ByteTranscoder implements Transcoder<String, byte[]> {

	@Override
	public Entry encode(byte[] value, long ttl) {
		return new Entry(value, ttl);
	}

	@Override
	public byte[] decode(Entry entry) {
		return entry.getData();
	}

	@Override
	public String decodeKey(String key) {
		return key;
	}

}
