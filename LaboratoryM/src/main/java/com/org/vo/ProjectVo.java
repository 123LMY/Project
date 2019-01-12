package com.org.vo;

import java.util.ArrayList;
import java.util.Date;
public class ProjectVo {
	private long id;
	private long labId;
	private String proName;
	private char proType;
	private ArrayList<String> projectMembers;
	private String projectLeader;
	private String projectTutor;
	private String foreMember;
	private String projectStatus;
	private String projectOperation;
	private Date endTime;
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
	public ArrayList<String> getProjectMembers() {
		return projectMembers;
	}
	public void setProjectMembers(ArrayList<String> projectMembers) {
		this.projectMembers = projectMembers;
	}
	public String getProjectLeader() {
		return projectLeader;
	}
	public void setProjectLeader(String projectLeader) {
		this.projectLeader = projectLeader;
	}
	public String getProjectTutor() {
		return projectTutor;
	}
	public void setProjectTutor(String projectTutor) {
		this.projectTutor = projectTutor;
	}
	public String getForeMember() {
		return foreMember;
	}
	public void setForeMember(String foreMember) {
		this.foreMember = foreMember;
	}
	public String getProjectStatus() {
		return projectStatus;
	}
	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}
	public long getLabId() {
		return labId;
	}
	public void setLabId(long labId) {
		this.labId = labId;
	}
	public String getProjectOperation() {
		return projectOperation;
	}
	public void setProjectOperation(String projectOperation) {
		this.projectOperation = projectOperation;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	
	
	
}
