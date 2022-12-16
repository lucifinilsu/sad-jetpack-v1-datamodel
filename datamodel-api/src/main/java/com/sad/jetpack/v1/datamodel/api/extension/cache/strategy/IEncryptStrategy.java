package com.sad.jetpack.v1.datamodel.api.extension.cache.strategy;

/**
 * 加解密策略
 *
 */
public interface IEncryptStrategy {

    String encrypt(String str);

    String decode(String str);
}
