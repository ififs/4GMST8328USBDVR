package com.syu.dvr.factory;

import java.util.Timer;
import java.util.TimerTask;

import com.syu.dvr.activity.MainActivity;

public class PreviewOverTime extends RunTimeFactory{
	
	private static PreviewOverTime loadingOverTime;
	private static MyTimerTask task;
	private static Timer timer;
	private int mCount=0;
	private MainActivity activity;
	public static PreviewOverTime getInstance() {
		if (loadingOverTime==null) {
			synchronized (PreviewOverTime.class) {
				if (loadingOverTime==null) {
					loadingOverTime=new PreviewOverTime();
				}
			}
		}
		return loadingOverTime;
	}
	
	public void setRecordCallback(MainActivity activity){
		this.activity=activity;
	}
	public void remRecordCallback(){
		activity=null;
	}
	public interface LoadingCallback{
		void LoadCallback();
	}
	
	
	class MyTimerTask extends TimerTask{

		@Override
		public void run() {
			mCount++;
			if (mCount==20) {
				if (activity!=null&&activity.handler!=null) {
					activity.handler.sendEmptyMessage(2);
				}
				stopTimer();
			}
		}
	}

	@Override
	public void RunTask() {
		timer=new Timer(PreviewOverTime.class.toString());
		if (task!=null) {
			task.cancel();
		}
		task=new MyTimerTask();
		timer.schedule(task, 10, 1000);
	}

	@Override
	public void stopTimer() {
		if (task!=null) {
			task.cancel();
			task=null;
		}
		mCount=0;
		
	}

}
