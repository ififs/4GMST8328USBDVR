package com.syu.dvr.module;

public class DeleFileInfor {
	private String path;
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
	public DeleFileInfor(){
		
	}
	public DeleFileInfor(String  y_m_d,String  h_m_s,String path){
		this.y_m_d=y_m_d;
		this.h_m_s=h_m_s;
		this.path=path;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

}
