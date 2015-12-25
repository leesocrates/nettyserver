package com.lee.server.retrofit.utils;

import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.gson.Gson;

public class Utils {

	private static final DateTimeFormatter RFC1123_DATE_TIME_FORMATTER = DateTimeFormat
			.forPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'").withZoneUTC()
			.withLocale(Locale.US);

	public static <T> T getObject(String jsonStr, Class<T> classzz) {
		Gson gson = new Gson();
		T t = gson.fromJson(jsonStr, classzz);
		return t;
	}

	public static String getJsonString(Object o) {
		Gson gson = new Gson();
		return gson.toJson(o);
	}

	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	public static String getFormatTime(Date date) {
		DateTime dateTime = new DateTime(date);
		return dateTime.toString();
	}

	public static String getHttpHeaderDate() {
		return RFC1123_DATE_TIME_FORMATTER.print(new DateTime());
	}

	public static boolean isNull(String s) {
		return s == null || s.length() == 0;
	}

	public static long getNextSecondStr() {
		DateTime dateTime = new DateTime();
		long curTime = dateTime.getMillis();
		return curTime / 1000 + 1;
	}
	
}
