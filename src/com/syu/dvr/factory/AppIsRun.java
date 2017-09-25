package com.syu.dvr.factory;

import java.util.Timer;
import java.util.TimerTask;

import com.syu.dvr.utils.Config;

public class AppIsRun extends RunTimeFactory   {
	private static AppIsRun appIsRun;
	private static MyTimerTask task;
	private static Timer timer;
	private int mCount=0;
	
	public AppIsRun() {
		
	}
	public static AppIsRun getInstance() {
		if (appIsRun==null) {
			synchronized (AppIsRun.class) {
				if (appIsRun==null) {
					appIsRun=new AppIsRun();
				}
			}
		}
		return appIsRun;
	}

	@Override
	public void RunTask() {
		timer=new Timer(AppIsRun.class.toString());
		if (task!=null) {
			timer.cancel();
		}
		task=new MyTimerTask();
		timer.schedule(task, 1, 1000);
	}

	@Override
	public void stopTimer() {
		if (task!=null) {
			task.cancel();
			task=null;
		}
		mCount=0;
		
	}
	class MyTimerTask extends TimerTask{

		@Override
		public void run() {
			mCount++;
			
			if (mCount==12) {
				Config.setShutUp(true);
				stopTimer();
			}
		}
		
	}
}
