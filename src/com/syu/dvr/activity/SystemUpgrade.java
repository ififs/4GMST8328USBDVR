package com.syu.dvr.activity;

import com.syu.dvr.R;
import com.syu.dvr.TheApp;
import com.syu.dvr.control.StartActivity;
import com.syu.dvr.observ.WatchedUsb;
import com.syu.dvr.observ.WatchedUsb.Watcher;
import com.syu.dvr.utils.Config;
import com.uvc.jni.RainUvc;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class SystemUpgrade extends BaseActivity implements Watcher{
	private LinearLayout mLinearLayout;
	private View []mView=new View[10];
	private LinearLayout.LayoutParams params;
	private int mNumber=0;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.system_upgrade_layout);
		mLinearLayout=(LinearLayout) findViewById(R.id.upgrade_layout);
		params=new LayoutParams(10, 10);
		params.gravity=Gravity.CENTER_VERTICAL;
		initView();
	}
	@Override
	protected void onResume() {
		if (TheApp.mCManager==null) {
			return;
		}
		TheApp.mCManager.stopRecord(TheApp.getDeviceId());
		if (TheApp.mIsShengMaiIC) {
			RainUvc.reboot();
		}else {
			TheApp.mCManager.setUvcExtenrnCall(TheApp.getDeviceId(), "62", "a5","");
		}
		mNumber=0;
		WatchedUsb.getInstance().addWatched(this);
		mhandler.sendEmptyMessage(0);
		super.onResume();
	}
	private void initView() {
		for (int i = 0; i < 10; i++) {
			View view=new View(this);
			view.setBackgroundResource(R.drawable.ic_uicommon_add_n);
			mView[i]=view;
			mLinearLayout.addView(view, params);
		}
	}
	int mcount=0;
	private void upData(){
		for (int i = 0; i < mView.length; i++) {
			if (i<mcount) {
				mView[i].setBackgroundResource(R.drawable.ic_uicommon_add_p);
			}else {
				mView[i].setBackgroundResource(R.drawable.ic_uicommon_add_n);
			}
		}
		
		if (mcount==10) {
			mcount=1;
		}else {
			mcount++;
		}
	}
	
	private Handler mhandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			upData();
			mhandler.sendEmptyMessageDelayed(0, 500);
		};
	};
	@Override
	public void updata(int updata) {
		if (updata==1) {
			mNumber++;
			if (mNumber==2) {
				StartActivity.startAcitivity(MainActivity.class);
				finish();
			}
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return true;
	}
}
