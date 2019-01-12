package com.org.vo;

public class DeviceInfo {
	private long id;//设备id
	private String devModel;//设备型号
	private String devName;//设备名称
	private int useNum;//使用次数
	private int userNum;//使用人数
	private int useDate;//累计小时数
	private int buyNum;//购入总数
	private int stock;//在库
	private int damageNum;//损坏数量
	private int devTotal;//设备总数
	private int usingNum;//在用数
	private int freeNum;//空闲数
	private int scrapNum;//报废数
	
	public String getDevModel() {
		return devModel;
	}
	public void setDevModel(String devModel) {
		this.devModel = devModel;
	}
	public String getDevName() {
		return devName;
	}
	public void setDevName(String devName) {
		this.devName = devName;
	}
	public int getUseNum() {
		return useNum;
	}
	public void setUseNum(int useNum) {
		this.useNum = useNum;
	}
	public int getUserNum() {
		return userNum;
	}
	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}
	public int getUseDate() {
		return useDate;
	}
	public void setUseDate(int useDate) {
		this.useDate = useDate;
	}
	public int getBuyNum() {
		return buyNum;
	}
	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public int getDamageNum() {
		return damageNum;
	}
	public void setDamageNum(int damageNum) {
		this.damageNum = damageNum;
	}
	public int getDevTotal() {
		return devTotal;
	}
	public void setDevTotal(int devTotal) {
		this.devTotal = devTotal;
	}
	public int getUsingNum() {
		return usingNum;
	}
	public void setUsingNum(int usingNum) {
		this.usingNum = usingNum;
	}
	public int getFreeNum() {
		return freeNum;
	}
	public void setFreeNum(int freeNum) {
		this.freeNum = freeNum;
	}
	public int getScrapNum() {
		return scrapNum;
	}
	public void setScrapNum(int scrapNum) {
		this.scrapNum = scrapNum;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	
}
