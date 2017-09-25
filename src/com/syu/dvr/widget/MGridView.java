package com.syu.dvr.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

public class MGridView extends GridView{
	 public boolean hasScrollBar = true;
	 public boolean isOnMeasure;
	  
	    /**
	     * @param context
	     */
	    public MGridView(Context context) {
	        this(context, null);
	    }
	  
	    public MGridView(Context context, AttributeSet attrs) {
	        super(context, attrs, 0);
	    }
	  
	    public MGridView(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	    }
	  
	    @Override
	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    	isOnMeasure=true;
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
		isOnMeasure=false;
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
