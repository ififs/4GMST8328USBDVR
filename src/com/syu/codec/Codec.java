package com.syu.codec;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.syu.dvr.utils.LogCatUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public abstract class Codec implements ICodec {
	public static final int CODEC_TYPE_HDLV4 = 0;
	public static final int CODEC_TYPE_MJPEG = 1;
	
	final static int MAX_FPS = 60; 
//	static int deviceId = -1;
//	static int videoWidth, videoHeight, fps, format;
	boolean inited = false;
	boolean paused = true;
	long lastTime = 0;
	
	int previewWidth , previewHeight;
	int quality;
	String photoPath;
	
	protected AtomicInteger count = new AtomicInteger(1);
	protected AtomicBoolean running = new AtomicBoolean(false);
	protected AtomicBoolean photo = new AtomicBoolean(false);
	
	protected Surface mSurface;
	Handler uiHandler;
	InitCallback callback;
	TakePictureCallback pictureCallback;
	
	public interface InitCallback {
		void onCallback(boolean result);
	}
	
	public interface TakePictureCallback {
		void onResult(boolean success, String path);
	}
	
	public Codec() {
		uiHandler = new Handler(Looper.getMainLooper());
	}
	
	public void setSurface(SurfaceView surfaceView) {
		if(surfaceView != null) {
			SurfaceHolder holder = surfaceView.getHolder();
			if(holder != null) {
				this.mSurface = holder.getSurface();
			}
		}
	}
	
/*	final public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}*/
	
/*	public void setup(int width, int height, int sfps, int sformat) {
		videoWidth = width;
		videoHeight = height;
		fps = sfps;
		format = sformat;
	}*/
	
	void setPreviewRect(int w, int h) {
		previewWidth = w;
		previewHeight = h;
	}

	@Override
	public void takePicture(String path, int quality, TakePictureCallback cb) {
		if(path != null && (path.endsWith(".png") || path.endsWith(".jpg")) && quality > 0 && quality <= 100) {
			Log.e("photo", "=======>0000");
			this.photoPath = path;
			this.quality = quality;
			photo.set(true);
		}
		pictureCallback = cb;
	}
	
	public void setInitCallback(InitCallback callback) {
		this.callback = callback;
	}
	void takePicture(Bitmap bitmap) {
		OutputStream os = null;
		File file = null;
		boolean result = false;
		try {
			file = new File(photoPath);
			if(file.exists()) {
				file.delete();
			}
			os = new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, quality, os);
			os.flush();
			result = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			file = null;
			if(pictureCallback != null) {
				pictureCallback.onResult(result, result ? photoPath : null);
			}
		}
	}
}
