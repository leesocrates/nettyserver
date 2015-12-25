package com.lee.server.retrofit.utils;

public class MemcacheKeyUtils {

	public static String getCounterKey(String key) {
		return key + Constants.KEY_SUFFIX_COUNTER;
	}
}
