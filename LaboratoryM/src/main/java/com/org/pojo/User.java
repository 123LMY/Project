package com.org.pojo;



public class User {
	private long id;//id
	private String userName;//用户名
	private byte[] userPwd;//密码
	private String realName;//真实姓名
	private String phone;//电话
	private String email;//邮箱
	private char grants;//用户权限
	private char userType;//用户类别
	private String userTypeName;
	private char fingerMark;//掌脉
	private char status;//账号状态
	private String statusName;
	private String remark;//备注
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public byte[] getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(byte[] userPwd) {
		this.userPwd = userPwd;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public char getGrants() {
		return grants;
	}
	public void setGrants(char grants) {
		this.grants = grants;
	}
	public char getUserType() {
		return userType;
	}
	public void setUserType(char userType) {
		this.userType = userType;
	}
	public char getFingerMark() {
		return fingerMark;
	}
	public void setFingerMark(char fingerMark) {
		this.fingerMark = fingerMark;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUserTypeName() {
		return userTypeName;
	}
	public void setUserTypeName(String userTypeName) {
		this.userTypeName = userTypeName;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
}
