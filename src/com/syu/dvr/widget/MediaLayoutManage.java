package com.syu.dvr.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView.Recycler;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

public class MediaLayoutManage extends GridLayoutManager{

	public MediaLayoutManage(Context context, int spanCount) {
		super(context, spanCount);
		
	}
	
	@Override
	public void onMeasure(Recycler recycler, State state, int widthSpec, int heightSpec) {
		int height = 0;
        int childCount = getItemCount();
        for (int i = 0; i < childCount; i++) {
            View child = recycler.getViewForPosition(i);
            measureChild(child, widthSpec, heightSpec);
            if (i % getSpanCount() == 0) {
                int measuredHeight = child.getMeasuredHeight() + getDecoratedBottom(child);
                height += measuredHeight;
            }
        }
        setMeasuredDimension(View.MeasureSpec.getSize(widthSpec), height);
	}

}
