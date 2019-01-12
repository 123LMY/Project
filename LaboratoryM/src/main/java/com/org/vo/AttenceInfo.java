package com.org.vo;

import java.sql.Timestamp;

public class AttenceInfo {

	private long id;
	private String userName;//用户名
	private String realName;//真实姓名
	private String signinTime;//进入时间
	private String signoutTime;//离开时间
	private long tUser_id;

	private String proName;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getSigninTime() {
		return signinTime;
	}
	public void setSigninTime(String signinTime) {
		this.signinTime = signinTime;
	}
	public String getSignoutTime() {
		return signoutTime;
	}
	public void setSignoutTime(String signoutTime) {
		this.signoutTime = signoutTime;
	}
	public long gettUser_id() {
		return tUser_id;
	}
	public void settUser_id(long tUser_id) {
		this.tUser_id = tUser_id;
	}

	
}
