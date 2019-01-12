package com.org.pojo;



public class Node {
	
	private long id;
	private String nodeNo;//编号
	private String nodeName;//名称
	private String ipAddress;//ip地址
	private long tUser_id;//操作人
	private String remark;//备注
	private User operator;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNodeNo() {
		return nodeNo;
	}
	public void setNodeNo(String nodeNo) {
		this.nodeNo = nodeNo;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public long gettUser_id() {
		return tUser_id;
	}
	public void settUser_id(long tUser_id) {
		this.tUser_id = tUser_id;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public User getOperator() {
		return operator;
	}
	public void setOperator(User operator) {
		this.operator = operator;
	}
	
	
}
