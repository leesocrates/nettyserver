package com.lee.server.retrofit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.joda.time.Hours;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Test {

	public static void main(String[] args) throws Exception {
//		Calendar calendar = Calendar.getInstance();
//		calendar.add(Calendar.DATE, 1);
//		calendar.set(Calendar.HOUR_OF_DAY, 0);
//		calendar.set(Calendar.MINUTE, 0);
//		calendar.set(Calendar.SECOND, 0);
//		long time = calendar.getTimeInMillis();
//		long times = time-System.currentTimeMillis();
//		System.out.println(times/1000);
//		System.out.println(times/1000/60);
//		System.out.println(times/1000/60/60);
//		System.out.println(calendar.toString());
//		System.out.println("year is : " + calendar.get(Calendar.YEAR));
//		System.out.println("year is : " + calendar.get(Calendar.MONTH));
//		System.out.println("year is : " + calendar.get(Calendar.DATE));
//		System.out.println("year is : " + calendar.get(Calendar.HOUR_OF_DAY));
//		System.out.println("year is : " + calendar.get(Calendar.MINUTE));
//		System.out.println("year is : " + calendar.get(Calendar.SECOND));
//		System.out.println("TimeUnit.SECONDS.toNanos(1) : "+TimeUnit.SECONDS.toNanos(1));
//		
//		System.out.println('\"'+"women"+'\"');
//		System.out.println('\"');
//		String type ="1";
//		
//		String s = "javascript:getphoto('" + type + "','" + response + "','" + userinfo + "')";
//				System.out.println(s);
//		String deviceType = "6.12.12";
//		if(deviceType.matches("6(\\.[\\d]{1,2}){1,2}")){
//			System.out.println(deviceType+" is 6.0");
//		} else {
//			System.out.println(deviceType+" is not 6.0");
//		}
		Gson gson = new Gson();
		TypeToken<BaseMessageResponse<ArrayList<String>>> t = new TypeToken<BaseMessageResponse<ArrayList<String>>>(){};
		BaseMessageResponse<ArrayList<String>> l =gson.fromJson("{\"result\":[\"abc\"]}", t.getType());
		System.out.println(l);
	}
	
	private void f(){
		
	}
}
