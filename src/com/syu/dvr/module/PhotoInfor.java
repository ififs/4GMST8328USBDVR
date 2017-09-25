package com.syu.dvr.module;

import android.graphics.Bitmap;

public class PhotoInfor {
	private String name;
	private String creatTime;
	private String seleTime;
	private String path;
	private Bitmap bitmap;
	
	
	
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	

}
