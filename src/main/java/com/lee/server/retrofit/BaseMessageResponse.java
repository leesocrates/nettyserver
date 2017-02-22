package com.lee.server.retrofit;

public class BaseMessageResponse<T> {

	public String msg;
	public T result;
	@Override
	public String toString() {
		return "BaseMessageResponse [msg=" + msg + ", result=" + result + "]";
	}
}
