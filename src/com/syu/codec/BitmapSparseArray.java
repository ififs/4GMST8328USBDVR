package com.syu.codec;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;

public class BitmapSparseArray extends SparseArray<Bitmap> {
	public static final int CACHE_SIZE = 2;
	int next = 0;
	int index = 0;
	
	public BitmapSparseArray() {
		super(CACHE_SIZE);
	}
	
	public synchronized void put(Bitmap bitmap) {
//		Log.e("DVR", " put index = " + index + " next = " + next);
		if(size() >= CACHE_SIZE) {
			bitmap.recycle();
			return;
		}
		put(index, bitmap);
		if(index == Integer.MAX_VALUE) {
			index = 0;
		} else {
			index++;
		}	
	}
	
	public synchronized Bitmap next() {
//		Log.e("DVR", " next index = " + index + " next = " + next);
		if(index - next > CACHE_SIZE) {
			next = index - CACHE_SIZE;
		}
		
		if(indexOfKey(next) >= 0) {
			Bitmap bitmap = get(next);
			remove(next);
			if(next == Integer.MAX_VALUE) {
				next = 0;
			} else {
				next++;
			}
			return bitmap;
		}
		return null;
	}
}
