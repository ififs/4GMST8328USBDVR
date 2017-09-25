package com.syu.dvr.factory;

import com.syu.dvr.activity.MainActivity;

public class ActiviytIsOpen {
	private static ActiviytIsOpen appIsRun;
	private boolean isOpen=false;
	
	private boolean mClose=false;
	
	
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
		
	}
	public boolean getOpen(){
		return isOpen;
	}
	public void setmClose(boolean mClose) {
		this.mClose = mClose;
		if (mClose&&isOpen) {
			if (MainActivity.activity!=null) {
				MainActivity.activity.finish();
			}
		}
		this.mClose=false;
		this.isOpen=false;
	}
	public ActiviytIsOpen() {
		
	}
	public static ActiviytIsOpen getInstance() {
		if (appIsRun==null) {
			synchronized (ActiviytIsOpen.class) {
				if (appIsRun==null) {
					appIsRun=new ActiviytIsOpen();
				}
			}
		}
		return appIsRun;
	}

	
}
