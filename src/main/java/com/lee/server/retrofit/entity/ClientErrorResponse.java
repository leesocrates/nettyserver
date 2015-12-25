package com.lee.server.retrofit.entity;

import java.util.ArrayList;
import java.util.List;

public class ClientErrorResponse implements ErrorResponse {

	private String message;
	private List<Error> errors;

	public ClientErrorResponse() {

	}

	public ClientErrorResponse(String message, ArrayList<Error> errors) {
		this.message = message;
		this.errors = errors;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return message;
	}

	@Override
	public List<Error> getErrors() {
		// TODO Auto-generated method stub
		return errors;
	}

	@Override
	public String getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSuccess() {
		// TODO Auto-generated method stub
		return false;
	}

}
