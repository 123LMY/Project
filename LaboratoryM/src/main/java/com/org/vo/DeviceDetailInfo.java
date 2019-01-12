package com.org.vo;

import java.sql.Date;

public class DeviceDetailInfo {

	private long id;//设备id
	private String devSn;//设备序号
	private String rfidNo;//rfid
	private Date inDate;//入库时间
	private char devStatus;
	private String devStatusName;//设备状态
	private String remark;//备注
	private String usingName;//使用人
	private String labName;//实验室名
	private String cabNo;
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
	public String getDevStatusName() {
		return devStatusName;
	}
	public void setDevStatusName(String devStatusName) {
		this.devStatusName = devStatusName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUsingName() {
		return usingName;
	}
	public void setUsingName(String usingName) {
		this.usingName = usingName;
	}
	public String getLabName() {
		return labName;
	}
	public void setLabName(String labName) {
		this.labName = labName;
	}
	public char getDevStatus() {
		return devStatus;
	}
	public void setDevStatus(char devStatus) {
		this.devStatus = devStatus;
	}
	public String getCabNo() {
		return cabNo;
	}
	public void setCabNo(String cabNo) {
		this.cabNo = cabNo;
	}
	
	
}
