package com.syu.dvr.module;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.syu.dvr.TheApp;
import com.syu.dvr.factory.AppIsRun;
import com.syu.dvr.factory.RunTimeFactory;
import com.syu.dvr.utils.Config;
import com.syu.dvr.utils.LogCatUtils;
import com.uvc.jni.RainUvc;

public class CameraPlayManeger extends RunTimeFactory{
	private String mCurrentName;
	private int mCurrentID;
	private List<String>mCurrentData;
	private int mCurrentType;
	private int mProgress;
	private int mTimeLeng;
	private int mCurrentProgress;
	private int mPlayState;
	public int getmPlayState() {
		return mPlayState;
	}
	public void setmPlayState(int mPlayState) {
		this.mPlayState = mPlayState;
	}
	public int getmTimeLeng() {
		return mTimeLeng;
	}
	
	public String getmProgressTime() {
		if (mProgress<=0) {
			return "00:00";
		}
		return returnTime(mProgress);
	}
	public void setmProgress(int mProgress) {
		this.mProgress =mProgress;
		this.mTimeLeng=mProgress;
	}
	public int getmProgress() {
		return mProgress;
	}
	public int getmCurrentProgress() {
		if (mCurrentProgress<=0) {
			return 0;
		}
		int value=(int) (100f/mProgress*mCurrentProgress);
		
		if (mCurrentProgress==mProgress) {
			setmPlayState(0);
		}
		return value;
	}
	public String getmCurrentTime(){
		if (mCurrentProgress<=0) {
			return "00:00";
		}
		return returnTime(mCurrentProgress);
	}
	private String returnTime(int data){
		int min=data/60;
		int second=data%60;
		return (min<10?"0"+String.valueOf(min):String.valueOf(min))+":"
				+(second<10?"0"+String.valueOf(second):String.valueOf(second));
	}
	public void setmCurrentProgress(int mCurrentProgress) {
		this.mCurrentProgress = mCurrentProgress;
		
	}
	public int getmCurrentType() {
		return mCurrentType;
	}
	public void setmCurrentType(int mCurrentType) {
		this.mCurrentType = mCurrentType;
	}
	public String getmCurrentName() {
		return mCurrentName;
	}
	public void setmCurrentName(String mCurrentName) {
		this.mCurrentName = mCurrentName;
		init();
	}
	public int getmCurrentID() {
		return mCurrentID;
	}
	public void setmCurrentID(int mCurrentID) {
		this.mCurrentID = mCurrentID;
	}
	public List<String> getmCurrentData() {
		return mCurrentData;
	}
	public void setmCurrentData(List<String> mCurrentData) {
		if (mCurrentData.isEmpty()) {
			return;
		}
		this.mCurrentData = mCurrentData;
		this.mCurrentID=mCurrentData.indexOf(mCurrentName);
	}
	public String getNestSong(){
		if (mCurrentData==null||mCurrentData.size()<=0) {
			return null;
		}
		mCurrentID=(mCurrentID+1>=mCurrentData.size()?0:mCurrentID+1);
		mCurrentName=mCurrentData.get(mCurrentID);
		return mCurrentName;
	}
	public String getLastSong(){
		if (mCurrentData==null||mCurrentData.size()<=0) {
			return null;
		}
		mCurrentID=(mCurrentID-1<0?mCurrentData.size()-1:mCurrentID-1);
		mCurrentName=mCurrentData.get(mCurrentID);
		
		return mCurrentName;
	}
	private void init(){
		
		if (mCurrentData.isEmpty()||mCurrentName.isEmpty()) {
			return;
		}
		mCurrentID=mCurrentData.indexOf(mCurrentName);
		LogCatUtils.showString("mcurrentID==="+mCurrentID);
	}
	private int mTempProgress;
	public int startPlay(int type,String name){
		RainUvc.FileInfo info = null;
		info=RainUvc.getFileInfoFromName(type, name);
		if (info!=null) {
			mTempProgress=info.file_length;
		}
		stopTimer();
		if (RainUvc.startPlayFile(type,name)>=0) {
			setmPlayState(1);
			RunTask();
		}
		return mTempProgress;
	}
	public void stopPlay(){
		if (RainUvc.stopPlayFile()>=0) {
			setmPlayState(0);
		}
	}
	private CameraPlayCallback callback;
	public void registerCallback(CameraPlayCallback callback){
		this.callback=callback;
	}
	public void unregisterCallback(){
		this.callback=null;
		stopPlay();
		stopTimer();
	}
	public interface CameraPlayCallback{
		void cameraPlayCallback();
	}
	
	private  MyTimerTask task;
	private  Timer timer;
	@Override
	public void RunTask() {
		timer=new Timer(AppIsRun.class.toString());
		if (task!=null) {
			timer.cancel();
		}
		task=new MyTimerTask();
		timer.schedule(task, 0, 500);
	}

	@Override
	public void stopTimer() {
		if (task!=null) {
			task.cancel();
			task=null;
		}
		mProgress=0;
		mTimeLeng=0;
		mCurrentProgress=0;
	}
	class MyTimerTask extends TimerTask{
		@Override
		public void run() {
				int mTemprogress;
				mTemprogress=RainUvc.getPlayingTime();
				LogCatUtils.showString("mTemprogress=="+mTemprogress);
				if (mTemprogress>=0) {
					setmCurrentProgress(mTemprogress);
				}
				if (callback!=null) {
					callback.cameraPlayCallback();
				}
				
		}}

}
