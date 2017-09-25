package com.syu.dvr.factory;

import java.util.Timer;
import java.util.TimerTask;

import com.syu.dvr.R;
import com.syu.dvr.TheApp;
import com.syu.dvr.activity.MainActivity;
import com.syu.dvr.utils.Config;
import com.syu.dvr.utils.LogCatUtils;
import com.syu.dvr.utils.PublicClass;
import com.syu.dvr.utils.RecordUi;
import com.syu.dvr.utils.RecordingStatus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class AppGetCameraSdCard extends RunTimeFactory{
	
	private static AppGetCameraSdCard getCameraSDcard;
	private  MyTimerTask task;
	private  Timer timer;
	private RecordUi callback;
	
	
	public static AppGetCameraSdCard getInstance(){
		if (getCameraSDcard==null) {
			synchronized (AppGetCameraSdCard.class) {
				if (getCameraSDcard==null) {
					getCameraSDcard=new AppGetCameraSdCard();
				}
			}
		}
		return getCameraSDcard;
	}
	public void setRecordCallback(RecordCallback myCallback){
		this.callback=(RecordUi) myCallback;
		
	}
	public void unRegister(){
		this.callback=null;
		
	}
	public interface RecordCallback{
		void Callback();
	}
	
	@Override
	public void RunTask() {
		timer=new Timer(AppGetCameraSdCard.class.toString());
		if (task!=null) {
			task.cancel();
			task=null;
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
		mToLauncher(0);
		sdcardLast=-1;
		RecordStatus=-1;
		mFileStatus=-1;
	}
	
	int sdcardLast=-1;
	int RecordStatus=-1;
	int mFileStatus=-1;
	private boolean mNeedCallback;
	private void mToLauncher(int i){
		Intent intent=new Intent("com.fyt.widget.DvrService");
		intent.setAction("com.fyt.launcher.dvr");
		intent.putExtra("DVR", i);
		TheApp.mApp.startService(intent);
	}
	class MyTimerTask extends TimerTask{
		@Override
		public void run() {
			mNeedCallback=false;
			if (TheApp.mCManager==null) {
				return;
			}
			if (TheApp.mApp==null) {
				return;
			}
			int[] value=TheApp.mCManager.setUvcExtenrnCall(TheApp.getDeviceId(),String.valueOf(7), 
					String.valueOf(83),"");
	    	if (value==null||value.length<16){
				return ;
			}
	    	for (int i = 0; i < value.length; i++) {
	    	
	    		if (i==0) {
					RecordingStatus.getInstance().setmRecordingStatus(value[i]);
					if (value[i]!=RecordStatus) {
						RecordStatus=value[i];
						mNeedCallback=true;
						TheApp.mApp.sendBroadCast(value[i]);
						mToLauncher(value[i]);
					}
				}
	    		if (i==1) {
	    			RecordingStatus.getInstance().setmFileStatus(value[i]);
					if (mFileStatus!=value[i]) {
						mFileStatus=value[i];
						if (mFileStatus==1) {
//							handler.sendEmptyMessage(4);
							PublicClass.getInstance().showStatuToast(4);
						}
						mNeedCallback=true;
					}
				}
	    		
	    		if (i==2) {
	    			RecordingStatus.getInstance().setmPhotoStatus(value[i]);
				}
	    		if (i==3) {
	    			RecordingStatus.getInstance().setmScardStatus(value[i]);
					if (sdcardLast!=value[i]) {
//						handler.sendEmptyMessage(value[i]);
						PublicClass.getInstance().showStatuToast(value[i]);
						sdcardLast=value[i];
						mNeedCallback=true;
					}
				}
	    		if (i==4) {
	    			RecordingStatus.getInstance().setmFileLock(value[i]);
				}
	    		if (mNeedCallback) {
	    			if (callback!=null) {
						callback.Callback();
					}
				}
			}
		}
	}
	public void settingCallback(){
		if (callback!=null) {
			callback.Callback();
		}
	}
	/*private Handler handler=new Handler(Looper.getMainLooper()){
		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
			case 0:
				TheApp.mApp.MyTosat(R.string.str_load_success);
				break;
			case 1:
				TheApp.mApp.MyTosat(R.string.str_tip_no_sdcard);
				TheApp.mApp.sendBroadCast(0);
				break;
			case 2:
				TheApp.mApp.MyTosat(R.string.str_low_speed_card);
				break;
			case 3:
				TheApp.mApp.MyTosat(R.string.str_card_error);
				break;
			case 4:
				TheApp.mApp.MyTosat(R.string.str_file_islock_video);
				break;
			default:
				break;
			}
		};
	};*/
}
