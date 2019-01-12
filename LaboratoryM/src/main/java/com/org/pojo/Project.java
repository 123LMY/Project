package com.org.pojo;


import java.sql.Date;
import java.util.List;

public class Project {
	private long id;
	private String proName;//项目名称
	private char proType;//项目类别
	private Date startTime;//启动日期
	private Date endTime;//结束日期
	private String showLink;//展示路径
	private long tUser_id;//操作人
	private String remark;//备注
	private User operator;
	private List<ProjectMember> projectMemberList;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	public char getProType() {
		return proType;
	}
	public void setProType(char proType) {
		this.proType = proType;
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
	public String getShowLink() {
		return showLink;
	}
	public void setShowLink(String showLink) {
		this.showLink = showLink;
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
	public List<ProjectMember> getProjectMemberList() {
		return projectMemberList;
	}
	public void setProjectMemberList(List<ProjectMember> projectMemberList) {
		this.projectMemberList = projectMemberList;
	}
	
	
}
