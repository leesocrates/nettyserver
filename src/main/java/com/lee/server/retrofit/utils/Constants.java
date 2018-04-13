package com.lee.server.retrofit.utils;

public class Constants {


	public static final String APP_PACKAGE_NAME = "com.care";

	public static final String KEY_SUFFIX_PROFILE = "profile";
	public static final String KEY_SUFFIX_APP = "app";
	public static final String KEY_SUFFIX_COUNTER = "counter";

	public static final String HEADER_KEY_X_RATELIMIT_LIMIT = "X-RateLimit-Limit";
	public static final String HEADER_KEY_X_RATELIMIT_REMAINING = "X-RateLimit-Remaining";
	public static final String HEADER_KEY_X_RATELIMIT_RESET = "X-RateLimit-Reset";
	public static final String HEADER_KEY_X_CONTENT_TYPE_OPTIONS = "X-Content-Type-Options";
	public static final String HEADER_KEY_X_APPVERSION_NEW = "X-AppVersion-New";
	public static final String HEADER_KEY_X_REAL_IP = "X-Real-IP";
	public static final String HEADER_KEY_STATUS = "status";
	public static final String HEADER_KEY_X_DIAGNOSE_MEDIA_TYPE = "X-Diagnose-Media-Type";
	public static final String HEADER_VALUE_X_CONTENT_TYPE_OPTIONS = "nosniff";
	public static final String HEADER_VALUE_X_DIAGNOSE_MEDIA_TYPE = "retrofitServer.v1";
	public static final String HEADER_VALUE_DEFAULT_X_RateLimit_Limit = "60";
	public static final String HEADER_VALUE_CONTENT_TYPE_JSON = "application/json; charset=UTF-8";
	public static final String HEADER_VALUE_CONTENT_TYPE_HTML = "text/html";
	public static final String HEADER_VALUE_CONTENT_TYPE_TEXT = "text";
	public static final String HEADER_VALUE_CONTENT_TYPE_JS = "application/javascript";
	public static final String HEADER_VALUE_CACHE_CONTROL = "max-age=0, private, must-revalidate";
	public static final String HEADER_VALUE_SERVER = "socrate.lee.com";
	public static final String HEADER_KEY_CONTENT_TYPE = "Content-type";

	public static final String DEFAULT_URL = "https://api.github.com/authorizations/1";
	public static final String DEFAULT_NOTE_URL = "http://optional/note/url";
	public static final String SCOPE_USER_PROFILE = "user:profile";
	public static final String APP_NAME = "Password Manager";
	public static final int AUTHORIZATION_ID = 1;

	public static final String BAD_REQUEST_ERROR_MESSAGE = "{\"message\":\"Body should be a JSON object\"}";
	public static final String BAD_REQUEST_NOT_JSON_OBJECT = "Body should be a JSON object";
	public static final String BAD_REQUEST_NO_USER_AGENT = "Request forbidden by administrative rules.Please make sure your request has a User-Agent header.";

	public static final int DEFAULT_EXPIRE_TIME = 7 * 24 * 60 * 60;
	public static final int DEFAULT_COUNTER_EXPIRE_TIME = 1;
	public static final int DEFAULT_COUNTER_INIT_VALUE = 1;
	public static final long DEFAULT_RATELIMIT_COUNTER = 60;
	
	public static class Status{
		public static final String STATUS_SUCCESS = "0";
		public static final String STATUS_REGIST_FAIL = "1";
		public static final String STATUS_LOGIN_FAIL = "2";
	}
}
