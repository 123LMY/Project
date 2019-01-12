package com.org.vo;

import java.sql.Date;
import java.sql.Timestamp;

public class LabUsingInfo {
	
	private long labid;//实验室id
	private long labapplyid;//实验室申请id
	private String labName;//实验室名
	private char labStatus;//实验室状态
	private String labStatusName;//实验室状态名
	private int userNum;//在用人数
	private String startTime;//开始时间
	private String endTime;//结束时间
	private String purpose;//使用目的
	private char auditStatus;//审核状态
	private String auditStatusName;//审核状态名
	private String reservations;//预约人
	private String proName;//项目名
	private String luser;//使用人
	private long tUser_id_a;//使用人id
	private long tProject_id;//项目组id
	
	
	public long getLabid() {
		return labid;
	}
	public void setLabid(long labid) {
		this.labid = labid;
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
	public String getLabStatusName() {
		return labStatusName;
	}
	public void setLabStatusName(String labStatusName) {
		this.labStatusName = labStatusName;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	
	public String getReservations() {
		return reservations;
	}
	public void setReservations(String reservations) {
		this.reservations = reservations;
	}
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	public String getLuser() {
		return luser;
	}
	public void setLuser(String luser) {
		this.luser = luser;
	}
	public int getUserNum() {
		return userNum;
	}
	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}
	public long getLabapplyid() {
		return labapplyid;
	}
	public void setLabapplyid(long labapplyid) {
		this.labapplyid = labapplyid;
	}
	public char getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(char auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getAuditStatusName() {
		return auditStatusName;
	}
	public void setAuditStatusName(String auditStatusName) {
		this.auditStatusName = auditStatusName;
	}
	public long gettUser_id_a() {
		return tUser_id_a;
	}
	public void settUser_id_a(long tUser_id_a) {
		this.tUser_id_a = tUser_id_a;
	}
	public long gettProject_id() {
		return tProject_id;
	}
	public void settProject_id(long tProject_id) {
		this.tProject_id = tProject_id;
	}
}
