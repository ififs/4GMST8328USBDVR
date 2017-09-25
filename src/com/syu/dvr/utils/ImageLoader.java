package com.syu.dvr.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.syu.dvr.R;
import com.syu.dvr.TheApp;
import com.syu.dvr.control.FileManager;
import com.uvc.jni.RainUvc;

public class ImageLoader {
	private LruCache<String, Bitmap> mLruCache;
	private ExecutorService mThreadPool;
	private Handler mHandler;
	private FileManager fileManager=new FileManager();
	private static ImageLoader mInstance;
	private ExecutorService eService ; 
	private File file;
	private byte []data;
	private ImageLoader(){
		eService = Executors.newFixedThreadPool(3); 
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		mLruCache = new LruCache<String, Bitmap>(cacheSize){
			@Override
			protected int sizeOf(String key, Bitmap value)
			{
				return value.getRowBytes() * value.getHeight();
			}
		};
	}
	public static ImageLoader getInstance(){

			if (mInstance == null)
			{
				synchronized (ImageLoader.class)
				{
					if (mInstance == null){
						mInstance = new ImageLoader();
					}
				}
			}
			return mInstance;
		}
	
	public void loadImage(final String path, final ImageView imageView,final String name){
		if (path.isEmpty()) {
			return;
		}
		if (imageView==null) {
			return;
		}
		file=new File(path);
		if (!TheApp.mIsShengMaiIC&&!file.exists()) {
			return;
		}
		imageView.setTag(path);
		if (mHandler == null){
			mHandler = new Handler(){
				@Override
				public void handleMessage(Message msg){
					ImgBeanHolder holder = (ImgBeanHolder) msg.obj;
					ImageView imageView = holder.imageView;
					Bitmap bm = holder.bitmap;
					String path = holder.path;
					if (imageView.getTag().toString().equals(path))
					{
						imageView.setImageBitmap(bm);
					}
				}
			};
		}
		
		Bitmap bm = getBitmapFromLruCache(path);
		
		if (bm != null){
			ImgBeanHolder holder = new ImgBeanHolder();
			holder.bitmap = bm;
			holder.imageView = imageView;
			holder.path = path;
			Message message = Message.obtain();
			message.obj = holder;
			mHandler.sendMessage(message);
			
		}else{
			eService.execute(new Runnable() {
				@Override
				public void run() {
					String mFileName=name;
					Bitmap bm =null;
					if (TheApp.mIsShengMaiIC) {
						int type=fileManager.getTypeForName(name);
						if (type>=0) {
							mFileName=name;
							if (name.endsWith(".mp4")) {
								mFileName=name.replace(".mp4", ".jpg");
							}
							LogCatUtils.showString("===name=="+mFileName+"===type=="+type);
							data=RainUvc.getReadFileAllData(type, mFileName);
							if (data!=null&&data.length>0) {
								bm=byteToBitmap(data);
								LogCatUtils.showString("==bitmap=null=="+String.valueOf(bm==null));
							}
						}
					}else {
						bm= decodeSampledBitmapFromResource(path, 0,0);
					}
					if (bm==null) {
//						bm=BitmapFactory.decodeResource(TheApp.mApp.getResources(), R.drawable.photoerror);
						threadsleep();
						return;
					}
					addBitmapToLruCache(path, bm);
					ImgBeanHolder holder = new ImgBeanHolder();
					holder.bitmap = getBitmapFromLruCache(path);
					holder.imageView = imageView;
					holder.path = path;
					Message message = Message.obtain();
					message.obj = holder;
					mHandler.sendMessage(message);
					threadsleep();
				}
			});
		}
	}
	private void threadsleep(){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (TheApp.mPlayOrDelete) {
			threadsleep();
		}
	}
	private Bitmap bitmap = null; 
	private InputStream input = null; 
	private SoftReference softRef;
	private  BitmapFactory.Options options;
	@SuppressWarnings("unchecked")
	private Bitmap byteToBitmap(byte[] imgByte) {  
		options= new BitmapFactory.Options();  
        options.inSampleSize = 8;  
        input = new ByteArrayInputStream(imgByte);  
		softRef = new SoftReference(BitmapFactory.decodeStream(  
                input, null, options));  
        bitmap = (Bitmap) softRef.get();  
        if (imgByte != null) {  
            imgByte = null;  
        }  
        try {  
            if (input != null) {  
                input.close();  
            }  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return bitmap;  
    }  
	private DisplayMetrics displayMetrics;
	private  LayoutParams params ;
	private int width ;
	private int height;
	private ImageSize getImageViewWidth(ImageView imageView){
		ImageSize imageSize = new ImageSize();
		displayMetrics = imageView.getContext()
				.getResources().getDisplayMetrics();
		params = imageView.getLayoutParams();

		width = params.width == LayoutParams.WRAP_CONTENT ? 0 : imageView
				.getWidth(); // Get actual image width
		if (width <= 0)
			width = params.width; // Get layout width parameter
		if (width <= 0)
			width = getImageViewFieldValue(imageView, "mMaxWidth"); // Check
																	// maxWidth
																	// parameter
		if (width <= 0)
			width = displayMetrics.widthPixels;
		 height = params.height == LayoutParams.WRAP_CONTENT ? 0 : imageView
				.getHeight(); // Get actual image height
		if (height <= 0)
			height = params.height; // Get layout height parameter
		if (height <= 0)
			height = getImageViewFieldValue(imageView, "mMaxHeight"); // Check
																		// maxHeight
																		// parameter
		if (height <= 0)
			height = displayMetrics.heightPixels;
		imageSize.width = width;
		imageSize.height = height;
		return imageSize;

	}
	private Bitmap getBitmapFromLruCache(String key){
		return mLruCache.get(key);
	}

	private void addBitmapToLruCache(String key, Bitmap bitmap){
		if (getBitmapFromLruCache(key) == null)
		{
			if (bitmap != null)
				mLruCache.put(key, bitmap);
		}
	}

	private int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight){
		int width = options.outWidth;
		int height = options.outHeight;
		int inSampleSize = 1;

		if (width > reqWidth && height > reqHeight){
			int widthRatio = Math.round((float) width / (float) reqWidth);
			int heightRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = Math.max(widthRatio, heightRatio);
		}
		return inSampleSize;
	}
	private Bitmap decodeSampledBitmapFromResource(String pathName,
			int reqWidth, int reqHeight){
		 Bitmap bitmap=null;
        InputStream inputStream=null;
        try {
            inputStream=new FileInputStream(pathName);
             BitmapFactory.Options options=new BitmapFactory.Options();
            options.inTempStorage=new byte[100*1024];
            options.inPreferredConfig=Bitmap.Config.RGB_565;
            options.inSampleSize=18;
            bitmap=BitmapFactory.decodeStream(inputStream,null,options);
 
        } catch (FileNotFoundException e) {
        	
            e.printStackTrace();
        }      
		
		
		return bitmap;
	}

	private class ImgBeanHolder
	{
		Bitmap bitmap;
		ImageView imageView;
		String path;
	}

	private class ImageSize{
		int width;
		int height;
	}

	
	private static int getImageViewFieldValue(Object object, String fieldName){
		int value = 0;
		try{
			Field field = ImageView.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			int fieldValue = (Integer) field.get(object);
			if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE)
			{
				value = fieldValue;

				Log.e("TAG", value + "");
			}
		} catch (Exception e)
		{
		}
		return value;
	}

}