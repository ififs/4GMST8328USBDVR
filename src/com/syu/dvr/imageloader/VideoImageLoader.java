package com.syu.dvr.imageloader;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.syu.dvr.utils.LogCatUtils;
import com.syu.dvr.widget.ComInterface.ImageCache;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore.Images.Thumbnails;
import android.widget.ImageView;

public class VideoImageLoader {

	ImageCache mImageCache=new DiskCache();
	private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	public void displayImage(String path,ImageView imageView){
		if (path.isEmpty()) {
			return;
		}
		Bitmap bitmap=mImageCache.get(path);
		if (bitmap!=null) {
			imageView.setImageBitmap(bitmap);
			return;
		}
		submitLoadRequest(path,imageView);
	}
	private void submitLoadRequest(final String path, final ImageView imageView) {
		imageView.setTag(path);
		executorService.submit(new  Runnable() {
			public void run() {
				Bitmap bitmap=downLoadImage(path);
				if (bitmap==null) {
					return;
				}
				if (imageView.getTag().equals(path)) {
					imageView.setImageBitmap(bitmap);
				}
				mImageCache.put(path, bitmap);
			}

		});
		
	}
	private Bitmap downLoadImage(String path) {
		
		File file=new File(path);
		if (!file.exists()) {
			return null;
		}
		Bitmap bitmap=getVideoBitmap(file.getAbsolutePath());
		if (bitmap==null) {
			return null;
		}
		
		return bitmap;
	}
	public Bitmap getVideoBitmap(String path) {
		
		Bitmap bitmap = null;
		
		try {
			bitmap = ThumbnailUtils.createVideoThumbnail(path,
					Thumbnails.MICRO_KIND);
			bitmap = ThumbnailUtils.extractThumbnail(bitmap, 80, 80,
					ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		} catch (Exception e) {
			bitmap=null;
		}catch (OutOfMemoryError e) {
			
		}
		if (bitmap!=null) {
			
			
		}
		return bitmap;
	}
}
