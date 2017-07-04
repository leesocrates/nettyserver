package com.lee.server.retrofit.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 各种加解密方法的工具类
 * @author lee
 *
 */
public class SafeUtils {

	public static String passwordEncrypt(String password){
		return DigestUtils.sha256Hex(password);
	}
}
