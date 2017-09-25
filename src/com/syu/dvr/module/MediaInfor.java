package com.syu.dvr.module;

import java.io.Serializable;

import android.graphics.Bitmap;

public class MediaInfor implements  Serializable,Cloneable{
	
	@Override
	public MediaInfor clone() throws CloneNotSupportedException {
		MediaInfor mediaInfor=null;
		try {
			mediaInfor=(MediaInfor) super.clone();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return mediaInfor;
	}
	
	/**
	 * 鍥剧墖鍜岃棰�
	 * */
	
	private static final long serialVersionUID = 1L;
	
	private String name;//鏂囦欢鍚�
	private String size;//鏂囦欢澶у皬
	private String creatTime;//鍒涘缓鏃堕棿 鏃跺垎绉�
	private String seleTime;//绛涢�夋椂闂村勾鏈堟棩鏃�
	private Bitmap bitmap;//缂╃暐鍥句綅鍥�
	private String path;//缁濆璺緞
	private boolean isLock;
	
	public boolean isLock() {
		return isLock;
	}
	public void setLock(boolean isLock) {
		this.isLock = isLock;
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
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	/*
	 * sort
	 * **/
	private String  y_m_d;
	private String  h_m_s;
	
	
	public String getY_m_d() {
		return y_m_d;
	}
	public void setY_m_d(String y_m_d) {
		this.y_m_d = y_m_d;
	}
	public String getH_m_s() {
		return h_m_s;
	}
	public void setH_m_s(String h_m_s) {
		this.h_m_s = h_m_s;
	}
	public MediaInfor(){
		
	}
	
}
