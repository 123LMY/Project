package com.org.pojo;

public class ProjectMember {
	private long id;
	private long tUser_id;//用户id
	private long tProject_id;//项目id
	private char isLeader;//是否负责任
	private char isAdviser;//是否指导老师
	private String remark;//备注
	private User user;
	private Project project;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long gettUser_id() {
		return tUser_id;
	}
	public void settUser_id(long tUser_id) {
		this.tUser_id = tUser_id;
	}
	public long gettProject_id() {
		return tProject_id;
	}
	public void settProject_id(long tProject_id) {
		this.tProject_id = tProject_id;
	}
	public char getIsLeader() {
		return isLeader;
	}
	public void setIsLeader(char isLeader) {
		this.isLeader = isLeader;
	}
	public char getIsAdviser() {
		return isAdviser;
	}
	public void setIsAdviser(char isAdviser) {
		this.isAdviser = isAdviser;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	
}
