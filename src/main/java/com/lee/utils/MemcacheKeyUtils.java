package com.lee.utils;

public class MemcacheKeyUtils {

	public static String getCounterKey(String key) {
		return key + Constants.KEY_SUFFIX_COUNTER;
	}
}
