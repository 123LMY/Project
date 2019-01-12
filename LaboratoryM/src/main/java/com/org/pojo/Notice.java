package com.org.pojo;

import java.sql.Date;

public class Notice {
	private long id;
	private String typeName;//标题
	private String description;//内容
	private long tUser_id_p;//发布人
	private Date publishDate;//发布时间
	private long tUser_id_o;//操作人
	private User publishers;
	private User operator;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long gettUser_id_p() {
		return tUser_id_p;
	}
	public void settUser_id_p(long tUser_id_p) {
		this.tUser_id_p = tUser_id_p;
	}
	public Date getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	public long gettUser_id_o() {
		return tUser_id_o;
	}
	public void settUser_id_o(long tUser_id_o) {
		this.tUser_id_o = tUser_id_o;
	}
	public User getPublishers() {
		return publishers;
	}
	public void setPublishers(User publishers) {
		this.publishers = publishers;
	}
	public User getOperator() {
		return operator;
	}
	public void setOperator(User operator) {
		this.operator = operator;
	}
	
	
}
