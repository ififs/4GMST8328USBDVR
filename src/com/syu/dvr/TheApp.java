package com.syu.dvr;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Debug.MemoryInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;

import com.syu.dvr.activity.BaseActivity;
import com.syu.dvr.adapter.GridViewAdapter;
import com.syu.dvr.control.CameraManager;
import com.syu.dvr.control.StartActivity;
import com.syu.dvr.control.SystemManager;
import com.syu.dvr.factory.SwitchDev;
import com.syu.dvr.observ.WatchedUsb;
import com.syu.dvr.server.CollisionVideoService;
import com.syu.dvr.upgrade.UpgradeManager;
import com.syu.dvr.utils.Config;
import com.syu.dvr.utils.CrashHandler;
import com.syu.dvr.utils.FileStatu;
import com.syu.dvr.utils.HandleConstant;
import com.syu.dvr.utils.LogCatUtils;
import com.syu.dvr.utils.LogPreview;
import com.syu.dvr.utils.MyToast;
import com.syu.dvr.utils.PublicClass;
import com.syu.dvr.utils.RecordingStatus;
import com.syu.dvr.view.WindowManageView;
import com.syu.dvr.widget.MyAlertDialog;
import com.syu.module.MsToolkitConnection;
import com.syu.module.gsensor.ConnectionGsensor;
import com.syu.module.main.ConnectionMain;
import com.syu.module.sound.ConnectionSound;
import com.uvc.jni.RainUvc;

public class TheApp extends Application{
	
