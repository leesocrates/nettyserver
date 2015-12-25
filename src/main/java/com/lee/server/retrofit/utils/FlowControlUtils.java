package com.lee.server.retrofit.utils;

public class FlowControlUtils {

	public static long incrAndGetCounter(String key) {
		return MemcachedManager.getInstance().incr(MemcacheKeyUtils.getCounterKey(key), 1);
	}

	public static boolean BeyondFlowControl(long counter) {
		if (counter <= Constants.DEFAULT_RATELIMIT_COUNTER) {
			return false;
		} else {
			return true;
		}
	}

	public static long getRemainCounter(long counter) {
		long remain = Constants.DEFAULT_RATELIMIT_COUNTER - counter;
		if (remain < 0) {
			remain = 0;
		}
		if (remain >= 60) {
			remain = 59;
		}
		return remain;
	}

}
