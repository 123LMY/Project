package com.org.pojo;

import java.sql.Timestamp;

public class IntelligentDevice {

	private long id;
	private String extAddr;
	private String idevName;
	private char idevStatus;
	private long dataValue;
	private Timestamp takeTime;
	private long tUser_id;
	private User operator;// 操作人

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

	public String getIdevName() {
		return idevName;
	}

	public void setIdevName(String idevName) {
		this.idevName = idevName;
	}

	public char getIdevStatus() {
		return idevStatus;
	}

	public void setIdevStatus(char idevStatus) {
		this.idevStatus = idevStatus;
	}

	public long getDataValue() {
		return dataValue;
	}

	public void setDataValue(long dataValue) {
		this.dataValue = dataValue;
	}

	public Timestamp getTakeTime() {
		return takeTime;
	}

	public void setTakeTime(Timestamp takeTime) {
		this.takeTime = takeTime;
	}

	public long gettUser_id() {
		return tUser_id;
	}

	public void settUser_id(long tUser_id) {
		this.tUser_id = tUser_id;
	}

	public User getOperator() {
		return operator;
	}

	public void setOperator(User operator) {
		this.operator = operator;
	}

}
