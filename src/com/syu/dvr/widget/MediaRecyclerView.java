package com.syu.dvr.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View.MeasureSpec;

public class MediaRecyclerView extends RecyclerView{
	 public boolean hasScrollBar = true;
	public MediaRecyclerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	public MediaRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}
	public MediaRecyclerView(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
	}
	 protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	        int expandSpec = heightMeasureSpec;
	        if (hasScrollBar) {
	            expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
	                    MeasureSpec.AT_MOST);
	            super.onMeasure(widthMeasureSpec, expandSpec);// 注意这里,这里的意思是直接测量出GridView的高度
	        } else {
	            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	        }
	    }
	    
	 @Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
	}   
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction()==MotionEvent.ACTION_MOVE) {
			return true;
		}
		return super.dispatchTouchEvent(ev);
	}
}
