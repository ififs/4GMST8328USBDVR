package com.syu.dvr.server;

import android.R.integer;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemProperties;

import com.syu.dvr.R;
import com.syu.dvr.TheApp;
import com.syu.dvr.activity.MainActivity;
import com.syu.dvr.factory.App4GGetRecordStatus;
import com.syu.dvr.utils.Config;
import com.syu.dvr.utils.LogCatUtils;
import com.syu.dvr.utils.PublicClass;
import com.syu.dvr.utils.RecordingStatus;
import com.syu.dvr.widget.ComInterface.CapturePhotos;
import com.syu.jni.SyuJniNative;
import com.syu.module.IUiNotify;
import com.syu.module.gsensor.DataGsensor;
import com.syu.module.gsensor.FinalGsensor;
import com.uvc.jni.RainUvc;
public class CollisionVideoService extends Service{
	public static String REMOTE_PICTURE ="android.com.syu.dvr.action.SHURTPHOTO";//远程唤醒拍照
	public static String GSENSOR_NSTIFY="android.com.syu.dvr.action.gesensor";
	public String LOCKE="android.com.syu.dvr.action.LOCKE";//加锁
	public String RECORD="android.com.syu.dvr.action.RECORD";//录制
	public String PHOTO="android.com.syu.dvr.action.PHOTO";//拍照
	private String PULL_MYCAR="com.syu.car.LocationService";//拉起爱车服务
	private String ACTION="android.com.syu.dvr.UP_NOTIFICATION";
	private String action;
	private Intent myCarService;
	private int mCountS=0;
	private String msgId;
	private String type="";
	private String mPhoto="photo";
	private String mGsensor="gsensor";
	private String mStopdvr="stopdvr";
	private boolean mCollisionHandle=false;
	@Override
	public IBinder onBind(Intent arg0) {
		if (arg0==null) {
			return null;
		}
		final String action=arg0.getAction();
		if (action.contains(ACTION)) {
			handler.sendEmptyMessageDelayed(3, 100);
			handler.sendEmptyMessageDelayed(5, 5000);
		}
		return null;
	}
	private int mSendBroadCount;
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if (!checkState(false)) {
					return;
				}
				doPicture(false);
				break;
			case 1:
				if (!checkState(true)) {
					return;
				}
				doPicture(true);
				break;
			case 2:
				if (TheApp.mApp.windowManageView!=null) {
					LogCatUtils.showString("=====removeView");
					TheApp.mApp.windowManageView.removeView();
				}
				break;
			case 3:
				isUsbCamera();
				break;
			case 4:
				if (MainActivity.activity==null||MainActivity.activity.isonStop) {
					TheApp.mApp.sendBroadCast(1);
				}else {
					removeMessages(5);
				}
				break;
			case 5:
				if (TheApp.mIsShengMaiIC) {
					App4GGetRecordStatus.getInstance().setRecordStatus();
				}
				handler.sendEmptyMessage(4);
				if ((mSendBroadCount++)<3) {
					sendEmptyMessageDelayed(5, 5000);
				}
				break;
			default:
				break;
			}
		};
	};
	private void isUsbCamera(){
		if(SystemProperties.get("sys.usbcamera.status", "def").equals("insert")){
			TheApp.mCManager.doOpenCamera(TheApp.getDeviceId());
			handler.sendEmptyMessageDelayed(4, 1000);
			return;
		}else {
			handler.sendEmptyMessageDelayed(3, 1000);
		}
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LogCatUtils.showString("====onStartCommand====");
		switchdvrmode();
		if (intent!=null) {
			if (intent.getAction()!=null) {
				this.action=intent.getAction();
				LogCatUtils.showString("action=="+action);
				actionHandler(action,intent);
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}
	private void switchdvrmode(){
		LogCatUtils.showString("  摄像头模式切换      ");
		if (TheApp.mIsShengMaiIC) {
			RainUvc.setMode(1);
		}else {
			SyuJniNative.getInstance().syu_jni_command(152, null, null);
		}
	}
	private void actionHandler(final String action,final Intent intent) {
		if (action.indexOf(REMOTE_PICTURE)!=-1) {
			mWolShurtPhoto(intent);
			if (intent!=null&&intent.hasExtra("type")&&
					intent.getStringExtra("type").contains(mGsensor)) {
				LogCatUtils.showString("  碰撞唤醒 ");
				TheApp.mCManager.startRecord(TheApp.getDeviceId());
				TheApp.mCManager.recordLock();
			}
		}else if (action.indexOf(GSENSOR_NSTIFY)!=-1) {
			addNotify();
		}else {
			RecordingStatus status=RecordingStatus.getInstance();
			PublicClass publicClass=new PublicClass();
			if (!(action.contains(LOCKE)||action.contains(RECORD)||action.contains(PHOTO))) {
				return;
			}
			if (!SystemProperties.get("sys.usbcamera.status", "def").equals("insert")) {
				if (mCountS==6) {
					TheApp.mApp.MyTosat(R.string.no_camera);
					mCountS=0;
					return;
				}
				handler.postDelayed(new Runnable() {
					public void run() {
						actionHandler(action, intent);
					}
				}, 1000);
				
				mCountS++;
				return ;
			}
			mCountS=0;
			if (TheApp.mCManager!=null&&status!=null&&publicClass!=null) {
				if (action.indexOf(RECORD)!=-1) {
					if (status.getmRecordingStatus()==0) {
						LogCatUtils.showString("=====startRecord====");
						TheApp.mCManager.startRecord(TheApp.getDeviceId());
					}else if (status.getmRecordingStatus()==1) {
						TheApp.mCManager.stopRecord(TheApp.getDeviceId());
					}
				}else if (action.indexOf(PHOTO)!=-1) {
					if (status.getmPhotoStatus()==1) {
						TheApp.mApp.MyTosat(R.string.str_text_lock);
						return ;
					}
					if (publicClass.recordStatuToast(status)) {
						TheApp.mCManager.setUvcExtenrnCall(TheApp.getDeviceId(), 
								String.valueOf(4), String.valueOf(1), String.valueOf(1));
						TheApp.mApp.MyTosat(R.string.str_text_photo);
					}
				}else if (action.indexOf(LOCKE)!=-1) {
					if (status.getmRecordingStatus()!=1) {
						TheApp.mApp.MyTosat(R.string.str_dvr_lock);
						return;
					}
					if (status.getmFileLock()==1) {
						TheApp.mApp.MyTosat(R.string.str_text_flock);
						return;
					}
					TheApp.mCManager.recordLock();
					TheApp.mApp.MyTosat(R.string.str_file_islock_video);
				}
			}
			
		}
	}
	private  void mWolShurtPhoto(Intent intent){
		if (intent!=null) {
			msgId=intent.getStringExtra("msgId");
			if (msgId!=null) {
				Config.setmServiceAction(action);
				Config.setMsgId(intent.getStringExtra("msgId"));
				type=intent.getStringExtra("type");
				LogCatUtils.showString(" 窝的爱车 type  "+type);
				Config.setType(type);
			}
		}
		if (TheApp.mCManager==null) {
			return;
		}
		sendHandler(0);
	}
	private void sendHandler(int time){
		if ((!type.isEmpty())&&((Config.getType().contains(mPhoto))||
				(Config.getType().contains(mGsensor)))) {
			handler.removeMessages(0);
			handler.sendEmptyMessageDelayed(0, time);
		}else if ((!type.isEmpty())&&(Config.getType().contains(mStopdvr))) {
			if (TheApp.mCManager!=null) {
				TheApp.mCManager.stopRecord(TheApp.getDeviceId());
			}
		}else{
			handler.removeMessages(1);
			handler.sendEmptyMessageDelayed(1, time);
		}
	}
	public void doPicture(final boolean mCollision){
		LogCatUtils.showString("doPicture==");
		TheApp.mCManager.MyCarCollitionPhoto(TheApp.getDeviceId(), new CapturePhotos() {
			@Override
			public void capturePhoto(int flag,String path) {
				doPictureHandle(flag, path,mCollision);
			}
		});
	}
	public void doPictureHandle(int flag,String path,boolean mCollision){
		LogCatUtils.showString("path=="+path+"===flag=="+flag);
		if (flag==1&&path!=null) {
			myCarService.putExtra("type", Config.getType()); 
		}else if (flag==-1) {
			myCarService.putExtra("type", "NoDevice");
		}
		if (!Config.getType().equals(mStopdvr)) {
			sendCallblck(path,mCollision);
		}
	}
	private void sendCallblck(String path, boolean mCollision){
		myCarService.setAction(PULL_MYCAR);
		if (Config.getMsgId()!=null) {
			myCarService.putExtra("msgId", Config.getMsgId());
		}else {
			myCarService.putExtra("msgId", "");
		}
		myCarService.putExtra("path", path);
		TheApp.mApp.startService(myCarService);
		Config.setMsgId(null);
		Config.setmServiceAction(null);
		if (mCollision&&((--mPictrue)==0)) {
			LogCatUtils.showString("===startservice==结束=="+mPictrue);
			LogCatUtils.showString("  发生碰撞拍照拉起爱车    ");
			handler.sendEmptyMessageDelayed(2, 200);
			mCollisionHandle=false;
		}else if (!mCollision) {
			LogCatUtils.showString("===startservice==结束==");
			handler.sendEmptyMessageDelayed(2, 200);
		}else if (mCollision&&mPictrue!=0) {
			handler.sendEmptyMessageDelayed(1, 2000);
		}
	}
	@Override
	public void onCreate() {
		super.onCreate();
		IntentFilter mFilter=new IntentFilter();
		mFilter.addAction(Intent.ACTION_DATE_CHANGED);
		mFilter.addAction(Intent.ACTION_TIME_CHANGED);
		mFilter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);
		mFilter.addAction(Intent.ACTION_TIME_CHANGED);
		mFilter.addAction(BootReceiver.ACTION_UPGRADE);
		registerReceiver(new BootReceiver(), mFilter);
		myCarService=new Intent();
		myCarService.setComponent(new ComponentName("com.syu.car", "com.syu.car.LocationService"));
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		handler.sendEmptyMessage(2);
		removeNotify();
	}
	private boolean checkState(boolean mCollision){
		LogCatUtils.showString("==isPreview["+TheApp.getDeviceId()+"]=="+
		TheApp.mCManager.isPreview[TheApp.getDeviceId()]);
		if (TheApp.mCManager.isPreview[TheApp.getDeviceId()]) {
			return true;
		}else {
			if (TheApp.mApp.windowManageView!=null) {
				TheApp.mApp.windowManageView.registerWatch(CollisionVideoService.this,mCollision);
			}
		}
		return false;
	}
	private void addNotify() {//数据接收器
		DataGsensor.NOTIFY_EVENTS[FinalGsensor.U_LOCK_RECORD].addNotify(notifyRefresh, 1);
		DataGsensor.NOTIFY_EVENTS[FinalGsensor.U_SLEEP_STOP_RECORD].addNotify(notifyRefresh, 0);
		DataGsensor.NOTIFY_EVENTS[FinalGsensor.U_STANDBY_RECORD_TIME].addNotify(notifyRefresh, 0);
	}
	private void removeNotify() {
		DataGsensor.NOTIFY_EVENTS[FinalGsensor.U_LOCK_RECORD].removeNotify(notifyRefresh);
		DataGsensor.NOTIFY_EVENTS[FinalGsensor.U_SLEEP_STOP_RECORD].removeNotify(notifyRefresh);
		DataGsensor.NOTIFY_EVENTS[FinalGsensor.U_STANDBY_RECORD_TIME].removeNotify(notifyRefresh);
	}
	IUiNotify notifyRefresh = new IUiNotify() {
		@Override
		public void onNotify(int updateCode, int[] ints, float[] flts, String[] strs) {
			switch (updateCode) {
			case FinalGsensor.U_LOCK_RECORD:
				LogCatUtils.showString("  发生碰撞    ");
				if (ints!=null&&ints.length>0&&!mCollisionHandle) {
					switchdvrmode();
					mCollisionHandle=true;
					LogCatUtils.showString("  发生碰撞录制    ");
					startRecord(null);
				}
				break;
			default:
				break;
			}
		}
	};
	private int mPictrue=0;
	private void startRecord(Intent intent) {
		LogCatUtils.showString("===recording=="+RecordingStatus.getInstance().getmRecordingStatus());
		mPictrue=3;
		if (RecordingStatus.getInstance().getmRecordingStatus()==1) {
			TheApp.mCManager.recordLock();
		}else if(TheApp.mCManager.doOpenCamera(TheApp.getDeviceId())) {
			TheApp.mCManager.startRecord(TheApp.getDeviceId());
			TheApp.mCManager.recordLock();
		}
		mWolShurtPhoto(intent);
	}
}
