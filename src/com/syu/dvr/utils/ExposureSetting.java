package com.syu.dvr.utils;

import com.syu.dvr.R;
import com.syu.dvr.TheApp;

import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class ExposureSetting {
	private Dialog settingDialog;
	public Dialog ExposureSetting(){
		settingDialog=new Dialog(TheApp.mApp,R.style.setting_dialog);
    	LinearLayout.LayoutParams params=new LayoutParams(600, 450);
    	View view=LinearLayout.inflate(TheApp.mApp, R.layout.setting_view_layout, null);
    	settingDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
    	settingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    	settingDialog.setContentView(view,params);
		settingDialog.show();
		return settingDialog;
	}
	

}
