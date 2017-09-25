package com.syu.dvr.view;

import com.syu.dvr.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class StatuspromptLayout extends LinearLayout{
	private ImageView connectImage;
	private LinearLayout.LayoutParams params;

	public StatuspromptLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(LinearLayout.HORIZONTAL);
		params=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		connectImage=new ImageView(context);
		connectImage.setBackgroundResource(R.drawable.ic_launcher);
		addView(connectImage, params);
	}

}
