package com.lee.server.retrofit.account;

public interface IAccountService {

	boolean addAccount(Account account);
	boolean deleteAccount(Account account);
	boolean updateAccount(Account account);
	Account getAccount(String accountName);
}
