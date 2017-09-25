package com.syu.dvr.utils;

import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.ViewGroup;

public class RecyclerViewUtils {
	
	/**
     * 存储宽度
     */
    static SparseIntArray mGvWidth = new SparseIntArray();
  
    /**
     * 计算GridView的高度
     * 
     * @param gridView 要计算的GridView
     */
    public static void updateGridViewLayoutParams(RecyclerView gridView, int maxColumn,int child) {
        int childs = child;
        
        
  
        if (childs > 0) {
            int columns = childs < maxColumn ? childs % maxColumn : maxColumn;
           
  
            ViewGroup.LayoutParams params = gridView.getLayoutParams();
            
            params.height=columns*(1024/3)+(columns-1)*10+10;
            gridView.setLayoutParams(params);
            
        }
        
    }
}
