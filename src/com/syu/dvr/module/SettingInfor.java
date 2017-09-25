package com.syu.dvr.module;

public class SettingInfor {
	private String mKey;
    private int mDrawableRes;
    private int mLabelRes;
    private String mValue;
    private boolean mChecked;
	public String getKey() {
		return mKey;
	}
	public void setKey(String mKey) {
		this.mKey = mKey;
	}
	public int getDrawableRes() {
		return mDrawableRes;
	}
	public void setDrawableRes(int mDrawableRes) {
		this.mDrawableRes = mDrawableRes;
	}
	public int getLabelRes() {
		return mLabelRes;
	}
	public void setLabelRes(int mLabelRes) {
		this.mLabelRes = mLabelRes;
	}
	public String getValue() {
		return mValue;
	}
	public void setValue(String mValue) {
		this.mValue = mValue;
	}
	public boolean isChecked() {
		return mChecked;
	}
	public void setChecked(boolean mChecked) {
		this.mChecked = mChecked;
	}
	

}
