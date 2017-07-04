package com.lee.server.retrofit.entity;

import java.util.List;

public class BaseResponse<T> implements Response {

	private boolean isSuccess;
	private String status;
	private String message;
	private List<T> data;

	public BaseResponse() {
	}

	public BaseResponse(boolean isSuccess, String status, String message) {
		this(isSuccess, status, message, null);
	}

	public BaseResponse(boolean isSuccess, String status, String message, List<T> data) {
		this.isSuccess = isSuccess;
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public boolean isSuccess() {
		return isSuccess;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "BaseResponse [isSuccess=" + isSuccess + ", status=" + status + ", message=" + message + ", data=" + data
				+ "]";
	}
	
	

}
