package com.syu.dvr.factory;

import java.util.Timer;
import java.util.TimerTask;

import com.syu.dvr.utils.RecordUi;

public class AppIsRecordShow extends RunTimeFactory{
	private static AppIsRecordShow appRecordShow;
	private static MyTimerTask task;
	private static Timer timer;
	private int mCount=0;
	
	public AppIsRecordShow() {
		
	}
	public static AppIsRecordShow getInstance() {
		if (appRecordShow==null) {
			synchronized (AppIsRecordShow.class) {
				if (appRecordShow==null) {
					appRecordShow=new AppIsRecordShow();
				}
			}
		}
		return appRecordShow;
	}
	@Override
	public void RunTask() {
		timer=new Timer(AppIsRecordShow.class.toString());
		if (task!=null) {
			task.cancel();
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
		if (timer!=null) {
			timer.cancel();
		}
		
	}
	class MyTimerTask extends TimerTask{
		@Override
		public void run() {
			
			if (RecordUi.mHandler!=null) {
				
				RecordUi.mHandler.sendEmptyMessage(3);//闪烁
			}
			
		}
		
	}
}
