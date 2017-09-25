package com.syu.dvr.widget;

import com.syu.dvr.R;
import com.syu.dvr.TheApp;
import com.syu.dvr.utils.LogCatUtils;
import com.syu.dvr.utils.RecordingStatus;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class StateSignLayout extends LinearLayout{
	private ImageView mConnect;
	private ImageView mSdcard;
	private ImageView mSoundRec;
	private ImageView mRecordTime;
	private ImageView mLock;
	private ImageView mResolution;
	private LinearLayout.LayoutParams params;
	public StateSignLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(LinearLayout.HORIZONTAL);
		params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		initLayout(context);
	}

	private void initLayout(Context context) {
		mConnect=new ImageView(context);
		mConnect.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.power_connection));
		addView(mConnect, params);
		
		mSdcard=new ImageView(context);
		mSdcard.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.no_sdcard));
		addView(mSdcard, params);
		
		mSoundRec=new ImageView(context);
		mSoundRec.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sound_no_recording));
		addView(mSoundRec, params);
		
		mLock=new ImageView(context);
		mLock.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.video_locking_bg));
		addView(mLock, params);
		
		
		mRecordTime=new ImageView(context);
		mRecordTime.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.one_record_time));
		addView(mRecordTime, params);
		
		mResolution=new ImageView(context);
		mResolution.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mresolution_720));
		addView(mResolution, params);
	}
	public void upData(RecordingStatus status) {
		if (status.getmRecordingStatus()==1) {
			mRecordTime.setVisibility(VISIBLE);
			int mTime=TheApp.mApp.getInt("RECORDING_TIME", 1);
			if (mTime==1) {
				mRecordTime.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.one_record_time));
			}else if (mTime==3) {
				mRecordTime.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.three_record_time));
			}else if (mTime==5) {
				mRecordTime.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.five_record_time));
			}
		}else {
			mRecordTime.setVisibility(GONE);
		}
		if ((status.getmFileStatus() & 1)==0) {
			mLock.setVisibility(View.GONE);
		}else if ((status.getmFileStatus() & 1)==1) {
			mLock.setVisibility(View.VISIBLE);
		}
		/*if (status.getmScardStatus()==0) {
			mSdcard.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.has_sdcard));
		}else */if (status.getmScardStatus()==1) {
			mSdcard.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.no_sdcard));
		}else {
			mSdcard.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.has_sdcard));
		}
		if (status.isMisRecordAudio()&&status.isHasMic()) {
			mSoundRec.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sound_recording));
		}else{
			mSoundRec.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sound_no_recording));
		}
		if (status.getmResolution()==0) {
			mResolution.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mresolution_1080));
		}else {
			mResolution.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mresolution_720));
		}
	}
}
