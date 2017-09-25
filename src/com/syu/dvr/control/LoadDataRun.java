package com.syu.dvr.control;

import java.util.Timer;
import java.util.TimerTask;

import com.syu.dvr.utils.LogCatUtils;

public class LoadDataRun {
	public static myTimerTask tasks;
	public static Timer timers;
	private int count=0;
	
	public void RunTask(int camereId){
		timers=new Timer(String.valueOf(camereId));
		if (tasks==null) {
			tasks=new myTimerTask();
		}
		timers.schedule(tasks, 1, 1000);
	}
	public void stopTimer(int camereId){
		if (tasks!=null) {
			tasks.cancel();
			tasks=null;
		}
	}
	public class myTimerTask extends TimerTask{
		@Override
		public void run() {
			if (count<10) {
				count++;
				
			}
		}
		
	}

}
