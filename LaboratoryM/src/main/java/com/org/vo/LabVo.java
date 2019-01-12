package com.org.vo;

import java.sql.Timestamp;

public class LabVo {
	private long labId;
	private long labApplyId;
	
	private String labName;
	private char labStatus;
	private Integer userCount;
	private Timestamp startTime;
	private Timestamp endTime;
	private char auditStatus;
	public long getLabId() {
		return labId;
	}
	public void setLabId(long labId) {
		this.labId = labId;
	}
	public String getLabName() {
		return labName;
	}
	public void setLabName(String labName) {
		this.labName = labName;
	}
	public char getLabStatus() {
		return labStatus;
	}
	public void setLabStatus(char labStatus) {
		this.labStatus = labStatus;
	}
	public Integer getUserCount() {
		return userCount;
	}
	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public char getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(char auditStatus) {
		this.auditStatus = auditStatus;
	}
	public long getLabApplyId() {
		return labApplyId;
	}
	public void setLabApplyId(long labApplyId) {
		this.labApplyId = labApplyId;
	}
}