	public static TheApp mApp;
	public static SystemManager mSysmanager;
	public static String defuPath;
	public static String FILE_DIRE;
	public static String mPhotoPath;
	public static String mVideoPath;
	public static String FlPath;
	public static String mCachePath;
	public static String MAIN="com.syu.dvr:main";
	public static List<String>mSeleNameList;
	public MyToast toast;
	public static boolean isSdExit=false;
	public static String videoPath;
	public BaseActivity activity;
	public static boolean isShowVideo=true;
	public PublicClass publicClass;
	public boolean isBoot=false;
	public AppHandler mHandler;
	public final int UPDATE_FILE=0;
	private List<String> mFileNameList;
	private List<String>mLockTime;
	private int mCameraID=0;
	public static int [] array;
	public Intent mFileService;
	public static boolean ismFileService=false;
	public static boolean       isSwitch	=true;
	public static boolean isServiceLoad=false;
	public static boolean MfragmentIsExit=false;
	public static boolean isWoL=false;
	public int value=-1;
	public List<String>mDataTime;
	public static CameraManager mCManager;
	public  boolean isscrolling=false;
	public  boolean isscroll=false;
	public ConnectionMain cMain;
	public ArrayList<Activity>mActivityList;
	public static Semaphore semaphore;
	public Intent notficationService;
	public FileStatu fileStatu;
	public static AtomicBoolean atomicBoolean;
	private boolean mI2Cerror=false;
	private boolean checkfailure=false;
	private static List<GridViewAdapter.ViewHolder>mHolders;
	public WindowManageView windowManageView;
	public static boolean mIsShengMaiIC=false;
	public boolean isPlayBack=false;
	public static boolean mProcessStart=false;
	public static boolean mPlayOrDelete;
	public void onCreate() {
		super.onCreate();
		mApp=this;
		getDeviceId();
		windowManageView=new WindowManageView(mApp);
		RecordingStatus.getInstance();
		CrashHandler.getInstance(mApp);
		initData();
		LogCatUtils.showString("===App oncreate===");
//		logcatPreview();
	}
	public boolean clientForeign(){
		return SystemProperties.getBoolean("ro.client.foreign", false);
	}
	private void logcatPreview(){
		File file=new File("/sdcard", "logs.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		LogPreview.getInstance(getApplicationContext(), "/sdcard/logs.txt");
	}
	private void connect() {
		MsToolkitConnection.getInstance().addObserver(ConnectionMain.getInstance());
		MsToolkitConnection.getInstance().addObserver(ConnectionGsensor.getInstance());
		MsToolkitConnection.getInstance().addObserver(ConnectionSound.getInstance());
		MsToolkitConnection.getInstance().connect(this);
	}
	public String[] getVersions(){
		String [] mVersions=new String[2];
		
		try {
			mVersions=new String[2];
			PackageManager pm = mApp.getPackageManager();  
	        PackageInfo pi = pm.getPackageInfo(mApp.getPackageName(), 0);  
	        String name = pi.versionName;  
	        int code = pi.versionCode;
	        mVersions[0]=name.isEmpty()?"":name;
	        mVersions[1]=code<=0?"":String.valueOf(code);
		} catch (NameNotFoundException e) {
			
			e.printStackTrace();
		}  
		return mVersions;
	}
	
	public String deviceid;
	public synchronized static int getDeviceId(){
		return 1;
		/*long type=RainUvc.getPVId("/dev/video5");
    	if (type==2502361858l) {
			TheApp.mIsShengMaiIC=true;
		}else {
			TheApp.mIsShengMaiIC=false;
		}
    	return TheApp.mIsShengMaiIC?0:1;*/
	}
	public static List<GridViewAdapter.ViewHolder> getmHolders() {
		return mHolders;
	}
	public static void setmHolders(List<GridViewAdapter.ViewHolder> mHolders) {
		TheApp.mHolders = mHolders;
	}
	public List<String> getmFileNameList() {
		return mFileNameList;
	}
	public void setmFileNameList(List<String> mFileNameList) {
		
		this.mFileNameList = mFileNameList;
	}
	public List<String> getmLockTime() {
		return mLockTime;
	}
	public void setmLockTime(List<String> mLockTime) {
		this.mLockTime = mLockTime;
	}
	public List<String> getmDataTime() {
		return mDataTime;
	}
	public void setmDataTime(List<String> mDataTime) {
		this.mDataTime = mDataTime;
	}
	
	public boolean ismI2Cerror() {
		return mI2Cerror;
	}
	public void setmI2Cerror(boolean mI2Cerror) {
		this.mI2Cerror = mI2Cerror;
	}
	public boolean isCheckfailure() {
		return checkfailure;
	}
	public void setCheckfailure(boolean checkfailure) {
		this.checkfailure = checkfailure;
	}
	private void initData(){
		atomicBoolean=new AtomicBoolean(false);
//		LogCatUtils.initLogcat();
		String processName = null;
		ActivityManager manager=(
				ActivityManager) TheApp.mApp.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo>runningAppProcessInfos=manager.getRunningAppProcesses();
		int pid=android.os.Process.myPid();
		for (ActivityManager.RunningAppProcessInfo info:runningAppProcessInfos) {
			if (pid==info.pid) {
				int[] pids=new int[]{pid};
				MemoryInfo[] memoryInfo=manager.getProcessMemoryInfo(pids);
				processName=info.processName;
			}
		}
		UpgradeManager.getInstance();
		fileStatu=new FileStatu();
		semaphore=new Semaphore(0);
		LogCatUtils.showString("===msg==="+processName);
		if (processName!=null&&processName.equals(MAIN)) {
			connect();
			mCManager=new CameraManager();
			startService(new Intent(mApp, CollisionVideoService.class));
		}
		mSysmanager=new SystemManager();
		mSeleNameList=new ArrayList<String>();
		publicClass=new PublicClass();
		mHandler=new AppHandler();
		activity=new BaseActivity();
		mFileNameList=new ArrayList<String>();
		mDataTime=new ArrayList<String>();
		mLockTime=new ArrayList<String>();
		mActivityList=new ArrayList<Activity>();
		mHandler.sendEmptyMessage(13);
		if (processName!=null&&processName.equals(MAIN)) {
			mFilter = new IntentFilter();
			mFilter.addAction(Intent.ACTION_USB_CAMERA);
			registerReceiver(mUsbCameraHotPlug, mFilter);
		}
		String ve=getVersions()[1];
		File Version=new File("dvr"+ve+".apk");
		if (Version.exists()) {
			Version.delete();
		}
	}
	public void MyTosat(int msg){
		if (toast!=null) {
			toast.cancel();
		}
		String message=mApp.getString(msg);
		toast=new MyToast(mApp, message, 100, Color.GREEN, 25);
		toast.show();
	}
	public void MyTosat(String msg){
		if (toast!=null) {
			toast.cancel();
		}
		toast=new MyToast(mApp, msg, 100, Color.GREEN, 25);
		toast.show();
	}
	@SuppressLint("HandlerLeak") public class AppHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			
			case UPDATE_FILE:
				
				break;
			case 1:
				int souceId=(Integer) msg.obj;
				MyAlertDialog dialog=new MyAlertDialog(TheApp.mApp);
				dialog.setData(souceId);
				break;
			case 2:
				MyTosat(R.string.str_not_supported);
			case 3:
				MyTosat(R.string.str_not_start_failed);
				break;
			
			case 5:
				break;
			case 6:
				break;
			case 7:
				MyTosat(R.string.str_file_islock_video);
				break;
			case 10:
				unregisterReceiver(mUsbCameraHotPlug);
				LogCatUtils.showString(" �˳���ǰ����  ");
				System.exit(0);
				break;
			case 12:
				break;
			case 13:
				break;
			case 14:
				TheApp.mApp.MyTosat(R.string.str_dvr_recoding_error);
				break;
			case HandleConstant.MY_TOAST_FOR_SOUCEID:
				MyTosat(msg.arg1);
				break;
			case 20:
				TheApp.mCManager.doOpenCamera(getDeviceId());
				WatchedUsb.getInstance().setData(1);
				break;
			
			default:
				break;
			}
		}
	}
	
	private  SharedPreferences getPreference(){
		return mApp.getSharedPreferences(/*Config.SETTING*/"", MODE_PRIVATE);
	}
	public void saveBoolean(String key,boolean value){
		getPreference().edit().putBoolean(key, value).commit();
	}
	public  void saveInt(String key, int value) {
		getPreference().edit().putInt(key, value).commit();
		
	}
	public void savaString(String key, String value){
		getPreference().edit().putString(key, value).commit();
	}
	public  int getInt(String key,int defValue){
		return getPreference().getInt(key, defValue);
	}
	public boolean getBoolean(String key,boolean value){
		return getPreference().getBoolean(key, value);
	}
	public String getString(String key,String defValue){
		return getPreference().getString(key, defValue);
	}
	public void playMeideo(String path){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK 
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File(path));
		intent.setDataAndType(uri, "video/*");
		startActivity(intent);
	}

	private IntentFilter mFilter;
	public int addTime=0;
	public int clickTiem=0;
	public int outTime=0;
	private BroadcastReceiver mUsbCameraHotPlug = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	if (TheApp.mCManager==null) {
				return;
			}
            if(intent.getFlags() == 32785) {
            	SwitchDev.getInstance().setmSwitchfu(true);
            	TheApp.mCManager.doCloseCamera();
            	LogCatUtils.showString("摄像头移出去广播");
            	RecordingStatus.getInstance().setmRecordingStatus(0);
            	WatchedUsb.getInstance().setData(0);
            	sendBroadCast(0);
            } else if(intent.getFlags() == 32786) {
            	RecordingStatus.getInstance().clearStatus();
            	LogCatUtils.showString("摄像头插入广播");
            	SwitchDev.getInstance().setSwitch(false);
				mHandler.sendEmptyMessageDelayed(20, 1000);
				return;
            }
        }
    };
    private String SHOW_NOTIFICATION="android.com.syu.dvr.action.SHOW_NOTIFICATION";
	private String CANCEL_NOTIFICATION="android.com.syu.dvr.action.CANCEL_NOTIFICATION";
    public void sendBroadCast(int flag){
    	LogCatUtils.showString("SHOW_NOTIFICATION==="+flag);
		if (flag==1) {
			int value=RecordingStatus.getInstance().getmRecordingStatus();
			if (value==1) {
				StartActivity.sendBroadCast(SHOW_NOTIFICATION);
			}
		}else if (flag==0) {
			StartActivity.sendBroadCast(CANCEL_NOTIFICATION);
		}
    }
}
