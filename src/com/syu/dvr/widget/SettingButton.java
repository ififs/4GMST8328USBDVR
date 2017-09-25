package com.syu.dvr.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.syu.dvr.R;

public class SettingButton extends LinearLayout{
	
	private TextView mTittle;
	private String text ="";
	private boolean mDirive;
	private LinearLayout layout;
	private Button button;
	private LinearLayout.LayoutParams params;
	public SettingButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.setting_attrs);
		text= typedArray.getString(R.styleable.setting_attrs_text);
		mDirive=typedArray.getBoolean(R.styleable.setting_attrs_isdirive, true);
		initLayout(context);
	}
	public SettingButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	private void initLayout(Context context){
		setOrientation(LinearLayout.VERTICAL);
		layout=new LinearLayout(context);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setGravity(Gravity.CENTER_VERTICAL);
		mTittle=new TextView(context);
		params=new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		addView(layout, params);
		mTittle.setTextSize(20);
		params.weight=1;
		params.leftMargin=20;
		mTittle.setText(text);
		
		layout.addView(mTittle,params);
		params=new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		LinearLayout inflater=new LinearLayout(mContext);
		button=new Button(mContext);
		button.setBackgroundResource(R.drawable.bg_guide_button);
		inflater.addView(button);
		params.weight=1;
		layout.addView(inflater, params);
		if (mDirive) {
			View diri=new View(context);
			diri.setBackgroundResource(R.drawable.divide_view);
			params=new LinearLayout.LayoutParams
					(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			params.topMargin=10;
			params.leftMargin=5;
			params.rightMargin=5;
			addView(diri, params);
		}
	}
	
	public Button getButton(){
		return button;
	}
}
	