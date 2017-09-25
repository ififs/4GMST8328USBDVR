package com.syu.dvr.factory;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.syu.dvr.TheApp;
import com.syu.dvr.activity.MainActivity;
import com.syu.dvr.control.FileManager;
import com.syu.dvr.observ.WatchedUsb;
import com.syu.dvr.utils.Config;
import com.syu.dvr.utils.LogCatUtils;
import com.syu.dvr.utils.PublicClass;
import com.syu.dvr.utils.RecordUi;
import com.syu.dvr.utils.RecordingStatus;
import com.uvc.jni.RainUvc;

import android.R.integer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.text.GetChars;

public class UpSystemTimeForCamera extends RunTimeFactory{
	private int mCount=1;
	private  MyTimerTask task;
	private  Timer timer;
	private FileManager mFileManager;
	private static UpSystemTimeForCamera upSystemTimeForCamera;
	private PublicClass publicClass=new PublicClass();
	private RecordUi recordUi;
	private boolean isRecording=false;
	private boolean isencrypton=false;
	private RecordingStatus status;
	private UpSystemTimeForCamera() {
		mFileManager=new FileManager();
		recordUi=new RecordUi();
	}
	public static UpSystemTimeForCamera getInstance(){
		if (upSystemTimeForCamera==null) {
			synchronized (UpSystemTimeForCamera.class) {
				if (upSystemTimeForCamera==null) {
					upSystemTimeForCamera=new UpSystemTimeForCamera();
				}
			}
		}
		
		return upSystemTimeForCamera;
	}

	@Override
	public void RunTask() {
		timer=new Timer(UpSystemTimeForCamera.class.toString());
		if (task!=null) {
			task.cancel();
		}
		task=new MyTimerTask();
		timer.schedule(task, 100, 1000);
		status=RecordingStatus.getInstance();
	}

	@Override
	public void stopTimer() {
		if (task!=null) {
			task.cancel();
			task=null;
		}
		mCount=0;
		handle.removeMessages(1);
	}
	private int[]setTime;
	private int mShakeHandcount;
	class MyTimerTask extends TimerTask{

		@Override
		public void run() {
			
			if (TheApp.mIsShengMaiIC) {
				
				RainUvc.TimeInfo info=timeInfo();
				if (info==null) {
					return;
				}
				if (RainUvc.setTime(info)>=0) {
					stopTimer();
					mShakeHandcount=0;
					handle.sendEmptyMessage(4);
					if (status.getmRecordingStatus()==1) {
						return;
					}else {
						checkAndRecord();
					}
				}else {
					return;
				}
			}
			mCount++;
			if (mCount%2==0) {
				int [] value=TheApp.mCManager.setUvcExtenrnCall(TheApp.getDeviceId(), 
						String.valueOf(60), String.valueOf(82),"");
				if (value==null||value.length==0) {
					return;
				}
				String times="";
				int flag=-1;
				
				for (int i = 0; i < value.length; i++) {
					times+=String.valueOf(value[i]);
					if (i==3) {
						flag=value[3] & 0x40;
					}
				}
				if (times.isEmpty()||flag==-1) {
					
					return;
				}
				String[]mTimes=mFileManager.getSystemTimeToCamera(TheApp.getDeviceId());
				if (mTimes==null||mTimes.length<=0) {
					return;
				}
				setTime=new int[mTimes.length];
				for (int i = 0; i < mTimes.length; i++) {
					setTime[i]=Integer.valueOf(mTimes[i]);
				}
				if (setTime==null||setTime.length==0) {
					return;
				}
				
				if (status==null) {
					return ;
				}
				if (setTimeisFish(value, setTime)) {
					stopTimer();
					checkAndRecord();
					return;
					
				}else {
					if (status.getmRecordingStatus()==1) {
						isRecording=true;
						TheApp.mCManager.stopRecord(TheApp.getDeviceId());
					}
					handle.sendEmptyMessageDelayed(0, 500);
				}
			}
		}
	}
	private void shakeHand(){
		RainUvc.TimeInfo info=timeInfo();
		if (RainUvc.shakeHand(info)!=1) {
			if (mShakeHandcount<10) {
				mShakeHandcount++;
				handle.sendEmptyMessageDelayed(4, 2*1000);
			}else {
				mShakeHandcount=0;
				int count=TheApp.mApp.getInt("com.syu.dvr.i2c.smerror",0);
				LogCatUtils.showString("===count==="+count);
				if (count<2) {
					TheApp.mApp.saveInt("com.syu.dvr.i2c.smerror", ++count);
					return;
				}
				WatchedUsb.getInstance().setData(4);
			}
		}else {
			TheApp.mApp.saveInt("com.syu.dvr.i2c.smerror", 0);
		}
	}
	
