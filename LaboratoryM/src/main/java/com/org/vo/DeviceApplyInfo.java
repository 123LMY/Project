package com.org.vo;

import java.sql.Date;

public class DeviceApplyInfo {

	private long devDetailId;//子设备id
	private long devApplyId;//设备申请id
	private String devModel;//设备型号
	private String devName;//设备名称
	private char devStatus;//设备状态
	private String devStatusName;//设备状态名
	private String devSn;//设备序号
	private Date startDate;//开始时间
	private Date endDate;//结束时间
	private String purpose;//使用目的
	private char auditStatus;//审核状态
	private String auditStatusName;//审核状态名
	private String reservations;//预约人
	private String duser;//使用人
	private long tUser_id_a;
	private String proName;//项目名
	public long getDevDetailId() {
		return devDetailId;
	}
	public void setDevDetailId(long devDetailId) {
		this.devDetailId = devDetailId;
	}
	public long getDevApplyId() {
		return devApplyId;
	}
	public void setDevApplyId(long devApplyId) {
		this.devApplyId = devApplyId;
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
	public char getDevStatus() {
		return devStatus;
	}
	public void setDevStatus(char devStatus) {
		this.devStatus = devStatus;
	}
	public String getDevSn() {
		return devSn;
	}
	public void setDevSn(String devSn) {
		this.devSn = devSn;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public char getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(char auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getReservations() {
		return reservations;
	}
	public void setReservations(String reservations) {
		this.reservations = reservations;
	}
	public String getDuser() {
		return duser;
	}
	public void setDuser(String duser) {
		this.duser = duser;
	}
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	public String getDevStatusName() {
		return devStatusName;
	}
	public void setDevStatusName(String devStatusName) {
		this.devStatusName = devStatusName;
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
	
	
}
