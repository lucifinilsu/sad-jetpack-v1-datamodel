package com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.transcoder;

import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.interfaces.Cache.Entry;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.interfaces.Transcoder;

public class StringTranscoder implements Transcoder<String, String> {

	@Override
	public Entry encode(String value, long ttl) {
		return new Entry(value.getBytes(), ttl);
	}

	@Override
	public String decode(Entry entry) {
		return new String(entry.getData());
	}

	@Override
	public String decodeKey(String key) {
		return key;
	}

}
