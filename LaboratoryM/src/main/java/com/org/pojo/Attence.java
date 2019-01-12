package com.org.pojo;


import java.sql.Timestamp;

public class Attence {
	private long id;
	private long tUser_id;//用户id
	private Timestamp signinTime;//签到时间
	private Timestamp signoutTime;//签出时间
	private String remark;//备注
	private User user;//用户
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long gettUser_id() {
		return tUser_id;
	}
	public void settUser_id(long tUser_id) {
		this.tUser_id = tUser_id;
	}
	public Timestamp getSigninTime() {
		return signinTime;
	}
	public void signinTime(Timestamp signinTime) {
		this.signinTime = signinTime;
	}
	public Timestamp getSignoutTime() {
		return signoutTime;
	}
	public void setSignoutTime(Timestamp signoutTime) {
		this.signoutTime = signoutTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
}
