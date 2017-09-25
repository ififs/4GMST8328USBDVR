package com.syu.codec;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.syu.dvr.utils.LogCatUtils;

import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceView;

public class HdlCodec extends Codec {
	public static final int CODEC_START = 0;
	public static final int CODEC_PAUSE = 1;
	public static final int TAKE_PICTURE = 2;
	public static final int CODEC_STOP = 3;

	private static final int COLOR_FormatI420 = 1;
	private static final int COLOR_FormatNV21 = 2;
	
	AtomicInteger count = new AtomicInteger(1);
	AtomicBoolean running = new AtomicBoolean(false);

	H mH;
	CodecThread thread = null;
	MediaCodec mCodec;
	MediaFormat mFormat = null;
	SurfaceView surfaceView;

	private static final ThreadFactory sThreadFactory = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		public Thread newThread(Runnable r) {
			return new Thread(r, "Decodec bitmap #" + mCount.getAndIncrement());
		}
	};

	//ÅÄÕÕ
	class JTakePicture implements Runnable {
		Image image;
		public JTakePicture(Image image) {
			this.image = image;
		}
		
		@Override
		public void run() {
			final boolean result;
			if(image != null) {
				result = compressToJpeg(photoPath, image);
				image.close();
			} else {
				result = false;
			}
			
			if(pictureCallback != null) {
				uiHandler.post(new Runnable() {
					@Override
					public void run() {
						pictureCallback.onResult(result, result ? photoPath : null);
						pictureCallback = null;
						start();
					}
				});
			}
		}
	}
	
