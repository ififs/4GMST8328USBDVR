package com.syu.dvr.widget;

import com.syu.dvr.R;
import com.syu.dvr.utils.LogCatUtils;

import android.R.integer;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingLayout extends LinearLayout{
	
	private ImageView imageView ;
	private TextView mTittle;
	private TypedArray names;
	private String text ="";
	private Button mButtons[];
	private View view;
	private LinearLayout layout;
	private LinearLayout.LayoutParams params;
	public SettingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.setting_attrs);
		text= typedArray.getString(R.styleable.setting_attrs_text);
		int NameID=typedArray.getResourceId(R.styleable.setting_attrs_settingName, -1);
		if (NameID!=-1) {
			names=getResources().obtainTypedArray(NameID);
		}
		initLayout(context,names);
	}
	public SettingLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	private void initLayout(Context context, TypedArray names){
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
		
		
		View diri=new View(context);
		diri.setBackgroundResource(R.drawable.divide_view);
		params=new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.topMargin=10;
		params.leftMargin=5;
		params.rightMargin=5;
		addView(diri, params);
		if (names==null||names.length()<=1) {
			return;
		}
		LinearLayout mNamelayout=new LinearLayout(context);
		mNamelayout.setOrientation(LinearLayout.HORIZONTAL);
		mNamelayout.setGravity(Gravity.CENTER_VERTICAL);
		params=new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.weight=1;
		layout.addView(mNamelayout, params);
		mButtons=new Button[names.length()];
		params=new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		for (int i = 0; i < names.length(); i++) {
			Button button=new Button(context);
			button.setText(names.getString(i));
			button.setTextSize(20);
			if (i==0) {
				button.setBackgroundResource(R.drawable.choose_left_def);
			}else if (i==names.length()-1) {
				button.setBackgroundResource(R.drawable.choose_right_def);
			}else {
				button.setBackgroundResource(R.drawable.choose_middle_def);
			}
			mButtons[i]=button;
			mNamelayout.addView(button, params);
		}
		
	}
	public void updataLayout(int bk){
		if (mButtons==null||mButtons.length<=bk||bk<0) {
			return;
		}
		for (int i = 0; i < mButtons.length; i++) {
			if (i==0) {
				mButtons[i].setBackgroundResource(R.drawable.choose_left_def);
			}else if (i==names.length()-1) {
				mButtons[i].setBackgroundResource(R.drawable.choose_right_def);
			}else {
				mButtons[i].setBackgroundResource(R.drawable.choose_middle_def);
			}
		}
		if (bk==0) {
			mButtons[bk].setBackgroundResource(R.drawable.choose_left_en);
		}else if (bk==mButtons.length-1) {
			mButtons[bk].setBackgroundResource(R.drawable.choose_right_en);
		}else {
			mButtons[bk].setBackgroundResource(R.drawable.choose_middle_en);
		}
	}
	public Button[] getButtons(){
		return mButtons;
	}
}
	