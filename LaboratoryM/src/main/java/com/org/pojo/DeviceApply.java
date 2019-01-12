package com.org.pojo;

import java.sql.Date;
import java.sql.Timestamp;

public class DeviceApply {
	private long id;
	private long tDeviceDetail_id;//设备详细
	private long tUser_id_b;//预约人
	private long tUser_id_a;//使用人
	private Date startDate;//开始日期
	private Date endDate;//结束日期
	private String purpose;//使用目的
	private char auditStatus;//审核状态
	private long tUser_id_o;//操作人
	private String remark;//备注
	private DeviceDetail deviceDetail;//设备
	private User reservations;//预约人
	private User user;//使用人
	private User operator;//操作人
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long gettDeviceDetail_id() {
		return tDeviceDetail_id;
	}
	public void settDeviceDetail_id(long tDeviceDetail_id) {
		this.tDeviceDetail_id = tDeviceDetail_id;
	}
	public long gettUser_id_b() {
		return tUser_id_b;
	}
	public void settUser_id_b(long tUser_id_b) {
		this.tUser_id_b = tUser_id_b;
	}
	public long gettUser_id_a() {
		return tUser_id_a;
	}
	public void settUser_id_a(long tUser_id_a) {
		this.tUser_id_a = tUser_id_a;
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
	public long gettUser_id_o() {
		return tUser_id_o;
	}
	public void settUser_id_o(long tUser_id_o) {
		this.tUser_id_o = tUser_id_o;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public DeviceDetail getDeviceDetail() {
		return deviceDetail;
	}
	public void setDeviceDetail(DeviceDetail deviceDetail) {
		this.deviceDetail = deviceDetail;
	}
	public User getReservations() {
		return reservations;
	}
	public void setReservations(User reservations) {
		this.reservations = reservations;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public User getOperator() {
		return operator;
	}
	public void setOperator(User operator) {
		this.operator = operator;
	}
	
}
