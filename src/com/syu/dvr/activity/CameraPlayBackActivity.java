package com.syu.dvr.activity;

import java.io.IOException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.syu.codec.Codec;
import com.syu.codec.PreviewView;
import com.syu.codec.Codec.InitCallback;
import com.syu.dvr.R;
import com.syu.dvr.TheApp;
import com.syu.dvr.control.CameraManager;
import com.syu.dvr.factory.PlayBackTFcardStatu;
import com.syu.dvr.factory.PlayBackTFcardStatu.TFcardStatuCallback;
import com.syu.dvr.utils.CameraPlayUi;
import com.syu.dvr.utils.Config;
import com.syu.dvr.utils.LogCatUtils;
import com.syu.dvr.utils.PublicClass;
import com.uvc.jni.RainUvc;

public class CameraPlayBackActivity extends BaseActivity{
	public static CameraPlayBackActivity activity=null;
	private FrameLayout mLinearLayout;
	private PreviewView mSurfaceView;
	private LinearLayout.LayoutParams mLayoutParams;
	private RelativeLayout.LayoutParams rParams;
	private LinearLayout mFromLayout;
	private RelativeLayout relativeLayout;
	public static boolean isShow=false; 
	public boolean isonStop=false;
	private View mRecodLayout;
	public CameraPlayUi playUi;
	private String mPlayName;
	private IntentFilter mFilter;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(initView());
		TheApp.mCManager=new CameraManager();
		activity=CameraPlayBackActivity.this;
		Intent intent=getIntent();
		if (intent.hasExtra("TimeData")) {
			Bundle bundle=intent.getBundleExtra("TimeData");
			if (bundle.containsKey("playName")) {
				mPlayName=bundle.getString("playName");
			}
		}
		playUi=new CameraPlayUi(TheApp.mApp,mPlayName);
	}
	private View initView() {
		LayoutInflater fInflater=LayoutInflater.from(TheApp.mApp);
		mRecodLayout=fInflater.inflate(R.layout.layout_cameraplay, null);
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
	public void initData(){
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
				LogCatUtils.showString("=Callback==result=="+String.valueOf(result));
				if (result) {
					addHandlerUI();
				}
			}
		});
	}
	protected void addHandlerUI() {
		// TODO Auto-generated method stub
		playUi.addView(mRecodLayout);
		playUi.startplay();
		playUi.handler.postDelayed(new Runnable() {
			public void run() {
				if (mRecodLayout!=null&&mRecodLayout.getParent()==null&&mLinearLayout!=null) {
					mLinearLayout.addView(mRecodLayout);
				}
			}
		}, 1*1000);
	}
	@Override
	protected void onResume() {
		super.onResume();
		if (RainUvc.setMode(2)>=0) {
			initData();
		}
		mFilter = new IntentFilter();
		mFilter.addAction(Intent.ACTION_USB_CAMERA);
		registerReceiver(mUsbCameraHotPlug, mFilter);
	}
	private BroadcastReceiver mUsbCameraHotPlug = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	if (TheApp.mCManager==null) {
				return;
			}
            if(intent.getFlags() == 32785) {
            	outCamera();
            	TheApp.mCManager.doCloseCamera();
            	finish();
            }
        }
    };
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		finish();
		return true;
	}
	@Override
	protected void onPause() {
		
		super.onPause();
	}
	@Override
	protected void onStop() {
		super.onStop();
		playUi.removeView();
		outCamera();
		TheApp.mCManager.doCloseCamera();
		unregisterReceiver(mUsbCameraHotPlug);
		finish();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		activity=null;
	}
	private void onDestroyTextureView(){
		TheApp.mCManager.doStopPreview(TheApp.getDeviceId());
		if (mSurfaceView!=null) {
			mSurfaceView.setVisibility(View.INVISIBLE);
		}
	}
	public synchronized void outCamera(){
		onDestroyTextureView();
		if ((mRecodLayout!=null)&&(mRecodLayout.getParent()!=null)) {
			mLinearLayout.removeView(mRecodLayout);
		}
		mFromLayout.removeAllViews();		
	}
	
}
