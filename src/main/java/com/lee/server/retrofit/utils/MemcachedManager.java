package com.lee.server.retrofit.utils;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

public class MemcachedManager {

	private static MemcachedClient mcc;
	private static final Logger logger = LoggerFactory
			.getLogger(MemcachedManager.class);
	private static final String SERVER_ADDRESS = "10.1.0.91:22322";

	public static MemcachedManager getInstance() {
		return SingletonHolder.mOnlyInstance;
	}

	private MemcachedManager() {
		try {

			mcc = new MemcachedClient(AddrUtil.getAddresses(SERVER_ADDRESS));
			logger.info("connect to memcache server address " + SERVER_ADDRESS
					+ ", status success");
		} catch (IOException e) {
			logger.info("connect to memcache server address " + SERVER_ADDRESS
					+ ", status fail, cause is IOException");
			e.printStackTrace();
		}
	}

	private static class SingletonHolder {
		private static final MemcachedManager mOnlyInstance = new MemcachedManager();
	}

	public void set(String key, Object value) {
		mcc.set(key, Constants.DEFAULT_EXPIRE_TIME, value);
	}

	public void set(String key, int exp, Object value) {
		mcc.set(key, exp, value);
	}

	public void replace(String key, Object value) {
		mcc.replace(key, Constants.DEFAULT_EXPIRE_TIME, value);
	}

	public void replace(String key, int exp, Object value) {
		mcc.replace(key, exp, value);
	}

	public Object get(String key) {
		return mcc.get(key);
	}

	public void delete(String key) {
		mcc.delete(key);
	}

	public long incr(String key, long by) {
		return mcc.incr(key, by, Constants.DEFAULT_COUNTER_INIT_VALUE,
				Constants.DEFAULT_COUNTER_EXPIRE_TIME);
	}

}
