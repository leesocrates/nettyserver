package com.lee.server.retrofit.account;

import java.util.HashMap;
import java.util.Map;

import com.lee.server.retrofit.utils.SafeUtils;

/**
 * 用文件保存账号，这个仅仅用来测试的demo，真正的实现还是用数据库做，文件做实现比较繁琐
 * @author lee
 *
 */
public class AccountServiceFileImpl implements IAccountService {

	private static AccountServiceFileImpl mInstance;
	private Map<String, Account> accountMap;

	public static AccountServiceFileImpl getInstance() {
		if (mInstance == null) {
			synchronized (AccountServiceFileImpl.class) {
				if (mInstance == null) {
					mInstance = new AccountServiceFileImpl();
				}
			}
		}
		return mInstance;
	}

	private AccountServiceFileImpl() {
		accountMap = new HashMap<>();
	}

	@Override
	public boolean addAccount(Account account) {
		if (account != null && !accountMap.containsKey(account.userName)) {
			account.password = SafeUtils.passwordEncrypt(account.password);
			accountMap.put(account.userName, account);
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteAccount(Account account) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateAccount(Account account) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Account getAccount(String accountName) {
		return accountMap.get(accountName);
	}

}
