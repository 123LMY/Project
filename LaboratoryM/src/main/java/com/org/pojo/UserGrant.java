package com.org.pojo;



public class UserGrant {
	private long id;//id
	private long tUser_id_a;//用户
	private char noteTopic;//门禁权限
	private long tProject_id;//项目id
	private long tUser_id_o;//操作人
	private long tLab_id;
	private String remark;//备注
	private User user;//用户
	private User operator;//操作人
	private Project project;//项目
	
	public long gettProject_id() {
		return tProject_id;
	}
	public void settProject_id(long tProject_id) {
		this.tProject_id = tProject_id;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public char getNoteTopic() {
		return noteTopic;
	}
	public void setNoteTopic(char noteTopic) {
		this.noteTopic = noteTopic;
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
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public User getOperator() {
		return operator;
	}
	public void setOperator(User operator) {
		this.operator = operator;
	}
	public long gettUser_id_a() {
		return tUser_id_a;
	}
	public void settUser_id_a(long tUser_id_a) {
		this.tUser_id_a = tUser_id_a;
	}
	public long gettLab_id() {
		return tLab_id;
	}
	public void settLab_id(long tLab_id) {
		this.tLab_id = tLab_id;
	}
}
