package com.syu.dvr.factory;

import java.util.Timer;
import java.util.TimerTask;
//定时器定时发三张图片
public class AppCollisionPictrue extends RunTimeFactory {
	private int mCount;
	private static AppCollisionPictrue pictrue;
	private MyTimerTask task;
	private Timer timer;
	private String action;
	public String mAction3="android.com.syu.dvr.action.SHURTPHOTO";
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public static AppCollisionPictrue getinstance(){
		if (pictrue==null) {
			synchronized (AppCollisionPictrue.class) {
				if (pictrue==null) {
					pictrue=new AppCollisionPictrue();
				}
			}
		}
		return pictrue;
	}

	@Override
	public void RunTask() {
		if (task!=null) {
			task.cancel();
		}
		task=new MyTimerTask();
		timer=new Timer();
		timer.schedule(task, 10, 2*1000);
	}

	@Override
	public void stopTimer() {
		if (task!=null) {
			task.cancel();
			task=null;
		}
		mCount=0;
		
	}
	public class MyTimerTask extends TimerTask{
		@Override
		public void run() {
			
			/*if (CollisionVideoService.service==null) {
				return;
			}
			mCount++;
			CollisionVideoService.service.mWolShurtPhoto(mAction3,null);*/
			if (mCount==3) {
				stopTimer();
			}
		}	
	}
}
