package com.syu.dvr.widget;

import java.io.File;

import com.syu.dvr.R;
import com.syu.dvr.TheApp;
import com.syu.dvr.upgrade.UpgradeManager;
import com.syu.dvr.utils.LogCatUtils;
import com.syu.dvr.utils.PublicClass;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.telecom.Conference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DownLoadLayout extends LinearLayout{
	private LinearLayout layout;
	private ProgressBar mProgressBar;
	private TextView mProgressPercentage;
	private long contentLength;
	private int progress;
	private Context mContext;
	private Dialog mDialog;
	public DownLoadLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext=context;
		LayoutInflater inflater=LayoutInflater.from(context);
		layout=(LinearLayout) inflater.inflate(R.layout.down_loading_layout, null);
		addView(layout);
		initDownLoadLayout();
	}
	private void initDownLoadLayout() {
		mProgressBar=(ProgressBar) layout.findViewById(R.id.progressBar);
		mProgressPercentage=(TextView) layout.findViewById(R.id.downLoadprogress);
	}
	
	public void downLoadSuccess(boolean b, int type, String mFile) {
		if (b&&type==0&&!mFile.isEmpty()) {
			progress=100;
			handler.sendEmptyMessage(1);
			PublicClass.getInstance().openFile(new File(mFile));
		}else if (!b&&type==-1) {
			handler.sendEmptyMessage(2);
		}
	}
	
	public void setContentLength(long contentLength) {
		this.contentLength=contentLength;
	}
	public void setmDownLoadProgressBar(long count) {
		 LogCatUtils.showString("进度===="+count);
		this.progress=(int) (count*100/contentLength);
		handler.sendEmptyMessage(1);
	}
	public void setDialog(Dialog dialog){
		this.mDialog=dialog;
	}
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				mProgressBar.setProgress(progress);
				if (progress<=100) {
					mProgressPercentage.setText(progress+"%");
					if (progress==100) {
						if (mDialog!=null) {
							mDialog.dismiss();
						}
					}
				}
				break;
			case 2:
				TheApp.mApp.MyTosat("网络异常下载失败");
				if (mDialog!=null) {
					mDialog.dismiss();
				}
				break;
			default:
				break;
			}
			
		}
	};
}
