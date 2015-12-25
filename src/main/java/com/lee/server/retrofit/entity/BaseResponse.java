package com.lee.server.retrofit.entity;

public class BaseResponse implements Response{
	
	private boolean isSuccess;
	private String status;
	private String message;
	
	public BaseResponse() {
	}
	
	public BaseResponse(boolean isSuccess, String status, String message){
		this.isSuccess = isSuccess;
		this.status = status;
		this.message = message;
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

}
