package com.syu.dvr.factory;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.syu.dvr.R;
import com.syu.dvr.TheApp;
import com.syu.dvr.activity.MainActivity;
import com.syu.dvr.utils.LogCatUtils;
import com.syu.dvr.utils.PublicClass;
import com.syu.dvr.utils.RecordingStatus;
import com.uvc.jni.RainUvc;

public class App4GGetRecordStatus extends RunTimeFactory{
	
	private static App4GGetRecordStatus getCameraSDcard;
	private  MyTimerTask task;
	private  Timer timer;
	private  int mSynchronizationTime=0;
	private RecordCallback4G callback;
	
	public static App4GGetRecordStatus getInstance(){
		if (getCameraSDcard==null) {
			synchronized (App4GGetRecordStatus.class) {
				if (getCameraSDcard==null) {
					getCameraSDcard=new App4GGetRecordStatus();
				}
			}
		}
		return getCameraSDcard;
	}
	public void setRecordCallback(RecordCallback4G myCallback){
		this.callback=myCallback;
		
	}
	public void unRegister(RecordCallback4G myCallback4g){
		callback=null;
	}
	public interface RecordCallback4G{
		void Callback();
	}
	@Override
	public void RunTask() {
		timer=new Timer(App4GGetRecordStatus.class.toString());
		if (task!=null) {
			task.cancel();
			task=null;
		}
		task=new MyTimerTask();
		timer.schedule(task, 1000, 1000);
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
		mSynchronizationTime=0;
	}
	
	int sdcardLast=-1;
	int RecordStatus=-1;
	int mFileStatus=-1;
	int mAudioLast=-1;
	int mResolutionLast=-1;
	int mTFStatus;
	int value;
	int mGSensorStatus;
	int audio;
	int mResolution;
	int mSdcard;
	int mError;
	int misHasMic;
	
	
	public void setRecordStatus() {
		/*LogCatUtils.showString("=====RecordStatus==="+RecordStatus);
		TheApp.mApp.sendBroadCast(RecordStatus);*/
		mToLauncher(RecordStatus);
	}
	private void mToLauncher(int i){
		Intent intent=new Intent("com.fyt.widget.DvrService");
		intent.setAction("com.fyt.launcher.dvr");
		intent.putExtra("DVR", i);
		TheApp.mApp.startService(intent);
	}
	private RecordingStatus status;
	private boolean mNeedCallback;
	class MyTimerTask extends TimerTask{
		@Override
		public void run() {
			/*if ((mSynchronizationTime++)>=300) {
				mSynchronizationTime=0;
				UpSystemTimeForCamera.getInstance().RunTask();
			}*/
			mNeedCallback=false;
			status=RecordingStatus.getInstance();
			//获取录制状态
			value=RainUvc.getVideoRecordStatus();
	    	if (value>=0){
				status.setmRecordingStatus(value);
				if (value!=RecordStatus) {
					LogCatUtils.showString("===RecordStatus=="+RecordStatus);
					TheApp.mApp.sendBroadCast(value);
					RecordStatus=value;
					if (callback!=null) {
						mNeedCallback=true;
					}
					mToLauncher(value);
				}
	    	}
	    	//获取TF卡状态
			mTFStatus=RainUvc.getTFCardStatus();
			if (mTFStatus>=0) {
				status.setmScardStatus(mTFStatus>1?2:Math.abs(mTFStatus-1));
				if (sdcardLast!=mTFStatus) {
					mNeedCallback=true;
					if (sdcardLast!=-1) {
//						handler.sendEmptyMessage(mTFStatus);
						PublicClass.getInstance().showStatuToast((mTFStatus==1)?0:1);
					}
					sdcardLast=mTFStatus;
				}
			}
			mGSensorStatus=RainUvc.getGSensorStatus();
			if (mGSensorStatus>=0) {
				status.setmFileStatus(mGSensorStatus);
				if (mFileStatus!=mGSensorStatus) {
					mFileStatus=mGSensorStatus;
					if (mGSensorStatus==1) {
//						handler.sendEmptyMessage(4);
						PublicClass.getInstance().showStatuToast(4);
					}
					mNeedCallback=true;
				}
			}
			//音频录制
			audio=RainUvc.getAudioRecordStatus();
			if (audio>=0) {
				status.setMisRecordAudio(audio==1?true:false);
				if (audio!=mAudioLast) {
					mAudioLast=audio;
					mNeedCallback=true;
				}
			}
			mResolution=RainUvc.getResolution();
			if (mResolution>=0) {	
				status.setmResolution(mResolution);
				if (mResolution!=mResolutionLast) {
					mResolutionLast=mResolution;
					mNeedCallback=true;
				}
			}
			
			mSdcard=RainUvc.isSdSpaceFull();
			status.setSdSpaceFull(mSdcard>0?true:false);
			
			mError=RainUvc.isSdWriteError();
			status.setIsSdWriteError(mError);
			misHasMic=RainUvc.doHasMic();
			status.setHasMic(misHasMic>0?true:false);
			
			if (mNeedCallback&&callback!=null) {
				callback.Callback();
			}
		}
	}
	/*private Handler handler=new Handler(Looper.getMainLooper()){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				TheApp.mApp.MyTosat(R.string.str_load_success);
				break;
			case 0:
				TheApp.mApp.MyTosat(R.string.str_tip_no_sdcard);
				TheApp.mApp.sendBroadCast(0);
				break;
			case 2:
				TheApp.mApp.MyTosat(R.string.str_low_speed_card);
				break;
			case 3:
				TheApp.mApp.MyTosat(R.string.str_load_sysfile);
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
