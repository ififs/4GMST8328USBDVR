package com.syu.dvr.widget;

import com.syu.dvr.R;
import com.syu.dvr.utils.LogCatUtils;

import android.R.integer;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

public class SettingSwitch extends LinearLayout{
	
	private TextView mTittle;
	private String text ="";
	private Button mButtons[];
	private boolean mDirive;
	private LinearLayout layout;
	private LinearLayout.LayoutParams params;
	public SettingSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.setting_attrs);
		text= typedArray.getString(R.styleable.setting_attrs_text);
		mDirive=typedArray.getBoolean(R.styleable.setting_attrs_isdirive, true);
		initLayout(context);
	}
	public SettingSwitch(Context context, AttributeSet attrs) {
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
		View inflater=LayoutInflater.from(mContext).inflate(R.layout.item_switch, null);
		params.weight=1;
		layout.addView(inflater, params);
		mButtons=new Button[2];
		mButtons[0]=(Button) inflater.findViewById(R.id.item_on);
		mButtons[1]=(Button) inflater.findViewById(R.id.item_off);
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
	public void updataLayout(boolean on){
		LogCatUtils.showString("===on==="+String.valueOf(on));
		if (mButtons==null||mButtons.length<=1) {
			return;
		}
		if (on) {
			mButtons[0].setBackgroundResource(R.color.transparent);
			mButtons[1].setBackgroundResource(R.drawable.off_p);
		}else {
			mButtons[1].setBackgroundResource(R.color.transparent);
			mButtons[0].setBackgroundResource(R.drawable.off_p);
		}
	}
	public Button[] getButtons(){
		return mButtons;
	}
}
	