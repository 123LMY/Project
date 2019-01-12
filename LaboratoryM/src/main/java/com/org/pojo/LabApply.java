package com.org.pojo;


import java.sql.Timestamp;

public class LabApply {
	
	private long id;
	private long tLab_id;//实验室id
	private long tUser_id_b;//预约人
	private long tUser_id_a;//使用人
	private long tProject_id;//项目id
	private Timestamp startTime;//开始时间
	private Timestamp endTime;//结束时间
	private String purpose;//使用目的
	private char auditStatus;//审核状态
	private long tUser_id_o;//操作人
	private String remark;//备注
	private Lab lab;//实验室
	private User reservations;//预约人
	private User user;//使用人
	private User operator;//操作人
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long gettLab_id() {
		return tLab_id;
	}
	public void settLab_id(long tLab_id) {
		this.tLab_id = tLab_id;
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
	public Lab getLab() {
		return lab;
	}
	public void setLab(Lab lab) {
		this.lab = lab;
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
	public long gettProject_id() {
		return tProject_id;
	}
	public void settProject_id(long tProject_id) {
		this.tProject_id = tProject_id;
	}
	
}
