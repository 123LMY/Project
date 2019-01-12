package com.org.vo;

import java.util.Date;

public class DeviceVo {
	private long id;
	private long devApplyId;
	private String appointmentUser;
	private String user;
	private String operator;
	private String devModel;
	private String devName;
	private String devSn;
	private char devStatus;
	private Date startTime;
	private Date endTime;
	private char auditStatus;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAppointmentUser() {
		return appointmentUser;
	}
	public void setAppointmentUser(String appointmentUser) {
		this.appointmentUser = appointmentUser;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getDevModel() {
		return devModel;
	}
	public void setDevModel(String devModel) {
		this.devModel = devModel;
	}
	public String getDevName() {
		return devName;
	}
	public void setDevName(String devName) {
		this.devName = devName;
	}
	public String getDevSn() {
		return devSn;
	}
	public void setDevSn(String devSn) {
		this.devSn = devSn;
	}
	public char getDevStatus() {
		return devStatus;
	}
	public void setDevStatus(char devStatus) {
		this.devStatus = devStatus;
	}
	public long getDevApplyId() {
		return devApplyId;
	}
	public void setDevApplyId(long devApplyId) {
		this.devApplyId = devApplyId;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public char getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(char auditStatus) {
		this.auditStatus = auditStatus;
	}
	 
	
}
