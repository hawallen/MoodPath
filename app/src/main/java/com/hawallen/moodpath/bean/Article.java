package com.hawallen.moodpath.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Article implements Serializable {

	private String aId = "";
	private String calendar = "";
	private String title = "";
	private String content = "";
	private String picPath = "";
	private int value = -1;
//	private boolean isRead = false;
	
	public String getaId() {
		return aId;
	}
	public void setaId(String aId) {
		this.aId = aId;
	}
	public String getCalendar() {
		return calendar;
	}
	public void setCalendar(String calendar) {
		this.calendar = calendar;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
//	public boolean isRead() {
//		return isRead;
//	}
//	public void setRead(boolean isRead) {
//		this.isRead = isRead;
//	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
}
