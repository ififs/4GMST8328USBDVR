package com.syu.dvr.utils;

import java.util.Timer;
import java.util.TimerTask;

import android.R.integer;
import android.app.Dialog;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.syu.dvr.R;
import com.syu.dvr.TheApp;
import com.syu.dvr.factory.AppIsRun;

public class ProgressDialog {
	
	public static ProgressDialog dialog;
	private TextView mTextView;
	private Dialog mFormatsdDialog;
	private int mSetuptime=60;
	private ProgressDialog(){
		initProgress();
	}
	public static ProgressDialog getInstance(){
		if (dialog==null) {
			synchronized (ProgressDialog.class) {
				if (dialog==null) {
					dialog=new ProgressDialog();
				}
			}
		}
		return dialog;
	}
	private void initProgress(){
		int []size;
    	size=PublicClass.getInstance().getWindowManeger();
    	if (size==null||size.length!=2) {
    		size=new int[]{1024,600};
		}	
		LinearLayout.LayoutParams params=new LayoutParams(size[0]/3, size[1]/3);
		View view =LinearLayout.inflate(TheApp.mApp, R.layout.layout_progress, null);
		mTextView=(TextView) view.findViewById(R.id.text_title);
		if (mFormatsdDialog==null) {
			mFormatsdDialog=new Dialog(TheApp.mApp,R.style.progress_dialog);
			mFormatsdDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
			mFormatsdDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mFormatsdDialog.setContentView(view,params);
			mFormatsdDialog.setCanceledOnTouchOutside(false);
			mFormatsdDialog.setCancelable(false);
		}
	}
	public void enableCanceledOnTouchOutside(boolean isenable){
		if (mFormatsdDialog!=null) {
			mFormatsdDialog.setCancelable(isenable);
		}
	}
	public void progressShow(int souceid){
		mSetuptime=60;
		if (mTextView!=null) {
			mTextView.setText(souceid);
		}
		if (mFormatsdDialog!=null) {
			mFormatsdDialog.dismiss();
			mFormatsdDialog.show();
			RunTask();
		}
	}
	
	public int getmSetuptime() {
		return mSetuptime;
	}
	public void setmSetuptime(int mSetuptime) {
		LogCatUtils.showString("===time=="+mSetuptime);
		this.mSetuptime = mSetuptime;
	}
	public void progressdismiss(){
		if (mFormatsdDialog!=null) {
			mFormatsdDialog.cancel();
			enableCanceledOnTouchOutside(false);
			stopTimer();
		}
	}
	private  MyTimerTask task;
	private  Timer timer;
	private int mCount;
	private void RunTask() {
		timer=new Timer(AppIsRun.class.toString());
		if (task!=null) {
			timer.cancel();
		}
		task=new MyTimerTask();
		timer.schedule(task, 100, 1000);
	}
	public void stopTimer() {
		if (task!=null) {
			task.cancel();
			task=null;
		}
		mCount=0;
	}
	class MyTimerTask extends TimerTask{
		@Override
		public void run() {
			mCount++;
			if (mCount==mSetuptime) {
				handler.sendEmptyMessage(0);
			}
		}
	}
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				TheApp.mApp.MyTosat(R.string.operation_failed);
				progressdismiss();
				break;
			case 1:
				
				break;
			default:
				break;
			}
		}
	};
}
