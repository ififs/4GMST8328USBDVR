package com.syu.dvr.module;

import android.graphics.Bitmap;

public class VideoInfo {
	
	
	private String name;//文件名
	private String size;//文件大小
	private String creatTime;//创建时间 时分秒
	private String seleTime;//筛选时间年月日时
	private Bitmap bitmap;//缩略图位图
	private String path;//绝对路径
	private String thuName;//缩略图名字
	private String type;
	
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getThuName() {
		return thuName;
	}
	public void setThuName(String thuName) {
		this.thuName = thuName;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}
	public String getSeleTime() {
		return seleTime;
	}
	public void setSeleTime(String seleTime) {
		this.seleTime = seleTime;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	
	
}
