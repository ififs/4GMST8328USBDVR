package com.syu.dvr.activity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.syu.dvr.R;
import com.syu.dvr.TheApp;
import com.syu.dvr.utils.LogCatUtils;
import com.syu.dvr.utils.ProgressDialog;
import com.syu.dvr.widget.ImageZoom;
import com.uvc.jni.RainUvc;

public class FillActivity extends BaseActivity {
	public ImageZoom fill;
	public static FillActivity context;
	private boolean isFill=false;
	InputStream inputStream=null;
	BitmapFactory.Options options;
	private Bitmap bitmap=null;
	private FrameLayout mRootView;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		addActivity(this);
		LayoutInflater inflater=LayoutInflater.from(this);
		mRootView=(FrameLayout) inflater.inflate(R.layout.fill_layout_activity, null);
		setContentView(mRootView);
		context=this;
		Intent intent=getIntent();
		Bundle bundle=intent.getBundleExtra("TimeData");
		String path=bundle.getString("data");
		options=new BitmapFactory.Options();
		ProgressDialog.getInstance().progressShow(R.string.str_add_file);
		new MyThread(path).start();
	}
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				finish();
				break;
			case 1:
				Bitmap bitmap=(Bitmap) msg.obj;
				if (bitmap==null) {
					inputStream=getResources().openRawResource(R.drawable.errorinmage);
					options.inSampleSize=1;
					bitmap=BitmapFactory.decodeStream(inputStream, null, options);
					TheApp.mApp.MyTosat(R.string.image_loading_fail);
				}
				fill=new ImageZoom(FillActivity.this);
				fill.setImage(bitmap);
				FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT);
				fill.setScaleType(ImageView.ScaleType.FIT_XY);
				mRootView.addView(fill, params);
				ProgressDialog.getInstance().progressdismiss();
				break;
			default:
				break;
			}
		};
	};
	public void onclick(View view){
		handler.sendEmptyMessageDelayed(0, 200);
	}
	class MyThread extends Thread{
		private String path;
		public MyThread(String path){
			this.path=path;
		}
		@Override
		public void run() {
			super.run();
			InputStream inputStream=null;
			BitmapFactory.Options options=new BitmapFactory.Options();
			try {
				startLoad(false);
				LogCatUtils.showString("===bitmap=="+String.valueOf(bitmap==null));
				Message message =Message.obtain();
				message.what=1;
				message.obj=bitmap;
				handler.sendMessage(message);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}finally {
				if (inputStream!=null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		private boolean startLoad(boolean isAgain) throws FileNotFoundException{
			if (TheApp.mIsShengMaiIC) {
				if (!path.isEmpty()&&path.endsWith(".JPG")) {
					byte []data=RainUvc.getReadFileAllData(3, path);
					if (data!=null&&data.length>0) {
						bitmap=BitmapFactory.decodeByteArray(data, 0, data.length);
						LogCatUtils.showString("==bitmap=null=="+String.valueOf(bitmap==null));
					}
				}	
			}else {
				inputStream=new FileInputStream(path);
				options.inTempStorage=new byte[100*1024];
				options.inPreferredConfig=Bitmap.Config.RGB_565;
				options.inSampleSize=2;
				bitmap=BitmapFactory.decodeStream(inputStream,null,options);
			}
			if (bitmap==null&&!isAgain) {
				return startLoad(true);
			}
			return false;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
			if (isFill) {
				fill.setFocusable(false);
				isFill=false;
			}else {
				handler.sendEmptyMessageDelayed(0, 200);
			}
		}
		return true;
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
}
