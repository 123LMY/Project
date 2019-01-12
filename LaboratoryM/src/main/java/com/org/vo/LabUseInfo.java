package com.org.vo;

public class LabUseInfo {

	private long labid;//实验室id
	private String labName;//实验室名
	private int useNum;//在用人数
	private int useTime;//累计使用小时数
	private int lendNum;//
	public String getLabName() {
		return labName;
	}
	public void setLabName(String labName) {
		this.labName = labName;
	}
	public int getUseNum() {
		return useNum;
	}
	public void setUseNum(int useNum) {
		this.useNum = useNum;
	}
	public int getUseTime() {
		return useTime;
	}
	public void setUseTime(int useTime) {
		this.useTime = useTime;
	}
	public int getLendNum() {
		return lendNum;
	}
	public void setLendNum(int lendNum) {
		this.lendNum = lendNum;
	}
	public long getLabid() {
		return labid;
	}
	public void setLabid(long labid) {
		this.labid = labid;
	}
	
	
}
