package com.syu.codec;

import java.util.ArrayDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.syu.dvr.utils.LogCatUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.os.SystemClock;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MjpegCodec extends Codec {
	Paint paint;
	PaintFlagsDrawFilter filter;
	
	Options opt;
	SurfaceHolder mHolder;
	DrawThread thread = null;
	BitmapSparseArray mCache = new BitmapSparseArray();

	AtomicLong index = new AtomicLong(0);
//	private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	private static final int CORE_POOL_SIZE = BitmapSparseArray.CACHE_SIZE;
	private static final int MAXIMUM_POOL_SIZE =  CORE_POOL_SIZE + 2;
	private static final int KEEP_ALIVE = 1;

	private static final ThreadFactory sThreadFactory = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		public Thread newThread(Runnable r) {
			return new Thread(r, "Decodec bitmap #" + mCount.getAndIncrement());
		}
	};

	private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(32);

	public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
			KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);

	public SerialExecutor mExecutor = new SerialExecutor();
	ExecutorService picExecutor = Executors.newSingleThreadExecutor(sThreadFactory);

	private static class SerialExecutor implements Executor {
		final ArrayDeque<Runnable> mTasks = new ArrayDeque<Runnable>();
		Runnable mActive;

		public synchronized void execute(final Runnable r) {
			mTasks.offer(new Runnable() {
				public void run() {
					try {
						r.run();
					} finally {
						scheduleNext();
					}
				}
			});
			if (mActive == null) {
				scheduleNext();
			}
		}

		protected synchronized void scheduleNext() {
			if ((mActive = mTasks.poll()) != null ) {
				THREAD_POOL_EXECUTOR.execute(mActive);
			}
		}
	}

	//绘制线程
	class DrawThread extends Thread {
		@Override
		public void run() {
			lastTime = SystemClock.elapsedRealtime();
			while (running.get()) {
				long drawtime = SystemClock.elapsedRealtime();
				long dtime = paused ? 0 : drawtime - lastTime;
//				Log.e("L", "======>>>>> 0 paused : " + paused + " dtime >= (1000 / MAX_FPS) == " + (dtime >= (1000 / MAX_FPS)) + " mCache.size() == " + mCache.size());
				//以极限每秒MAX_FPS绘制，当设备绘制帧数低于MAX_FPS时以设备实际帧数为准，当设备实际帧数超过MAX_FPS帧数实际绘制以MAX_FPS为准。
				if (!paused && dtime >= (1000 / MAX_FPS)) {
					
					if (/*running.get() && */mCache.size() <= BitmapSparseArray.CACHE_SIZE) {
						mExecutor.execute(new DecodecTask());
					}
					lastTime = drawtime;
					doDraw();
				} else {
					try {
						Thread.sleep(1000 / MAX_FPS - dtime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}
	}

	//解析图片
	class DecodecTask implements Runnable {
		@Override
		public void run() {
			byte[] data = Utils.getBuff();
			if (running.get() && data != null && data.length > 0) {
				Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opt);
				if(bitmap != null){
					if(running.get()){
						mCache.put(bitmap);
					} else {
						bitmap.recycle();
					}
				}
			}
		}
	}

	//拍照
	class TakePicture implements Runnable {
		Bitmap bitmap;
		
		public TakePicture(Bitmap bitmap) {
			this.bitmap = bitmap;
		}
		
		@Override
		public void run() {
			takePicture(bitmap);
			bitmap.recycle();
		}
	}
	
	public MjpegCodec() {
		super();
		opt = new Options();
//		opt.inPreferredConfig = Config.RGB_565;
		opt.inPremultiplied = false;
		opt.inPreferQualityOverSpeed = false;
		
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//		filter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG    
//	            | Paint.FILTER_BITMAP_FLAG);
	}
	
	public void doDraw() {
		synchronized (this) {
			Bitmap bitmap = mCache.next();
			if(running.get() && bitmap != null) {
				Canvas canvas = mHolder.lockCanvas();
		       if(canvas != null) {
		    	   canvas.drawBitmap(bitmap, 0, 0, paint);
		    	   mHolder.unlockCanvasAndPost(canvas);
		       }
				if(photo.get()) {
					picExecutor.execute(new TakePicture(bitmap));
					photo.set(false);
				} else {
					bitmap.recycle();
				}
			}
		}
	}
	public void setInitCallback(InitCallback callback) {
		this.callback = callback;
	}
	
	@Override
	public void start() {
		boolean lastRuned = running.get();
		boolean inited = lastRuned;
		if(!inited) {
			if(thread != null) {
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				thread = null;
			}
//			int ret = Utils.open(deviceId);
//			if(ret >= 0) {
//				if(Utils.setup(videoWidth, videoHeight, fps, format) >= 0) {
//					inited = true;
//				}
//			}
			inited = Utils.inited;
			LogCatUtils.showString("inited =null "+String.valueOf(inited));
		}
		LogCatUtils.showString("inited =null "+String.valueOf(inited));
		if(inited && paused) {
			if(Utils.startPreview() >= 0) {
				inited = true;
				paused = false;
				LogCatUtils.showString("inited =null "+String.valueOf(inited));
				if(thread == null) {
					running.set(true);
					thread = new DrawThread();
					thread.start();
				}
			} else {
				inited = false;
				LogCatUtils.showString("inited =null "+String.valueOf(inited));
			}
		}
		
		if(!lastRuned && callback != null) {
			final boolean result = inited;
			uiHandler.post(new Runnable() {
				@Override
				public void run() {
					callback.onCallback(result);
				}
			});
		}
	}

	@Override
	public void pause() {
		if(running.get() && !paused) {
			Utils.stopPreview();
			paused = true;
		}
	}

	@Override
	public void stop() {
		if(running.get()) {
			running.set(false);
			paused = true;
			Utils.stopPreview();
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			mCache.clear();
			thread = null;
		}
		lastTime = 0;
	}

	@Override
	public void setSurface(SurfaceView surfaceView) {
		if(surfaceView != null) {
			SurfaceHolder holder = surfaceView.getHolder();
			if(holder != null) {
				mHolder = holder;
			}
		}
	}
}
