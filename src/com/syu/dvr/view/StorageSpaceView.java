package com.syu.dvr.view;

import com.syu.dvr.R;
import com.syu.dvr.TheApp;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
public class StorageSpaceView extends View implements OnClickListener{
	
	private View remoteView = null;
	private WindowManager mWindowManager = null;
	private LayoutParams mLayoutParams = null;
	public boolean isLoad=false;
	public StorageSpaceView(Context context) {
		super(context);
		mWindowManager = (WindowManager) TheApp.mApp
				.getSystemService(Context.WINDOW_SERVICE);
		mLayoutParams = new LayoutParams();
		mLayoutParams.type = LayoutParams.TYPE_TOAST;
		mLayoutParams.format = PixelFormat.RGBA_8888;
		mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
		mLayoutParams.x = 200;
		mLayoutParams.y = 80;
		mLayoutParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
				| LayoutParams.FLAG_NOT_FOCUSABLE;
		mLayoutParams.width =350;
		mLayoutParams.height =300;
		
	}
	public void addView(int title) {
	
	LayoutInflater fInflater=LayoutInflater.from(TheApp.mApp);
		remoteView=fInflater.inflate(R.layout.storage_space_layout, null);
		if(remoteView.getParent() == null) {
			
			mWindowManager.addView(remoteView, mLayoutParams);
			isLoad=true;
		}
		
		TextView view=(TextView) remoteView.findViewById(R.id.title);
		view.setText(title);
		Button button=(Button) remoteView.findViewById(R.id.view_hide);
		initListener();
		button.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				remoview();
			}
		});
	}
	public void remoview(){
		if (remoteView.getParent() != null) {
			mWindowManager.removeView(remoteView);
			isLoad=false;
		}
	}
	
	private void initListener() {
		this.setOnClickListener(this);
		remoteView.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				remoview();
				return false;
			}
		});
		remoteView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				remoview();
				return false;
			}
		});
		
	}
	@Override
	public void onClick(View arg0) {
		
		remoview();
	}
	
}
