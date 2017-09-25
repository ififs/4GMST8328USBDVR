package com.syu.dvr.utils;

import com.syu.dvr.widget.MGridView;

import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;

public class GridViewUtils {
	
	
    static SparseIntArray mGvWidth = new SparseIntArray();
  
    
    public static void updateGridViewLayoutParams(MGridView gridView, int maxColumn) {
        int childs = gridView.getAdapter().getCount();
  
        if (childs > 0) {
        	int columns=childs < maxColumn ? childs % maxColumn : maxColumn;
            gridView.setNumColumns(columns);
            int width = 0;
            int cacheWidth = mGvWidth.get(columns);
            if (cacheWidth != 0) {
                width = cacheWidth;
            } else { 
                int rowCounts = childs < maxColumn ? childs : maxColumn;
                for (int i = 0; i < rowCounts; i++) {
                    View childView = gridView.getAdapter().getView(i, null, gridView);
                    childView.measure(0, 0);
                    width += childView.getMeasuredWidth();
                }
            }
  
            ViewGroup.LayoutParams params = gridView.getLayoutParams();
            params.width = width;
            gridView.setLayoutParams(params);
            if (mGvWidth.get(columns) == 0) {
                mGvWidth.append(columns, width);
            }
           
        }
        
    }
}
