package com.org.pojo;

import java.sql.Timestamp;

public class TempHumiRecord {

	private long id;
	private String extAddr;
	private char sensorType;
	private String t_h_value;
	private String takeTime;
	private long tUser_id;
	private User operator;

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

	public char getSensorType() {
		return sensorType;
	}

	public void setSensorType(char sensorType) {
		this.sensorType = sensorType;
	}

	public String getT_h_value() {
		return t_h_value;
	}

	public void setT_h_value(String t_h_value) {
		this.t_h_value = t_h_value;
	}

	public String getTakeTime() {
		return takeTime;
	}

	public void setTakeTime(String takeTime) {
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