	private RainUvc.TimeInfo timeInfo(){
		String[]times = mFileManager.getSystemTimeSetCamera();
		RainUvc.TimeInfo info=new RainUvc.TimeInfo();
		if (times==null||times.toString().isEmpty()) {
			return null;
		}
		info.year=new Short(times[0]);
		info.month=new Short(times[1]);
		info.day=new Short(times[2]);
		info.hour=new Short(times[3]);
		info.minute=new Short(times[4]);
		info.second=new Short(times[5]);
		return info;
	}
	private void checkAndRecord(){
		if (publicClass.recordStatuToast(status)) {
			if (handle!=null) {
				handle.sendEmptyMessageDelayed(1, 1000);
			}
			
		}
	}
	private Handler handle=new Handler(Looper.getMainLooper()){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (TheApp.mCManager!=null) {
					
					TheApp.mCManager.upDataTime(TheApp.getDeviceId(), 
							TheApp.getDeviceId(),setTime);
				}
				
				break;
				
			case 1:
				if (TheApp.mCManager!=null&&!TheApp.mApp.isPlayBack) {
					LogCatUtils.showString("=====startRecord====");
					TheApp.mCManager.startRecord(TheApp.getDeviceId());
				}
				break;
			case 2:
				if (TheApp.mCManager!=null) {
					
					TheApp.mCManager.setUvcExtenrnCall(TheApp.getDeviceId()
							, String.valueOf(63), String.valueOf(81),"");
					String value=SystemProperties.get("sys.usbcamera.sql");
					
					if (value.isEmpty()) {
						return;
					}
					if (value.contains("mcl")) {
						TheApp.mApp.saveInt("com.syu.dvr.i2c.error",0);
						return;
					}
					isencrypton=false;
					if (mencryptonCount<3) {
						encrypton();
					}else {
						int count=TheApp.mApp.getInt("com.syu.dvr.i2c.error", 0);
						LogCatUtils.showString("===count==="+count);
						if (count<2) {
							TheApp.mApp.saveInt("com.syu.dvr.i2c.error", ++count);
							return;
						}
						if (value.contains("iic")) {
							TheApp.mApp.setmI2Cerror(true);
							WatchedUsb.getInstance().setData(2);
							return;
						}
						if (value.contains("lyk")) {
							TheApp.mApp.setCheckfailure(true);
							WatchedUsb.getInstance().setData(2);
							return;
						}
					}
				}
				break;
				
			case 4:
				shakeHand();
				break;
			default:
				break;
			}
		};
	};
	private boolean setTimeisFish(int[] values1,int []values2){
		if (values1.length<6||values2.length<6) {
			return false;
		}
		if (values1[0]<values2[0]) {
			return false;
		}
		if (values1[1]<values2[1]) {
			return false;
		}
		if (values1[2]<values2[2]) {
			return false;
		}
		int a=values1[3]*60*values1[4]*60+values1[5];
		int b=values2[3]*60*values2[4]*60+values2[5];
		
		if (Math.abs(a-b)>120) {
			return false;
		}
		mencryptonCount=0;
		if (!isencrypton) {
			
			encrypton();
		}
		return true;
	}
	int mencryptonCount=0;
	private void encrypton(){
		isencrypton=true;
		String isencypton=SystemProperties.get("sys.camera.sql","");
		if (isencypton.isEmpty()||!isencypton.contains("ture")) {
			return;
		}
		int [] version=TheApp.mCManager.setUvcExtenrnCall(TheApp.getDeviceId(),
    			String.valueOf(9), String.valueOf(81),"");
		if (version==null||version.length!=16) {
			return ;
		}
		String mTime="";
		for (int i = 0; i < 6; i++) {
			mTime+=String.valueOf(version[i]);
		}
		if (mTime.isEmpty()) {
			return;
		}
		if (Integer.valueOf(mTime)<161201) {
			return;
		}
		Random random=new Random();
		int []temp=new int[16];
		temp[0]=Integer.valueOf("63", 16);
		temp[1]=1;
		for (int i = 0; i < 4; i++) {
			temp[i+2]=random.nextInt(254);
		}
		mencryptonCount++;
		TheApp.mCManager.setCameraSetting(TheApp.getDeviceId(), temp);
		handle.sendEmptyMessageDelayed(2, 10*1000);
		
	}
}
