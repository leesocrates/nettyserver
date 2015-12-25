package com.lee.server.retrofit.entity;

public class ClientError implements Error{

	private String resource;
	private String field;
	private ErrorCode code;

	public void setResource(String resource) {
		this.resource = resource;
	}

	public void setField(String field) {
		this.field = field;
	}

	public void setCode(ErrorCode code) {
		this.code = code;
	}

	@Override
	public String getResource() {
		return resource;
	}

	@Override
	public String getField() {
		return field;
	}

	public ErrorCode getCode() {
		return code;
	}

}
