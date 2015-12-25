package com.lee.server.retrofit.entity;

public interface Error {

	String getResource();

	String getField();

	ErrorCode getCode();

	public static enum ErrorCode {
		missing, missing_field, invalid, already_exists, upgrade_required, custom
	}
}
