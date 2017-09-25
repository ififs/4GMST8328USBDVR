package com.syu.dvr.factory;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import com.syu.dvr.TheApp;
import com.syu.dvr.factory.AppGetCameraSdCard.RecordCallback;

public class LoadingOverTime extends RunTimeFactory{
	
	private static LoadingOverTime loadingOverTime;
	private static MyTimerTask task;
	private static Timer timer;
	private int mCount=0;
	private WeakReference<LoadingCallback>callback;
	public LoadingOverTime() {
		
	}
	public static LoadingOverTime getInstance() {
		if (loadingOverTime==null) {
			synchronized (LoadingOverTime.class) {
				if (loadingOverTime==null) {
					loadingOverTime=new LoadingOverTime();
				}
			}
		}
		return loadingOverTime;
	}
	
	public void setRecordCallback(LoadingCallback myCallback){
		callback=new WeakReference<LoadingOverTime.LoadingCallback>(myCallback);
	}
	public void remRecordCallback(){
		callback=null;
	}
	public interface LoadingCallback{
		void LoadCallback();
	}
	
	
	class MyTimerTask extends TimerTask{

		@Override
		public void run() {
			
			mCount++;
			if (mCount==20) {
				if (callback!=null) {
					LoadingCallback myCallback=callback.get();
					if (myCallback!=null) {
						myCallback.LoadCallback();
					}
				}
				stopTimer();
			}
			
		}
		
	}

	@Override
	public void RunTask() {
		timer=new Timer(LoadingOverTime.class.toString());
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
