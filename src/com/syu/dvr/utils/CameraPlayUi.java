package com.syu.dvr.utils;

import android.R.layout;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.syu.dvr.R;
import com.syu.dvr.module.CameraPlayManeger;
import com.syu.dvr.module.CameraPlayManeger.CameraPlayCallback;
import com.syu.dvr.module.MediaTypeModule;
import com.uvc.jni.RainUvc;

public class CameraPlayUi implements CameraPlayCallback{
	public Button mLastSong;
	public Button mNextSong;
	public Button mStartPause;
	public MyClick oClick; 
	public TextView mStartTime;
	public TextView mEndTime;
	public SeekBar mProgressBar;
	private CameraPlayManeger mPlayManeger;
	private View view;
	private boolean isTracking;
	private LinearLayout mPlayControl;
	public CameraPlayUi(Context context,String mPlayName) {
		initData(mPlayName);
	}
	public void addView(View view){
		this.view=view;
		initView();
		initListener();
	}
	public void removeView(){
		this.view=null;
		if (mPlayManeger!=null) {
			mPlayManeger.unregisterCallback();
		}
		mPlayManeger.stopPlay();
	}
	private void initListener() {
		mLastSong.setOnClickListener(oClick);
		mNextSong.setOnClickListener(oClick);
		mStartPause.setOnClickListener(oClick);
		mProgressBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				handler.sendEmptyMessageDelayed(1, 5*100);
			}
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				CameraPlayUi.this.isTracking=true;
			}
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean isTracking) {
				if (isTracking) {
					int mTimeLong=mPlayManeger.getmTimeLeng();
					RainUvc.seekPlayingTime((int) ((float)arg1/100*mTimeLong));
				}
			}
		});
		
	}

	private void initData(String mPlayName) {
		oClick=new MyClick();
		if (mPlayName.isEmpty()) {
			return;
		}
		mPlayManeger=new CameraPlayManeger();
		mPlayManeger.registerCallback(this);
		if (mPlayName.endsWith(".mp4")&&mPlayName.startsWith("ch0")) {
			if (MediaTypeModule.getInstance().getmEvent()!=null&&
					MediaTypeModule.getInstance().getmEvent().contains(mPlayName)) {
				mPlayManeger.setmCurrentType(1);
				mPlayManeger.setmCurrentData(MediaTypeModule.getInstance().getmEvent());
			}else {
				mPlayManeger.setmCurrentType(0);
				mPlayManeger.setmCurrentData(MediaTypeModule.getInstance().getmVideo());
			}
		}
		mPlayManeger.setmCurrentName(mPlayName);
	}
	
	private void initView() {
		mLastSong=(Button)view.findViewById(R.id.last_song);
		mNextSong=(Button)view.findViewById(R.id.next_song);
		mStartPause=(Button)view.findViewById(R.id.start_pause);
		mStartTime=(TextView) view.findViewById(R.id.start_time);
		mEndTime=(TextView) view.findViewById(R.id.end_time);
		mProgressBar=(SeekBar) view.findViewById(R.id.progressBar);
		mPlayControl=(LinearLayout) view.findViewById(R.id.play_control_layout);
		if (PublicClass.getInstance().isVerticalScreen()) {
			RelativeLayout.LayoutParams params=(android.widget.RelativeLayout.LayoutParams) mPlayControl.getLayoutParams();
			params.bottomMargin=162;
			params.leftMargin=20;
			view.findViewById(R.id.last_song_layout).setLayoutParams(new LinearLayout.
					LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.1f));
			view.findViewById(R.id.start_song_layout).setLayoutParams(new LinearLayout.
					LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.1f));
			view.findViewById(R.id.next_song_layout).setLayoutParams(new LinearLayout.
					LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.1f));
			view.findViewById(R.id.placeholder_layout).setLayoutParams(new LinearLayout.
					LayoutParams(0, 60, 0.4f));
		}
	}
	public void startplay(){
		if (mPlayManeger!=null) {
			int value=mPlayManeger.startPlay(mPlayManeger.getmCurrentType(),
					mPlayManeger.getmCurrentName());
			mPlayManeger.setmProgress(value);
		}		
	}
	private void upDataUi(){
		if (mEndTime!=null&&mPlayManeger!=null) {
			mEndTime.setText(mPlayManeger.getmProgressTime());
		}
		if (mStartTime!=null&&mPlayManeger!=null) {
			mStartTime.setText(mPlayManeger.getmCurrentTime());
		}
		if (mProgressBar!=null&&mPlayManeger!=null&&!isTracking) {
			mProgressBar.setProgress(mPlayManeger.getmCurrentProgress());
		}
		LogCatUtils.showString("show===="+mPlayManeger.getmPlayState());
		if (mPlayManeger!=null&&mStartPause!=null) {
			if (mPlayManeger.getmPlayState()==0) {
				mStartPause.setBackgroundResource(R.drawable.d_uicommon_start);
			}else if (mPlayManeger.getmPlayState()==1) {
				mStartPause.setBackgroundResource(R.drawable.d_uicommon_pause);
			}else if (mPlayManeger.getmPlayState()==2) {
				mStartPause.setBackgroundResource(R.drawable.d_uicommon_start);
			}
		}
	}
	
	class MyClick implements OnClickListener{
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.last_song:
				if (mPlayManeger==null) {
					return;
				}
				mPlayManeger.setmProgress(mPlayManeger.startPlay(mPlayManeger.getmCurrentType(),
						mPlayManeger.getLastSong()));
				break;
			case R.id.next_song:
				if (mPlayManeger==null) {
					return;
				}
				mPlayManeger.setmProgress(mPlayManeger.startPlay(mPlayManeger.getmCurrentType(),
						mPlayManeger.getNestSong()));
				break;
			case R.id.start_pause:
				
				if (mPlayManeger.getmPlayState()==1) {
					if (RainUvc.pausePlayFile()>=0) {
						mPlayManeger.setmPlayState(2);
					}
				}else if(mPlayManeger.getmPlayState()==2){
					if (RainUvc.resumePlayFile()>=0) {
						mPlayManeger.setmPlayState(1);
					}
				}else if (mPlayManeger.getmPlayState()==0) {
					mPlayManeger.setmProgress(mPlayManeger.startPlay(mPlayManeger.getmCurrentType(),
							mPlayManeger.getmCurrentName()));
				}
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void cameraPlayCallback() {
		
		handler.sendEmptyMessage(0);
	}
	public Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				upDataUi();
				break;
			case 1:
				CameraPlayUi.this.isTracking=false;
				break;
			default:
				break;
			}
		};
	};
	
}
