package com.lee.server.retrofit.entity;

import java.util.List;

public interface ErrorResponse extends Response{

	List<Error> getErrors();

}
