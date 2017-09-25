package com.syu.dvr.activity;

import java.util.ArrayList;

import com.syu.dvr.R;
import com.syu.dvr.TheApp;
import com.syu.dvr.factory.PlayBackTFcardStatu;
import com.syu.dvr.factory.SwitchDev;
import com.syu.dvr.factory.PlayBackTFcardStatu.TFcardStatuCallback;
import com.syu.dvr.observ.WatchedUsb;
import com.syu.dvr.utils.Config;
import com.syu.dvr.utils.LogCatUtils;
import com.syu.dvr.utils.RecordingStatus;
import com.syu.dvr.widget.ComInterface.CloseActivityFinsh;
import com.syu.dvr.widget.ComInterface.OutAllActivity;
import com.syu.dvr.widget.ComInterface.ToHandle;
import com.uvc.jni.RainUvc;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;

public class BaseActivity extends Activity implements OutAllActivity,TFcardStatuCallback{
	public int BACK_PLAY=0;
	
	public static BaseActivity app;
	public ArrayList<Activity>mActivityList=new ArrayList<Activity>();
	public void startAcitivity(Class<?extends Activity>target){
		Intent intent =new Intent(this, target);
		startActivity(intent);
	}
	public void startAcitivity(Class<?extends Activity>target,Bundle data){
		Intent intent =new Intent(this, target);
		intent.putExtra("TimeData", data);
		startActivity(intent);
		
	}
	public void startActivityNoAnim(Class<? extends Activity>target){
		Intent intent =new Intent(this, target);
		startActivity(intent);
		overridePendingTransition(0, 0);
	}
	
	public void closeActivity(){
		finish();
	}
	public void backplay(boolean backplay){
		if (backplay&&TheApp.mIsShengMaiIC) {
			PlayBackTFcardStatu.getInstance().RunTask();
			PlayBackTFcardStatu.getInstance().setTFcardStatuCallback(this);
			IntentFilter mFilter = new IntentFilter();
			mFilter.addAction(Intent.ACTION_USB_CAMERA);
			registerReceiver(mUsbCameraHotPlug, mFilter);
		}
	}
	private BroadcastReceiver mUsbCameraHotPlug = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getFlags() == 32785) {
            	String string=getString(R.string.str_no_sdcard_out_play);
            	TheApp.mApp.MyTosat(string.replace("SD", ""));
            	handler.sendEmptyMessageDelayed(0, 1000);
            } 
        }
    };
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		app=BaseActivity.this;
		TheApp.mApp.mActivityList.add(this);
	}
	public void addActivity(Activity activity){
		if (!TheApp.mApp.mActivityList.equals(activity)) {
			TheApp.mApp.mActivityList.add(activity);
		}
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		closeActivity();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		TheApp.mApp.mActivityList.remove(this);
	}
	
	public void closeApp(CloseActivityFinsh finsh){
		for(Activity activity:TheApp.mApp.mActivityList){
			if (activity!=null) {
				activity.finish();
			}
		}
		TheApp.mApp.mActivityList.clear();
		finsh.closeActivityFinsh();
	
	}
	public void closeApp(){
		for(Activity activity:TheApp.mApp.mActivityList){
			if (activity!=null) {
				activity.finish();
			}
		}
		TheApp.mApp.mActivityList.clear();
		PlayBackTFcardStatu.getInstance().unRegister();
	}
	
	@Override
	public void outAllActivity() {
		closeApp();
	}
	@Override
	public void Callback(int staut) {
		if (staut==0&&RainUvc.getTFCardStatus()==0) {
			TheApp.mApp.MyTosat(R.string.str_no_sdcard_out_play);
			handler.sendEmptyMessageDelayed(0, 1000);
		}
	}
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			closeApp();
		};
	};
}
