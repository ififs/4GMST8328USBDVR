package com.syu.dvr.utils;

import android.widget.ImageView;

public class LoadThumbnails {
	private static LoadThumbnails thumbnails;
	private ImageLoader mImageLoader;
	public LoadThumbnails(){
		mImageLoader = ImageLoader.getInstance();
	}
	public static LoadThumbnails getInstance(){
		if (thumbnails==null) {
			synchronized (LoadThumbnails.class) {
				if (thumbnails==null) {
					thumbnails=new LoadThumbnails();
				}
			}
		}	
		return thumbnails;
	}
	public void loadImageThumbnails(final String path, final ImageView imageView,final String name){
		mImageLoader.loadImage(path,imageView,name);
	}

}
