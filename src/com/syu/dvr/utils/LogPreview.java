/**
 * 工程名:PCBA_Launcher_3188_2
 * 文件名:LogPreview.java
 * 包   名:com.syu.net
 * 日   期:2016年1月25日上午9:01:41
 * 作   者:fyt 
 * Copyright (c) 2016, kexuan52@yeah.net All Rights Reserved.
 *
 */
package com.syu.dvr.utils;

import java.io.File;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 *类   名:LogPreview
 *功   能:TODO
 *
 *日  期:2016年1月25日 上午9:01:41
 * @author fyt
 *
 */
public class LogPreview {
	
	boolean mDebug;
	String debugSwith;
	static boolean isShow=true;//是否显示
	WindowManager wm;
	WindowManager.LayoutParams mParams;
	
	TextView logPreview;
	FrameLayout rootView;
	Context mContext;
	Handler mHandler;
	
	StringBuffer logs = new StringBuffer();
	
	HandlerThread checkWork = new HandlerThread("check work");
	
	static LogPreview instance;
	
	/**
	 * @return the instance
	 */

	public static LogPreview getInstance(Context mContext, String swith) {
		if(instance == null)
			instance = new LogPreview(mContext, swith);
		return instance;
	}
	
	/**
	* <p>Title: </p>
	* <p>Description: </p>
	*/
	LogPreview(Context mContext, String swith) {
		Log.e("Logs", "LogPreview === LogPreview swith = " + swith);
		this.mContext = mContext.getApplicationContext();
		wm = (WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE);
		init();
		mHandler = new Handler(Looper.getMainLooper());
		debugSwith = swith;
		checkWork.start();
		
		
		final Handler handler = new Handler(checkWork.getLooper());
		Runnable check = new Runnable() {
			
			@Override
			public void run() {
				checkDebug();
				Log.e("Logs", "mDebug === " + mDebug);
				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						if(mDebug) {
							if(rootView.getParent() == null) {
								wm.addView(rootView, mParams);
							}
						}else{
							if(rootView.getParent() != null) {
								wm.removeView(rootView);
							}
						}
					}
				});
				
				handler.removeCallbacks(this);
				handler.postDelayed(this, 25 * 1000);
			}
		};
		handler.post(check);
	}
	
	public void init(){
		mParams = new WindowManager.LayoutParams(
				LayoutParams.MATCH_PARENT, 
				LayoutParams.MATCH_PARENT, 
				0, 0, 
				LayoutParams.TYPE_SYSTEM_ALERT, 
				LayoutParams.FLAG_NOT_TOUCH_MODAL 
				| LayoutParams.FLAG_NOT_FOCUSABLE
				| LayoutParams.FLAG_NOT_TOUCHABLE, 
				PixelFormat.RGBA_8888);
		
		rootView = new FrameLayout(mContext);
	
		logPreview = new TextView(mContext);
		logPreview.setTextSize(14);
		logPreview.setGravity(Gravity.BOTTOM | Gravity.LEFT);
		logPreview.setTextColor(Color.GREEN);
		rootView.addView(logPreview, 
				new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 
				FrameLayout.LayoutParams.WRAP_CONTENT));
	}
	
	public void checkDebug() {
		if(debugSwith == null || debugSwith.isEmpty()) return;
		File file = new File(debugSwith);
		mDebug = file.exists();
		file = null;
	}
	
	/**
	 * @param debugSwith the debugSwith to set
	 */
	public void setDebugSwith(String debugSwith) {
		this.debugSwith = debugSwith;
	}
	
	public void showLogLine(String log) {
		if(!mDebug) return;
		if(logs.length() >= 1024 * 20) {
			logs.replace(0, 1024 * 4, "");
		}
		logs.append(log + "\n");
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				logPreview.setText(logs.toString());
			}
		});
	}
	
	public void destroy(){
		if(checkWork != null) {
			try {
				checkWork.getLooper().quit();
				checkWork.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void show(String msg) {
		if (isShow) {
			if(instance == null) return;
			instance.showLogLine(msg);
		}
	}
}
