package com.org.pojo;



public class Profession {
	
	private long id;
	private String profNo;
	private String profName;
	private long tDepartment_id;
	private Department department;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getProfNo() {
		return profNo;
	}
	public void setProfNo(String profNo) {
		this.profNo = profNo;
	}
	public String getProfName() {
		return profName;
	}
	public void setProfName(String profName) {
		this.profName = profName;
	}
	public long gettDepartment_id() {
		return tDepartment_id;
	}
	public void settDepartment_id(long tDepartment_id) {
		this.tDepartment_id = tDepartment_id;
	}
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	
}
