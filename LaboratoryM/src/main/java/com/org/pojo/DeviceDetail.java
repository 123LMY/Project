package com.org.pojo;


import java.sql.Date;


public class DeviceDetail {
	private long id;
	private String devSn;//设备序号
	private String rfidNo;//rfid
	private Date inDate;//入库日期
	private char devStatus;//状态
	private long tDevice_id;//设备号
	private long tLab_id;//实验室id
	private long tUser_id_o;//操作人
	private String remark;//设备
	private String cabNo;//柜子号
	private Device device;//设备
	private Lab lab;//实验室
	private User operator;//操作人
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDevSn() {
		return devSn;
	}
	public void setDevSn(String devSn) {
		this.devSn = devSn;
	}
	public String getRfidNo() {
		return rfidNo;
	}
	public void setRfidNo(String rfidNo) {
		this.rfidNo = rfidNo;
	}
	public Date getInDate() {
		return inDate;
	}
	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}
	public char getDevStatus() {
		return devStatus;
	}
	public void setDevStatus(char devStatus) {
		this.devStatus = devStatus;
	}
	public long gettDevice_id() {
		return tDevice_id;
	}
	public void settDevice_id(long tDevice_id) {
		this.tDevice_id = tDevice_id;
	}
	public long gettLab_id() {
		return tLab_id;
	}
	public void settLab_id(long tLab_id) {
		this.tLab_id = tLab_id;
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
	public Device getDevice() {
		return device;
	}
	public void setDevice(Device device) {
		this.device = device;
	}
	public Lab getLab() {
		return lab;
	}
	public void setLab(Lab lab) {
		this.lab = lab;
	}
	public User getOperator() {
		return operator;
	}
	public void setOperator(User operator) {
		this.operator = operator;
	}
	public String getCabNo() {
		return cabNo;
	}
	public void setCabNo(String cabNo) {
		this.cabNo = cabNo;
	}
	
}
