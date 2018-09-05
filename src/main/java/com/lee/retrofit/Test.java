package com.lee.retrofit;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lee.retrofit.account.Account;

public class Test {

	public static void main(String[] args) {
		String result = "{\"userName\":\"dghjjj\",\"password\":\"aaaaaa\"}";
		Gson gson = new Gson();
		Type type = new TypeToken<Account>() {
		}.getType();
		Account account = gson.fromJson(result, type);
		System.out.println(account);
		
	}
}
