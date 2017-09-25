package com.syu.dvr.factory;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.syu.dvr.utils.LogCatUtils;
import com.syu.dvr.utils.RecordingStatus;
import com.uvc.jni.RainUvc;

public class PlayBackTFcardStatu extends RunTimeFactory{
	private static PlayBackTFcardStatu getCameraSDcard;
	private  MyTimerTask task;
	private  Timer timer;
	private TFcardStatuCallback callback;
	public static PlayBackTFcardStatu getInstance(){
		if (getCameraSDcard==null) {
			synchronized (PlayBackTFcardStatu.class) {
				if (getCameraSDcard==null) {
					getCameraSDcard=new PlayBackTFcardStatu();
				}
			}
		}
		return getCameraSDcard;
	}
	public void setTFcardStatuCallback(TFcardStatuCallback callback){
		this.callback=callback;
		
	}
	public void unRegister(){
		this.callback=null;
	}
	public interface TFcardStatuCallback{
		void Callback(int staut);
	}

	@Override
	public void RunTask() {
		timer=new Timer(PlayBackTFcardStatu.class.toString());
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
		sdcardLast=-1;
		RecordStatus=-1;
		mFileStatus=-1;
	}
	
	int sdcardLast=-1;
	int RecordStatus=-1;
	int mFileStatus=-1;

	RecordingStatus status;
	class MyTimerTask extends TimerTask{

		@Override
		public void run() {
			
	    	//»ñÈ¡TF¿¨×´Ì¬
			int mTFStatus=RainUvc.getTFCardStatus();
			if (mTFStatus>=0) {
				if (sdcardLast!=mTFStatus) {
					sdcardLast=mTFStatus;
					handler.sendEmptyMessage(mTFStatus);
				}
			}
		}
	}
	private Handler handler=new Handler(Looper.getMainLooper()){
		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
			case 0:
				if (callback!=null) {
					callback.Callback(0);
				}
				break;
			/*case 1:
				if (callback!=null) {
					callback.Callback(1);
				}
				break;*/
			default:
				break;
			}
		};
	};
}
