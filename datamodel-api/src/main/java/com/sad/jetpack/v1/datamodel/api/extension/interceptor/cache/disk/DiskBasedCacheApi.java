package com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.disk;

import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.CacheConfig;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.interfaces.CacheApi;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.interfaces.Transcoder;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.util.CacheUtils;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.util.CacheLog;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.cache.util.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 文件系统缓存
 *
 */
public class DiskBasedCacheApi extends CacheApi {
	public static final String TAG = DiskBasedCacheApi.class.getName();

	/**
	 * Map of the Key, CacheHeader pairs
	 */
	private final Map<String, CacheHeader> headers = new LinkedHashMap<String, CacheHeader>(16, .75f, true);

	/**
	 * 缓存占用总大小大小
	 */
	private long totalSize = 0;

	/**
	 * 文件缓存的根目录
	 */
	private File rootDirectory;

	/**
	 * 最多支持存放多少数据
	 */
	private long maxCacheSizeInBytes;

	/**
	 * 需要文件缓存的根目录
	 * 
	 * @param rootDirectory
	 */
	public DiskBasedCacheApi(File rootDirectory) {
		super(null);
		this.rootDirectory = rootDirectory;
		maxCacheSizeInBytes = CacheConfig.DEFAULT_DISK_USAGE_BYTES;
	}

	/**
	 * put/get 解码器 
	 * @param transcoder
	 */
	public DiskBasedCacheApi(Transcoder transcoder) {
		super(transcoder);
	}

	/**
	 * 缓存配置
	 * 
	 * @param cacheConfig
	 * @return
	 */
	@Override
	public void config(CacheConfig cacheConfig) {
		super.config(cacheConfig);
		if(cacheConfig.getDiskUsageBytes() > 0) {
			this.maxCacheSizeInBytes = cacheConfig.getDiskUsageBytes();
		}
		if(cacheConfig.getCacheRootDirectory() != null) {
			this.rootDirectory = cacheConfig.getCacheRootDirectory();
		}
	}

