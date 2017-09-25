package com.syu.codec;

import com.syu.codec.Codec.InitCallback;
import com.syu.codec.Codec.TakePictureCallback;
import com.syu.dvr.utils.LogCatUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PreviewView extends SurfaceView implements SurfaceHolder.Callback, ICodec {
	Codec mCodec;
	int type = Codec.CODEC_TYPE_HDLV4;
	Callback cb;
	public interface Callback {
		public void onCreated(int type, SurfaceHolder holder);
		public void onDestroyed(int type);
	}
	
	public PreviewView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		getHolder().addCallback(this);
	}

	public PreviewView(Context context, AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);
	}

	public PreviewView(Context context) {
		super(context);
		getHolder().addCallback(this);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if(cb != null) {
			cb.onCreated(type, holder);
		}
		
		if(mCodec != null) {
			mCodec.setSurface(this);
			mCodec.start();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		if(mCodec != null) {
			mCodec.setPreviewRect(width, height);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if(mCodec != null) {
			mCodec.stop();
		}
		
		if(cb != null) {
			cb.onDestroyed(type);
		}
	}

	public void setType(int type) {
		this.type = type;
		if(type == Codec.CODEC_TYPE_MJPEG) {
			mCodec = new MjpegCodec();
		} else if(type == Codec.CODEC_TYPE_HDLV4) {
			mCodec = new HdlCodec();
		}
	}

	public void setCallback(Callback cb) {
		this.cb = cb;
	}
	
	@Override
	public void start() {
		if(mCodec != null) {
			mCodec.start();
		}
	}

	@Override
	public void pause() {
		if(mCodec != null) {
			mCodec.pause();
		}
	}

	@Override
	public void stop() {
		if(mCodec != null) {
			mCodec.stop();
		}
	}

	@Override
	public void takePicture(String path, int quality, TakePictureCallback cb) {
		if(mCodec != null) {
			mCodec.takePicture(path, quality, cb);
		}
	}

//	@Override
//	public void setup(int width, int height, int fps, int format) {
//		if(mCodec != null) {
//			mCodec.setup(width, height, fps, format);
//		}
//	}

	public void setInitCallback(InitCallback callback) {
		LogCatUtils.showString("  mcodec =null "+String.valueOf(mCodec==null));
		if(mCodec != null) {
			mCodec.setInitCallback(callback);
		}
	}
}
