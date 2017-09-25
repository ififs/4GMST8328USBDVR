package com.syu.dvr.widget;

import com.syu.dvr.R;
import com.syu.dvr.utils.LogCatUtils;

import android.R.integer;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingLayout3 extends LinearLayout{
	
	private ImageView imageView ;
	private TextView textView;
	private String text ="";
	private LinearLayout.LayoutParams params;
	public SettingLayout3(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.setting_attrs);
		text= typedArray.getString(R.styleable.setting_attrs_text);
		initLayout(context);
	}
	public SettingLayout3(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		
	}
	private void initLayout(Context context){
		setOrientation(LinearLayout.HORIZONTAL);
		setGravity(Gravity.CENTER_VERTICAL);
		textView=new TextView(context);
		imageView=new ImageView(context);
		params=new LayoutParams(20,20);
		imageView.setBackgroundResource(R.drawable.nomarl);
		addView(imageView, params);
		
		params=new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		textView.setTextSize(20);
		params.leftMargin=60;
		textView.setText(text);
		addView(textView,params);
	}
	public void setBackgrounds(int souceID){
		if (imageView!=null) {
			imageView.setBackgroundResource(souceID);
		}
	}
}
	