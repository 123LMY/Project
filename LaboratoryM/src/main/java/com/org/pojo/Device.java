package com.org.pojo;

import java.util.List;

public class Device {
	private long id;
	private String devModel;//型号
	private String devName;//名称
	private String vender;//厂家
	private long tDevType_id;//类别
	private long tUser_id_k;//管理员
	private long tUser_id_o;//操作人
	private String remark;//备注
	private DevType devType;//类型
	private User custodian;//保管员
	private User operator;//操作人
	private List<DeviceDetail> deviceDetail;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public String getVender() {
		return vender;
	}
	public void setVender(String vender) {
		this.vender = vender;
	}
	public long gettDevType_id() {
		return tDevType_id;
	}
	public void settDevType_id(long tDevType_id) {
		this.tDevType_id = tDevType_id;
	}
	public long gettUser_id_k() {
		return tUser_id_k;
	}
	public void settUser_id_k(long tUser_id_k) {
		this.tUser_id_k = tUser_id_k;
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
	public DevType getDevType() {
		return devType;
	}
	public void setDevType(DevType devType) {
		this.devType = devType;
	}
	public User getCustodian() {
		return custodian;
	}
	public void setCustodian(User custodian) {
		this.custodian = custodian;
	}
	public User getOperator() {
		return operator;
	}
	public void setOperator(User operator) {
		this.operator = operator;
	}
	public List<DeviceDetail> getDeviceDetail() {
		return deviceDetail;
	}
	public void setDeviceDetail(List<DeviceDetail> deviceDetail) {
		this.deviceDetail = deviceDetail;
	}
	
	
}
