package com.syu.dvr.factory;

import java.util.Timer;
import java.util.TimerTask;

import com.syu.dvr.utils.HandleConstant;
import com.syu.dvr.utils.RecordUi;

public class HideLayout {
	
	private static HideLayout hideLayout;
	
	public static HideLayout getInstance(){
		if (hideLayout==null) {
			synchronized (HideLayout.class) {
				if (hideLayout==null) {
					hideLayout=new HideLayout();
				}
			}
		}
		return hideLayout;
	}
	
	private int mCount=0;
	private  MyTimerTask task;
	private  Timer timer;
	public void RunTask() {
		if (task!=null) {
			task.cancel();
			task=null;
			mCount=0;
		}
		timer=new Timer(RecordUi.class.getName());
		task=new MyTimerTask();
		timer.schedule(task, 1, 1000);
	}

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
			
			if (mCount==10&&RecordUi.mHandler!=null) {
				RecordUi.mHandler.sendEmptyMessageDelayed(HandleConstant.HIDE_MENU,0);
				stopTimer();
			}
		}
	}

}
