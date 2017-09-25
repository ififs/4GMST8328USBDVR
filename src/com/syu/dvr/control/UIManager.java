package com.syu.dvr.control;

import com.syu.dvr.TheApp;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class UIManager {
	public double getWidth(){
		WindowManager manager=(WindowManager) TheApp.mApp.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics=new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels/10-20;
		
	}

}