// ÅÄÕÕ
	class TakePicture implements Runnable {
		MediaCodec codec;
		boolean working = false;
		long ltime = 0;

		public TakePicture() {
			try {
				codec = MediaCodec.createDecoderByType("video/avc");
				MediaFormat mediaFormat = MediaFormat.createVideoFormat("video/avc", Utils.videoWidth, Utils.videoHeight);
				mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, Utils.fps);
				codec.configure(mediaFormat, null, null, 0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			working = true;
			BufferInfo info = new BufferInfo();
			ltime = SystemClock.elapsedRealtime();
			codec.start();
			while (working) {
				long drawtime = SystemClock.elapsedRealtime();
				long dtime = drawtime - ltime;
				if (dtime == 0) {
					try {
						Thread.sleep(1000 / MAX_FPS - dtime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					int ii = codec.dequeueInputBuffer(10000);
					if (ii >= 0) {
						ByteBuffer buffer = codec.getInputBuffer(ii);
						int sampleSize = (int) Utils.getBuffer(buffer);
						int flag = sampleSize < 0 ? MediaCodec.BUFFER_FLAG_END_OF_STREAM : 0;
						if (sampleSize > 0) {
							codec.queueInputBuffer(ii, 0, sampleSize, 0, flag);
						}
					}

					int oi = codec.dequeueOutputBuffer(info, 10000);
					if (oi >= 0) {
						 boolean doRender = (info.size != 0);
						if (doRender) {
							Image img = codec.getOutputImage(oi);
							final boolean result = compressToJpeg(photoPath, img);
							img.close();
							working = false;
							if(pictureCallback != null) {
								uiHandler.post(new Runnable() {
									@Override
									public void run() {
										pictureCallback.onResult(result, result ? photoPath : null);
										pictureCallback = null;
										start();
									}
								});
							}
						}
						codec.releaseOutputBuffer(oi, 10000);
					} else if (oi == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
//						mFormat = codec.getOutputFormat();
					}
				}
			}
			
			if (codec != null) {
				codec.stop();
				codec.release();
				codec = null;
			}
		}
	}

	ExecutorService picExecutor = Executors.newSingleThreadExecutor(sThreadFactory);

	public HdlCodec() {
		super();
		HandlerThread work = new HandlerThread("Codec Thread");
		work.start();

		mH = new H(work.getLooper());
	}

	@Override
	public void start() {
		handleEvent(CODEC_START);
	}

	@Override
	public void pause() {
		handleEvent(CODEC_PAUSE);
	}

	@Override
	public void stop() {
		handleEvent(CODEC_STOP);
	}

	@Override
	public void takePicture(String path, int quality, TakePictureCallback cb) {
		super.takePicture(path, quality, cb);
		// handleEvent(TAKE_PICTURE);
		if (photo.get()) {
			stop();
			picExecutor.execute(new TakePicture()); 
		}
	}

	@Override
	public void setSurface(SurfaceView surfaceView) {
		super.setSurface(surfaceView);
		this.surfaceView = surfaceView;
	}

	public void prepare() {
		if (mCodec != null)
			return;
		try {
			mCodec = MediaCodec.createDecoderByType("video/avc");
			MediaFormat mediaFormat = MediaFormat.createVideoFormat("video/avc", Utils.videoWidth, Utils.videoHeight);
			mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, Utils.fps);
			Log.e("DVR", "Buffer Test ===>>>> " + mediaFormat.toString());
			mCodec.configure(mediaFormat, mSurface, null, 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.e("DVR", "prepare  mCodec ===>>>> " + mCodec);
	}

	public class CodecThread extends Thread {
		AtomicInteger num = new AtomicInteger(1);

		public CodecThread() {
			setName(getClass().getSimpleName() + "#" + num.getAndIncrement());
		}

		@Override
		public synchronized void start() {
			running.set(true);
			super.start();

		}

		public synchronized void joinStop() {
			running.set(false);
			try {
				join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			super.run();
			BufferInfo info = new BufferInfo();
			MediaCodec codec = mCodec;
			lastTime = SystemClock.elapsedRealtime();
			while (running.get()) {
				long drawtime = SystemClock.elapsedRealtime();
				long dtime = paused ? 0 : drawtime - lastTime;
				if (dtime == 0) {
					try {
						Thread.sleep(1000 / MAX_FPS - dtime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					int ii = mCodec.dequeueInputBuffer(10000);
					if (ii >= 0) {
						ByteBuffer buffer = codec.getInputBuffer(ii);
						int sampleSize = (int) Utils.getBuffer(buffer);
						int flag = sampleSize < 0 ? MediaCodec.BUFFER_FLAG_END_OF_STREAM : 0;
						if (running.get() && sampleSize > 0) {
							codec.queueInputBuffer(ii, 0, sampleSize, 0, flag);
						}
					}

					if (running.get()) {
						int oi = mCodec.dequeueOutputBuffer(info, 10000);
						if (oi >= 0) {
							mCodec.releaseOutputBuffer(oi, 10000);
						} else if (oi == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
							mFormat = mCodec.getOutputFormat();
						}
					}
				}
			}
		}
	}

	void handleEvent(int event) {
		if (mH == null)
			return;
		Message msg = mH.obtainMessage(event);
		msg.sendToTarget();
	}

	public class H extends Handler {
		public H() {
			super();
		}

		public H(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int what = msg.what;
			switch (what) {
			case CODEC_START:
				boolean lastRuned = running.get();
				boolean inited = lastRuned;
				boolean takePhoto = /*inited &&*/ photo.get();
				LogCatUtils.showString("inited =null "+String.valueOf(inited));
				if (!inited) {
					if (thread != null) {
						try {
							thread.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						thread = null;
					}
					inited = Utils.inited;
					LogCatUtils.showString("inited =null "+String.valueOf(inited));
				}
				LogCatUtils.showString("inited =null "+String.valueOf(inited));
				if (inited) {
					prepare();
					if (paused) {
						boolean preview = true;
						if(photo.get()) {
							photo.set(false);
						} else {
							preview = Utils.startPreview() >= 0;
						}
						
						if (preview) {
							inited = true;
							paused = false;
							LogCatUtils.showString("inited =null "+String.valueOf(inited));
							if (thread == null) {
								running.set(true);
								mCodec.start();
								thread = new CodecThread();
								thread.start();
							}
						} else {
							inited = false;
							LogCatUtils.showString("inited =null "+String.valueOf(inited));
						}
					}
				}

				if (!lastRuned && !takePhoto && callback != null) {
					final boolean result = inited;
					uiHandler.post(new Runnable() {
						@Override
						public void run() {
							callback.onCallback(result);
						}
					});
				}
				break;

			case CODEC_PAUSE:
				if (running.get() && !paused) {
					Utils.stopPreview();
					paused = true;
				}
				break;

			case TAKE_PICTURE:
				
				break;

			case CODEC_STOP:
				if (running.get()) {
					running.set(false);
					paused = true;
					if(!photo.get()) {
						Utils.stopPreview();
					}
					try {
						thread.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					thread = null;

					if (mCodec != null) {
						mCodec.stop();
						mCodec.release();
						mCodec = null;
					}
				}
				lastTime = 0;
				break;

			default:
				break;
			}
		}
	}

	
	private boolean compressToJpeg(String fileName, Image image) {
		boolean result = false;
        FileOutputStream outStream;
        try {
            outStream = new FileOutputStream(fileName);
        } catch (IOException ioe) {
            throw new RuntimeException("Unable to create output file " + fileName, ioe);
        }
        
       try{
    	   if(outStream != null) {
	        	 Rect rect = image.getCropRect();
	 	        YuvImage yuvImage = new YuvImage(getDataFromImage(image, COLOR_FormatNV21), ImageFormat.NV21, rect.width(), rect.height(), null);
	 	        yuvImage.compressToJpeg(rect, quality, outStream);
	 	        result = true;
	        }
       }catch(Exception e){
    	   e.printStackTrace();
       }
        return result;
    }
    
    private  boolean isImageFormatSupported(Image image) {
        int format = image.getFormat();
        switch (format) {
            case ImageFormat.YUV_420_888:
            case ImageFormat.NV21:
            case ImageFormat.YV12:
                return true;
        }
        return false;
    }
    
    private  byte[] getDataFromImage(Image image, int colorFormat) {
        if (colorFormat != COLOR_FormatI420 && colorFormat != COLOR_FormatNV21) {
            throw new IllegalArgumentException("only support COLOR_FormatI420 " + "and COLOR_FormatNV21");
        }
        if (!isImageFormatSupported(image)) {
            throw new RuntimeException("can't convert Image to byte array, format " + image.getFormat());
        }
        Rect crop = image.getCropRect();
        int format = image.getFormat();
        int width = crop.width();
        int height = crop.height();
        Image.Plane[] planes = image.getPlanes();
        byte[] data = new byte[width * height * ImageFormat.getBitsPerPixel(format) / 8];
        byte[] rowData = new byte[planes[0].getRowStride()];
        if (true) Log.v("photo", "get data from " + planes.length + " planes");
        int channelOffset = 0;
        int outputStride = 1;
        for (int i = 0; i < planes.length; i++) {
            switch (i) {
                case 0:
                    channelOffset = 0;
                    outputStride = 1;
                    break;
                case 1:
                    if (colorFormat == COLOR_FormatI420) {
                        channelOffset = width * height;
                        outputStride = 1;
                    } else if (colorFormat == COLOR_FormatNV21) {
                        channelOffset = width * height + 1;
                        outputStride = 2;
                    }
                    break;
                case 2:
                    if (colorFormat == COLOR_FormatI420) {
                        channelOffset = (int) (width * height * 1.25);
                        outputStride = 1;
                    } else if (colorFormat == COLOR_FormatNV21) {
                        channelOffset = width * height;
                        outputStride = 2;
                    }
                    break;
            }
            ByteBuffer buffer = planes[i].getBuffer();
            int rowStride = planes[i].getRowStride();
            int pixelStride = planes[i].getPixelStride();
            if (true) {
                Log.v("photo", "pixelStride " + pixelStride);
                Log.v("photo", "rowStride " + rowStride);
                Log.v("photo", "width " + width);
                Log.v("photo", "height " + height);
                Log.v("photo", "buffer size " + buffer.remaining());
            }
            int shift = (i == 0) ? 0 : 1;
            int w = width >> shift;
            int h = height >> shift;
            buffer.position(rowStride * (crop.top >> shift) + pixelStride * (crop.left >> shift));
            for (int row = 0; row < h; row++) {
                int length;
                if (pixelStride == 1 && outputStride == 1) {
                    length = w;
                    buffer.get(data, channelOffset, length);
                    channelOffset += length;
                } else {
                    length = (w - 1) * pixelStride + 1;
                    buffer.get(rowData, 0, length);
                    for (int col = 0; col < w; col++) {
                        data[channelOffset] = rowData[col * pixelStride];
                        channelOffset += outputStride;
                    }
                }
                if (row < h - 1) {
                    buffer.position(buffer.position() + rowStride - length);
                }
            }
            if (true) Log.v("photo", "Finished reading data from plane " + i);
        }
        return data;
    }
	
}
