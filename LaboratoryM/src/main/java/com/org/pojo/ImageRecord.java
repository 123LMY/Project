package com.org.pojo;


import java.sql.Timestamp;

public class ImageRecord {
	
	private long id;
	private String filePath;
	private Timestamp takeTime;
	private long tUser_id;
	private User operator;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
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