	/**
	 * 初始化缓存
	 */
	@Override
	public void initialize() {
		if(rootDirectory == null) {
			CacheLog.e(TAG,  "没有配置缓存路径");
			return;
		}
		if (!this.rootDirectory.exists()) {
			if (!this.rootDirectory.mkdirs()) {
				CacheLog.d(TAG, "Unable to create cache dir "
							+ this.rootDirectory .getAbsolutePath());
			}
			return;
		}

		File[] files = rootDirectory.listFiles();
		if (files == null) {
			return;
		}
		for (File file : files) {
			if(!file.getName().startsWith(CacheConfig.CACHE_FILE_PREFIX)) {
				continue ;
			}
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				CacheHeader header = CacheHeader.readHeader(fis);
				header.size = file.length();
				putHeader(header.key, header);
			} catch (IOException e) {
				CacheLog.e(TAG, "Failed to read header for" + file.getAbsolutePath(),  e);
				if (file != null) {
					file.delete();
				}
			} finally {
				try {
					if (fis != null) {
						fis.close();
					}
				} catch (IOException ignored) {
				}
			}
		}
	}

	/**
	 * 使缓存失效
	 * 
	 * @param key
	 */
	@Override
	public synchronized <K> void invalidate(K key) {
		CacheHeader header = headers.get(keyToString(key));
		header.ttl = 0;
	}

	/**
	 * 把数据放入缓存中
	 * 
	 * @param key
	 * @param entry
	 */
	@Override
	public <K> void putEntry(K key, Entry entry) {
		pruneIfNeeded(entry.size());
		File file = CacheUtils.getFileForKey(rootDirectory, keyToString(key));
		try {
			FileOutputStream fos = new FileOutputStream(file);
			CacheHeader header = new CacheHeader(keyToString(key), entry);
			boolean success = header.writeHeader(fos);
			if (!success) {
				fos.close();
				throw new IOException();
			}
			fos.write(entry.getData());
			fos.close();
			putHeader(keyToString(key), header);
			return;
		} catch (IOException e) {
			CacheLog.d(TAG, "Failed to write header for "
						+ file.getAbsolutePath(), e);
		}
		boolean deleted = file.delete();
		if (!deleted) {
			CacheLog.d(TAG, "Could not clean up file "
								+ file.getAbsolutePath());
		}
	}

	/**
	 * 把数据头记住
	 * 
	 * @param key
	 * @param header
	 */
	public void putHeader(String key, CacheHeader header) {
		if (!headers.containsKey(key)) {
			totalSize += header.size;
		} else {
			CacheHeader oldEntry = headers.get(key);
			totalSize += (header.size - oldEntry.size);
		}
		headers.put(key, header);
	}

	/**
	 * 获取缓存数据
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public <K> Entry getEntry(K key) {
		CacheHeader entry = headers.get(keyToString(key));
		// if the entry does not exist, return.
		if (entry == null) {
			return null;
		}
		if (entry.isExpired()) {
			remove(key);
			return null;
		}

		File file = CacheUtils.getFileForKey(rootDirectory, keyToString(key));
		CountingInputStream cis = null;
		try {
			cis = new CountingInputStream(new FileInputStream(file));
			CacheHeader.readHeader(cis); // eat header
			byte[] data = StreamUtils.streamToBytes(cis, (int) (file.length() - cis.bytesRead));
			return entry.toCacheEntry(data);
		} catch (IOException e) {
			CacheLog.d(TAG, "Could not read cache data for " + file.getAbsolutePath(), e);
			remove(key);
			return null;
		} finally {
			if (cis != null) {
				try {
					cis.close();
				} catch (IOException ioe) {
					return null;
				}
			}
		}
	}

	/**
	 * 是否存在该缓存
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public <K> boolean contains(K key) {
		CacheHeader header = headers.get(keyToString(key));
		if (header != null && !header.isExpired()) {
			return true;
		}
		remove(key);
		return false;
	}

	/**
	 * 删除缓存数据
	 * 
	 * @param key
	 */
	@Override
	public synchronized <K>  void remove(K key) {
		File file = CacheUtils.getFileForKey(rootDirectory, keyToString(key));
		CacheHeader header = headers.get(keyToString(key));
		if (header != null) {
			totalSize -= header.size;
			headers.remove(keyToString(key));
		}
		if(file.exists()) {
			if (!file .delete()) {
				CacheLog.d(TAG,
						String.format("Could not delete cache entry for key=%s, filename=%s",
								key,
								CacheUtils.getHashForKey(keyToString(key))));
				}
		}
	}

	/**
	 * 清除所有缓存
	 */
	@Override
	public synchronized void clear() {
		File[] files = rootDirectory.listFiles();
		if (files != null) {
			for (File file : files) {
				if(!file.getName().startsWith(CacheConfig.CACHE_FILE_PREFIX)) {
					continue ;
				}
				file.delete();
			}
		}
		headers.clear();
		totalSize = 0;
	}

	/**
	 * 是否够用空间，如果不够用，清除不常用的
	 * 
	 * @param neededSpace
	 */
	private void pruneIfNeeded(int neededSpace) {
		if ((totalSize + neededSpace) < maxCacheSizeInBytes) {
			return;
		}

		long before = totalSize;
		int prunedFiles = 0;
		long startTime = System.currentTimeMillis();

		Iterator<Map.Entry<String, CacheHeader>> iterator = headers.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, CacheHeader> headerEntry = iterator.next();
			CacheHeader header = headerEntry.getValue();
			boolean deleted = CacheUtils.getFileForKey(rootDirectory, header.key).delete();
			if (deleted) {
				totalSize -= header.size;
			} else {
				CacheLog.d(TAG,
						String.format("Could not delete cache entry for key=%s, filename=", 
						header.key, CacheUtils.getHashForKey(header.key)));
			}
			iterator.remove();
			prunedFiles++;

			if ((totalSize + neededSpace)  < maxCacheSizeInBytes * CacheConfig.HYSTERESIS_FACTOR) {
				break;
			}
		}

		CacheLog.v(TAG, String.format("pruned %d files, %d bytes, %d ms",
				prunedFiles, (totalSize - before),
				System.currentTimeMillis() - startTime));
	}

}
