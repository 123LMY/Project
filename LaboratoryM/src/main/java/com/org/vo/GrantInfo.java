package com.org.vo;

import java.sql.Date;

import com.org.pojo.User;

public class GrantInfo {
	private long id;//id
	private long tUser_id_a;//使用人
	private char noteTopic;//门禁权限
	private String labName;//实验室名称
	private long lab_id;//实验室id
 	private String topic;//权限名
	private String proName;//项目名
	private Date startTime;//开始时间
	private Date endTime;//结束时间
	private String status;//状态
	private String userName;//用户名
	private User user;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long gettUser_id_a() {
		return tUser_id_a;
	}
	public void settUser_id_a(long tUser_id_a) {
		this.tUser_id_a = tUser_id_a;
	}
	public char getNoteTopic() {
		return noteTopic;
	}
	public void setNoteTopic(char noteTopic) {
		this.noteTopic = noteTopic;
	}
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getLabName() {
		return labName;
	}
	public void setLabName(String labName) {
		this.labName = labName;
	}
	public long getLab_id() {
		return lab_id;
	}
	public void setLab_id(long lab_id) {
		this.lab_id = lab_id;
	}
	
}
