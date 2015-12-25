package com.lee.server.retrofit;

import java.util.Calendar;
import java.util.Date;

import org.joda.time.Hours;

public class Test {

	public static void main(String[] args) throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		long time = calendar.getTimeInMillis();
		long times = time-System.currentTimeMillis();
		System.out.println(times/1000);
		System.out.println(times/1000/60);
		System.out.println(times/1000/60/60);
		System.out.println(calendar.toString());
		System.out.println("year is : " + calendar.get(Calendar.YEAR));
		System.out.println("year is : " + calendar.get(Calendar.MONTH));
		System.out.println("year is : " + calendar.get(Calendar.DATE));
		System.out.println("year is : " + calendar.get(Calendar.HOUR_OF_DAY));
		System.out.println("year is : " + calendar.get(Calendar.MINUTE));
		System.out.println("year is : " + calendar.get(Calendar.SECOND));
	}
}
