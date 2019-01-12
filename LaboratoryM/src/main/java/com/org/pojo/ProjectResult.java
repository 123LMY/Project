package com.org.pojo;


public class ProjectResult {
	private long id;
	private long tProject_id;//项目id
	private String resultType;//项目类型
	private String contestName;//比赛名
	private String prizeLevel;//获奖级别
	private String prizeName;//获奖名称
	private String prizeFile;//获奖图片
	private String patentName;//专利名
	private String patentFile;//专利名字
	private String resultTransfer;//成功转让
	private String resultFile;//转让文件
	private long tUser_id;//操作人
	private String remark;//备注
	private int prizeYear;//获奖年份
	private int patentYear;//专利年份
	private int resultYear;//转让年份
	private Project project;//项目
	private User operator;//操作人
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long gettProject_id() {
		return tProject_id;
	}
	public void settProject_id(long tProject_id) {
		this.tProject_id = tProject_id;
	}
	public String getResultType() {
		return resultType;
	}
	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
	public String getContestName() {
		return contestName;
	}
	public void setContestName(String contestName) {
		this.contestName = contestName;
	}
	public String getPrizeLevel() {
		return prizeLevel;
	}
	public void setPrizeLevel(String prizeLevel) {
		this.prizeLevel = prizeLevel;
	}
	public String getPrizeName() {
		return prizeName;
	}
	public void setPrizeName(String prizeName) {
		this.prizeName = prizeName;
	}
	public String getPatentName() {
		return patentName;
	}
	public void setPatentName(String patentName) {
		this.patentName = patentName;
	}
	public String getResultTransfer() {
		return resultTransfer;
	}
	public void setResultTransfer(String resultTransfer) {
		this.resultTransfer = resultTransfer;
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
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public User getOperator() {
		return operator;
	}
	public void setOperator(User operator) {
		this.operator = operator;
	}
	public String getPrizeFile() {
		return prizeFile;
	}
	public void setPrizeFile(String prizeFile) {
		this.prizeFile = prizeFile;
	}
	public String getPatentFile() {
		return patentFile;
	}
	public void setPatentFile(String patentFile) {
		this.patentFile = patentFile;
	}
	public String getResultFile() {
		return resultFile;
	}
	public void setResultFile(String resultFile) {
		this.resultFile = resultFile;
	}
	public int getPrizeYear() {
		return prizeYear;
	}
	public void setPrizeYear(int prizeYear) {
		this.prizeYear = prizeYear;
	}
	public int getPatentYear() {
		return patentYear;
	}
	public void setPatentYear(int patentYear) {
		this.patentYear = patentYear;
	}
	public int getResultYear() {
		return resultYear;
	}
	public void setResultYear(int resultYear) {
		this.resultYear = resultYear;
	}
	
}
