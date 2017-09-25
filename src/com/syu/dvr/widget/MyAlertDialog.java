package com.syu.dvr.widget;

import com.syu.dvr.R;
import com.syu.dvr.utils.LogCatUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyAlertDialog extends AlertDialog{

	private MyAlertDialog mAlertDialog;
	private LinearLayout.LayoutParams mLayoutParams;
	private View mLayout;
	public MyAlertDialog(Context context) {
		super(context);
		mAlertDialog=this;
		mAlertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		mAlertDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_view);
		mLayoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 
				250);
		LayoutInflater inflater=LayoutInflater.from(context);
		mLayout=inflater.inflate(R.layout.storage_space_layout, null);
	}
	public void setData(int souceid){
		if (mAlertDialog==null) {
			return;
		}
		if (mAlertDialog.isShowing()) {
			mAlertDialog.dismiss();
		}
		mAlertDialog.show();
		Window window = mAlertDialog.getWindow();
		window.setContentView(mLayout, mLayoutParams);
		TextView view=(TextView) mAlertDialog.findViewById(R.id.title);
		view.setText(souceid);
		Button button=(Button) mAlertDialog.findViewById(R.id.view_hide);
		button.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (mAlertDialog.isShowing()) {
					
					mAlertDialog.dismiss();
				}
				return false;
			}
		});
	}

}
