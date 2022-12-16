package com.sad.jetpack.v1.datamodel.api.extension.cache.util;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 公平锁单例类
 *
 */
public class LockUtil {
    public static ReentrantReadWriteLock getInstance() {
        return ReentrantLockHolder.lock;
    }


    private static class ReentrantLockHolder {
        static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    }
}
