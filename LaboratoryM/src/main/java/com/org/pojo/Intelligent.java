package com.org.pojo;

public class Intelligent {
	private long id;
	private String node; // 设备名称
	private String status; // 状态
	private String useTime; // 操作时间
	private String useName; // 操作人
	private String extAddr;// 设备地址

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getExtAddr() {
		return extAddr;
	}

	public void setExtAddr(String extAddr) {
		this.extAddr = extAddr;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUseTime() {
		return useTime;
	}

	public void setUseTime(String useTime) {
		this.useTime = useTime;
	}

	public String getUseName() {
		return useName;
	}

	public void setUseName(String useName) {
		this.useName = useName;
	}

}
