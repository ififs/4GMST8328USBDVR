package com.syu.dvr.observ;

import java.util.ArrayList;
import java.util.List;

import com.syu.dvr.utils.LogCatUtils;

import android.hardware.Camera;
import android.os.SystemProperties;

public class WatchedUsb extends Watched {
	private static WatchedUsb watchedUsb;
	private static List<Watcher>watcheds=new ArrayList<Watcher>();
	private int data=-1;
	
	public static WatchedUsb getInstance(){
		if (watchedUsb==null) {
			synchronized (WatchedUsb.class) {
				if (watchedUsb==null) {
					watchedUsb=new WatchedUsb();
				}
			}
		}
		return watchedUsb;
	}
	@Override
	public void addWatched(Watcher watched) {
		
		if ((watched!=null)&&(!watcheds.contains(watched))) {
			watcheds.add(watched);
			LogCatUtils.showString("  isusbcamera   "+isUsbCamera());
			if (isUsbCamera()) {
				watched.updata(1);
			}
		}
	}
	@Override
	public void removeWatched(Watcher watched) {
		if (watcheds.contains(watched)) {
			watcheds.remove(watched);
		}
	}
	@Override
	public void notifyWatchers(int uddata) {
		
			for (Watcher watcher:watcheds) {
				
				if (watcher!=null) {
					
					watcher.updata(uddata);
				}
			}
	}
	public void setData(int data){
		if (this.data!=data) {
			this.data=data;
			notifyWatchers(data);
		}
		
	}
	public interface Watcher{
		void updata(int updata);
	}

	@SuppressWarnings("deprecation")
	public boolean isUsbCamera(){
		return SystemProperties.get("sys.usbcamera.status", "def").equals("insert");
	}
	
}
