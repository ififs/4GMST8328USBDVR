package com.syu.dvr.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telecom.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.syu.codec.Codec;
import com.syu.codec.Codec.InitCallback;
import com.syu.codec.PreviewView;
import com.syu.dvr.R;
import com.syu.dvr.TheApp;
import com.syu.dvr.control.CameraManager.CameraManagerCallback;
import com.syu.dvr.factory.ActiviytIsOpen;
import com.syu.dvr.factory.HideLayout;
import com.syu.dvr.factory.PreviewOverTime;
import com.syu.dvr.observ.WatchedUsb;
import com.syu.dvr.observ.WatchedUsb.Watcher;
import com.syu.dvr.upgrade.UpgradeManager;
import com.syu.dvr.utils.Config;
import com.syu.dvr.utils.LogCatUtils;
import com.syu.dvr.utils.ProgressDialog;
import com.syu.dvr.utils.PublicClass;
import com.syu.dvr.utils.RecordUi;
import com.syu.dvr.widget.NoCameraCarousel;
import com.syu.jni.SyuJniNative;
import com.uvc.jni.RainUvc;

public class MainActivity extends BaseActivity implements Watcher,CameraManagerCallback{
	public static MainActivity activity=null;
	private FrameLayout mLinearLayout;
	private PreviewView mSurfaceView;
	private TextView mWaning;
	private LinearLayout.LayoutParams mLayoutParams;
	private RelativeLayout.LayoutParams rParams;
	private LinearLayout mFromLayout;
	private RelativeLayout relativeLayout;
	public static boolean isModeSwitch=false; 
	public boolean isonStop=false;
	private View mRecodLayout;
	public RecordUi recordUi;
	public MyHandler handler;
	private Dialog mWarningDialog;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(initView());
		activity=MainActivity.this;
		if (TheApp.mApp.notficationService!=null) {
			TheApp.mApp.notficationService.setAction("android.com.syu.dvr.action.CANCEL_NOTIFICATION");
			TheApp.mApp.startService(TheApp.mApp.notficationService);
		}
		handler=new MyHandler();
		if (TheApp.mApp.clientForeign()) {
			mWarningDialog=PublicClass.getInstance().NoCameraWarning(2);
		}else {
			mWarningDialog=new NoCameraCarousel(activity, R.style.usb_camera_no_find);
		}
	}
	private View initView() {
		LayoutInflater fInflater=LayoutInflater.from(TheApp.mApp);
		mRecodLayout=fInflater.inflate(R.layout.layout_main, null);
		mWaning=(TextView)mRecodLayout.findViewById(R.id.waning);
		mLinearLayout = new FrameLayout(this);
		mLinearLayout.setBackgroundColor(getResources().getColor(R.color.black));
		relativeLayout=new RelativeLayout(this);
		rParams=new RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,
				android.widget.RelativeLayout.LayoutParams.MATCH_PARENT);
		mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mFromLayout=new LinearLayout(this);
		mFromLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		relativeLayout.addView(mFromLayout);
		mLinearLayout.addView(relativeLayout, rParams);
		if (PublicClass.getInstance().isVerticalScreen()) {
			mLayoutParams.bottomMargin=122;
			mLayoutParams.topMargin=72;
		}
		return mLinearLayout;
	}
	private void showLayout(){
		if (recordUi!=null&&RecordUi.mHandler!=null) {
			RecordUi.mHandler.sendEmptyMessage(recordUi.SHOW_LAYOUT);
		}
		HideLayout.getInstance().RunTask();
	}
	public void initData(){
		LogCatUtils.showString("======initdata===");
		if (mFromLayout!=null) {
			mFromLayout.removeAllViews();
		}
		mSurfaceView=new PreviewView(this);
		mSurfaceView.setCallback(new com.syu.codec.PreviewView.Callback() {
			
			@Override
			public void onDestroyed(int type) {
				TheApp.mCManager.removePreviewView(type);
			}
			
			@Override
			public void onCreated(int type, SurfaceHolder holder) {
				TheApp.mCManager.startPreview(type, null);
			}
		});
		mSurfaceView.setType(TheApp.getDeviceId());
		mFromLayout.addView(mSurfaceView,mLayoutParams);
		mSurfaceView.setInitCallback(new InitCallback() {
			@Override
			public void onCallback(boolean result) {
				LogCatUtils.showString(" setInitCallback = "+String.valueOf(result));
				if (result) {
					TheApp.mCManager.setmPreviewView(mSurfaceView, TheApp.getDeviceId());
					addHandlerUI();
				}
			}
		});
		mSurfaceView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showLayout();
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		isModeSwitch=false;
		if (!TheApp.mApp.clientForeign()) {
			UpgradeManager.getInstance().BackStageNetWordCheck();
		}
		if (TheApp.mApp.windowManageView!=null) {
			LogCatUtils.showString("==removeView=====");
			TheApp.mApp.windowManageView.removeView();
		}
		ProgressDialog.getInstance().progressShow(R.string.camera_loading);
		ProgressDialog.getInstance().enableCanceledOnTouchOutside(true);
		PreviewOverTime.getInstance().setRecordCallback(this);
		PreviewOverTime.getInstance().RunTask();
		if (TheApp.mCManager!=null) {
			TheApp.mCManager.registerCallback(this);
		}
		Bundle outparam=new Bundle();
		SyuJniNative.getInstance().syu_jni_command(12, null, outparam);
		if (outparam!=null) {
			if (outparam.getInt("param0",-1)==1) {
				Dialog dialog=PublicClass.getInstance().NoCameraWarning(1);
				dialog.show();
				return;
			}
		}
		isonStop=false;
		handler.sendEmptyMessage(0);
		handler.sendEmptyMessageDelayed(3, 500);
		LogCatUtils.showString("   onResume  " );
	}
	private void addHandlerUI(){
		PreviewOverTime.getInstance().stopTimer();
		handler.sendEmptyMessage(1);
		recordUi=new RecordUi();
		recordUi.addView(mRecodLayout);
		handler.postDelayed(new Runnable() {
			public void run() {
				if (mRecodLayout!=null&&mRecodLayout.getParent()==null&&mLinearLayout!=null) {
					mLinearLayout.addView(mRecodLayout);
				}
				if (mWarningDialog!=null) {
					mWarningDialog.cancel();
				}
				ProgressDialog.getInstance().progressdismiss();
			}
		}, 1*1000);
	}
	public  class MyHandler extends Handler{
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			switch (msg.what) {
			case 0:
				mSwitchCamera();
				break;
			case 1:
				break;
			case 2:
				if (mWarningDialog!=null&&!mWaning.isShown()) {
					mWarningDialog.cancel();
					mWarningDialog.show();
					ProgressDialog.getInstance().progressdismiss();
				}
				break;
			case 3:
				if (activity!=null) {
					WatchedUsb.getInstance().addWatched(activity);
				}
				break;
			case 4:
				
				break;
			default:
				break;
			}
			
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		handler.postDelayed(new Runnable() {
			public void run() {
				finish();
			}
		}, 600);
		return true;
	}
	int retValue=0;
	private void mSwitchCamera(){
		if (TheApp.mIsShengMaiIC) {
			retValue=RainUvc.setMode(1);
		}else {
			SyuJniNative.getInstance().syu_jni_command(152, null, null);
		}
	}
	@Override
	protected void onPause() {
		if (recordUi!=null) {
			recordUi.closeDialog();
		}
		super.onPause();
	}
	@Override
	protected void onStop() {
		super.onStop();
		LogCatUtils.showString(" onStop()  ");
		ProgressDialog.getInstance().progressdismiss();
		PublicClass.getInstance().NoCameraWarning(6).dismiss();
		WatchedUsb.getInstance().removeWatched(activity);
		PreviewOverTime.getInstance().remRecordCallback();
		if (mWaning!=null) {
			mWaning.setVisibility(View.GONE);
		}
		isonStop=true;
		TheApp.mApp.sendBroadCast(1);
		outCamera();
		if (mWarningDialog!=null) {
			mWarningDialog.cancel();
		}
		if (TheApp.mCManager!=null) {
			TheApp.mCManager.unregisterCallback(this);
		}
		ActiviytIsOpen.getInstance().setOpen(false);
	}
	@Override
	protected void onDestroy() {
		LogCatUtils.showString(" onDestroy()  ");
		if (recordUi!=null) {
			recordUi.unRegister();
		}
		activity=null;
		super.onDestroy();
	}
	private void onDestroyTextureView(){
		TheApp.mCManager.doStopPreview(TheApp.getDeviceId());
		if (mSurfaceView!=null) {
			mSurfaceView.setVisibility(View.INVISIBLE);
		}
	}
	@Override
	public void updata(int updata) {
		LogCatUtils.showString("  updata  "+updata);
		if (updata==0) {
			outCamera();
			TheApp.mApp.setmI2Cerror(false);
			TheApp.mApp.setCheckfailure(false);
			if (!isonStop&&!isModeSwitch) {
				TheApp.mApp.MyTosat(R.string.str_camera_exit);
			}
			if (mWaning!=null) {
				mWaning.setVisibility(View.GONE);
			}
		}else if (updata==1) {
			initData();
		}else if (updata==2) {
			if (TheApp.mApp.ismI2Cerror()) {
				mWaning.setVisibility(View.VISIBLE);
				mWaning.setText(R.string.i2c_error);
			}else if (TheApp.mApp.isCheckfailure()) {
				mWaning.setVisibility(View.VISIBLE);
				mWaning.setText(R.string.check_failure);
			}
		}else if (updata==4) {
			mWaning.setVisibility(View.VISIBLE);
			mWaning.setText(R.string.shake_failed);
		}
	}
	public synchronized void outCamera(){
		onDestroyTextureView();
		if (recordUi!=null) {
			recordUi.closeDialog();
		}
		if ((mRecodLayout!=null)&&(mRecodLayout.getParent()!=null)) {
			mLinearLayout.removeView(mRecodLayout);
		}
		mFromLayout.removeAllViews();
		LogCatUtils.showString("  outCamera() ");
	}
	@Override
	public void cameraCallback(int value) {
		PublicClass.getInstance().NoCameraWarning(value).show();
	}
	public interface StartPreViewCallBack{
		void PreviewCallback();
	}
}
