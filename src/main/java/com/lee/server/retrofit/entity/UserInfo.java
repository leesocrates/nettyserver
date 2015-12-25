package com.lee.server.retrofit.entity;

import java.util.Arrays;

public class UserInfo {
	private byte[] userIcon;
	private String nickName;

	public byte[] getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(byte[] userIcon) {
		this.userIcon = userIcon;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	@Override
	public String toString() {
		return "UserInfo{" + "userIcon=" + Arrays.toString(userIcon) + ", nickName='" + nickName + '\'' + '}';
	}
}
