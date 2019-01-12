package com.org.pojo;

import java.util.List;

public class Department {
	
	private long id;
	private String deptNo;
	private String deptName;
	private List<Profession> professionList;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDeptNo() {
		return deptNo;
	}
	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public List<Profession> getProfessionList() {
		return professionList;
	}
	public void setProfessionList(List<Profession> professionList) {
		this.professionList = professionList;
	}
	
	
}
