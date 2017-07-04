package com.lee.server.retrofit.account;

import java.util.HashMap;
import java.util.Map;

public class AccountServiceManager {

	public static final String SERVICE_TYPE_FILE = "file";
	public static final String SERVICE_TYPE_DATABASE = "database";
	private static AccountServiceManager mInstance;

	private Map<String, IAccountService> serviceCache;

	public static AccountServiceManager getInstance() {
		if (mInstance == null) {
			synchronized (AccountServiceManager.class) {
				if (mInstance == null) {
					mInstance = new AccountServiceManager();
				}
			}
		}
		return mInstance;
	}

	private AccountServiceManager() {
		serviceCache = new HashMap<>();
	}

	public IAccountService getAccountService(String serviceType) {
		if (serviceCache.containsKey(serviceType)) {
			return serviceCache.get(serviceType);
		}
		IAccountService accountService = createService(serviceType);
		serviceCache.put(serviceType, accountService);
		return accountService;
	}

	private IAccountService createService(String serviceType) {
		IAccountService accountService = null;
		switch (serviceType) {
		case SERVICE_TYPE_FILE:
			accountService = AccountServiceFileImpl.getInstance();
			break;
		case SERVICE_TYPE_DATABASE:
			accountService = new AccountServiceDatabaseImpl();
			break;
		default:
			throw new IllegalArgumentException("could not create "+serviceType +" accountSerive");
		}
		return accountService;
	}
}
