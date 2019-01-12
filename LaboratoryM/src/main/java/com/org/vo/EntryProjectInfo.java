package com.org.vo;

public class EntryProjectInfo {

	private char proType;//项目类别
	private String proTypeName;//类别名
	private int proTotal;//项目总数
	private int stuTotal;//学生总数
	private int teachTotal;//老师总数
	private int finish;//结题数
	private int schoolLevel;//学校等级
	private int provincialLevel;//省级
	private int nationalLevel;//国家级
	private int internationalLevel;//国际级
	private int patent;//专利数
	private int achievements;//成果转让
	public char getProType() {
		return proType;
	}
	public void setProType(char proType) {
		this.proType = proType;
	}
	public int getProTotal() {
		return proTotal;
	}
	public void setProTotal(int proTotal) {
		this.proTotal = proTotal;
	}
	public int getStuTotal() {
		return stuTotal;
	}
	public void setStuTotal(int stuTotal) {
		this.stuTotal = stuTotal;
	}
	public int getTeachTotal() {
		return teachTotal;
	}
	public void setTeachTotal(int teachTotal) {
		this.teachTotal = teachTotal;
	}
	public int getFinish() {
		return finish;
	}
	public void setFinish(int finish) {
		this.finish = finish;
	}
	public int getSchoolLevel() {
		return schoolLevel;
	}
	public void setSchoolLevel(int schoolLevel) {
		this.schoolLevel = schoolLevel;
	}
	public int getProvincialLevel() {
		return provincialLevel;
	}
	public void setProvincialLevel(int provincialLevel) {
		this.provincialLevel = provincialLevel;
	}
	public int getNationalLevel() {
		return nationalLevel;
	}
	public void setNationalLevel(int nationalLevel) {
		this.nationalLevel = nationalLevel;
	}
	public int getInternationalLevel() {
		return internationalLevel;
	}
	public void setInternationalLevel(int internationalLevel) {
		internationalLevel = internationalLevel;
	}
	public int getPatent() {
		return patent;
	}
	public void setPatent(int patent) {
		this.patent = patent;
	}
	public int getAchievements() {
		return achievements;
	}
	public void setAchievements(int achievements) {
		this.achievements = achievements;
	}
	public String getProTypeName() {
		return proTypeName;
	}
	public void setProTypeName(String proTypeName) {
		this.proTypeName = proTypeName;
	}
	
}
