package com.org.vo;

import java.util.List;

import com.org.pojo.User;

public class ProResultAndMemberInfo {

	private long proId;//项目id
	private String proName;//项目名
	
	private String contestName;//赛事名
	private int prizeYear;//获奖年份
	private String prizeName;//获奖名称
	private String prizeLevel;//获奖级别
	private String prize;//获奖信息
	
	private String patentName;//专利名字
	private int patentYear;//专利年份
	private String patent;//专利信息
	
	private String resultTransfer;//成功转让
	private int resultYear;//转让年份
	private String result;//转让信息
	
	private String proMembers;//项目成员
	private String teachers;//指导老师
	private List<User> proMember;
	private List<User> teacher;
	
	public long getProId() {
		return proId;
	}
	public void setProId(long proId) {
		this.proId = proId;
	}
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	public int getPrizeYear() {
		return prizeYear;
	}
	public void setPrizeYear(int prizeYear) {
		this.prizeYear = prizeYear;
	}
	public String getPrizeName() {
		return prizeName;
	}
	public void setPrizeName(String prizeName) {
		this.prizeName = prizeName;
	}
	public String getPrizeLevel() {
		return prizeLevel;
	}
	public void setPrizeLevel(String prizeLevel) {
		this.prizeLevel = prizeLevel;
	}
	public String getPatentName() {
		return patentName;
	}
	public void setPatentName(String patentName) {
		this.patentName = patentName;
	}
	public int getPatentYear() {
		return patentYear;
	}
	public void setPatentYear(int patentYear) {
		this.patentYear = patentYear;
	}
	public String getResultTransfer() {
		return resultTransfer;
	}
	public void setResultTransfer(String resultTransfer) {
		this.resultTransfer = resultTransfer;
	}
	public int getResultYear() {
		return resultYear;
	}
	public void setResultYear(int resultYear) {
		this.resultYear = resultYear;
	}
	public List<User> getProMember() {
		return proMember;
	}
	public void setProMember(List<User> proMember) {
		this.proMember = proMember;
	}
	public List<User> getTeacher() {
		return teacher;
	}
	public void setTeacher(List<User> teacher) {
		this.teacher = teacher;
	}
	public String getContestName() {
		return contestName;
	}
	public void setContestName(String contestName) {
		this.contestName = contestName;
	}
	public String getPrize() {
		return prize;
	}
	public void setPrize(String prize) {
		this.prize = prize;
	}
	public String getPatent() {
		return patent;
	}
	public void setPatent(String patent) {
		this.patent = patent;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getProMembers() {
		return proMembers;
	}
	public void setProMembers(String proMembers) {
		this.proMembers = proMembers;
	}
	public String getTeachers() {
		return teachers;
	}
	public void setTeachers(String teachers) {
		this.teachers = teachers;
	}
	
	
}
