package com.org.pojo;



public class Lab {
	
	private long id;
	private String labName;//名称
	private String labSite;//地点
	private String labFunction;//职能
	private char labStatus;//状态
	private long tUser_id_k;//管理员
	private long tUser_id_o;//操作人
	private String remark;//备注
	private User administrators;//管理员
	private User operator;//操作人
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getLabName() {
		return labName;
	}
	public void setLabName(String labName) {
		this.labName = labName;
	}
	public String getLabSite() {
		return labSite;
	}
	public void setLabSite(String labSite) {
		this.labSite = labSite;
	}
	public String getLabFunction() {
		return labFunction;
	}
	public void setLabFunction(String labFunction) {
		this.labFunction = labFunction;
	}
	public char getLabStatus() {
		return labStatus;
	}
	public void setLabStatus(char labStatus) {
		this.labStatus = labStatus;
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
	public User getAdministrators() {
		return administrators;
	}
	public void setAdministrators(User administrators) {
		this.administrators = administrators;
	}
	public User getOperator() {
		return operator;
	}
	public void setOperator(User operator) {
		this.operator = operator;
	}
	

}
