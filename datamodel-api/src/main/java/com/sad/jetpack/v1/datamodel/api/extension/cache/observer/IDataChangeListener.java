package com.sad.jetpack.v1.datamodel.api.extension.cache.observer;

/**
 * 数据变化监听接口
 *
 */
public interface IDataChangeListener {
    /**
     * 数据变化
     *
     * @param key   主键
     * @param value 值
     */
    void onDataChange(String key, String value);
}
