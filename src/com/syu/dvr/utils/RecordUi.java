package com.syu.dvr.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.syu.dvr.R;
import com.syu.dvr.TheApp;
import com.syu.dvr.activity.CalendarActivity;
import com.syu.dvr.activity.MainActivity;
import com.syu.dvr.adapter.CalendarAdapter;
import com.syu.dvr.adapter.VFileAdapter;
import com.syu.dvr.control.StartActivity;
import com.syu.dvr.factory.App4GGetRecordStatus;
import com.syu.dvr.factory.App4GGetRecordStatus.RecordCallback4G;
import com.syu.dvr.factory.AppGetCameraSdCard;
import com.syu.dvr.factory.AppGetCameraSdCard.RecordCallback;
import com.syu.dvr.factory.AppIsRecordShow;
import com.syu.dvr.factory.HideLayout;
import com.syu.dvr.factory.SwitchDev;
import com.syu.dvr.module.VideoInfo;
import com.syu.dvr.widget.StateSignLayout;
import com.uvc.jni.RainUvc;

public class RecordUi implements RecordCallback,RecordCallback4G{
	public  static final int SHOW_CIRCLE   =3;
	private final int        LIST_UPDATA   =5;
	public final int        SHOW_LAYOUT =6;
	public final static int        SET_LOCK_BG   =8;
	public static final int SD_SPACE_NOT_ENOUGH =12;
	public final  int        BLAK_PLAY		=13;
	public final static  int        START_RECODER  =14;
	public final static int         RECOURD_HANDLER=15;
	public Button photo;
	public Button mRecoder;
	public Button mSetting;
	public Button mPlayblak;
	public Button mAudio;
	public Button mHelp;
	public boolean layoutShow=false;
	public boolean isShow=false;
	public boolean isFill=false;
	public ImageView front_recod;
	public Button lock;
	public Dialog dialog;//回放
	public ListView mList;
	public VFileAdapter adapter;
	public List<VideoInfo> lists;
	public List<VideoInfo>initlist;
	public CalendarAdapter calV;
	public GridView calendar;
	public MyClick oClick; 
	public static MyHandler mHandler;
	private boolean isRecording=false;
	private  Dialog mSetDialog ;
	private TextView mTextView;
	private StateSignLayout layout;
	private LinearLayout mHideRecordLayout;
	private LinearLayout mLockShutterLayout;
	private int mCount=0;
	public void addView(View view){
		initView(view);
		initData();
		initListener();
		HideLayout.getInstance().RunTask();
	}
	private void initListener() {
		photo.setOnClickListener(oClick);
		front_recod.setOnClickListener(oClick);
		mSetting.setOnClickListener(oClick);
		mPlayblak.setOnClickListener(oClick);
		mRecoder.setOnClickListener(oClick);
		lock.setOnClickListener(oClick);
		mAudio.setOnClickListener(oClick);
		mHelp.setOnClickListener(oClick);
	}

	private void initData() {
		mCount=0;
		lists=new ArrayList<VideoInfo>();
		initlist=new ArrayList<VideoInfo>();
		oClick=new MyClick();
		mHandler=new MyHandler();
		recordingHandle(true);
	}
	public void unRegister(){
		App4GGetRecordStatus.getInstance().unRegister(this);
		AppGetCameraSdCard.getInstance().unRegister();
	}
	private void initView(View view) {
		if (TheApp.mIsShengMaiIC) {
			App4GGetRecordStatus.getInstance().setRecordCallback(this);
		}else {
			AppGetCameraSdCard.getInstance().setRecordCallback(this);
		}
		mHideRecordLayout=(LinearLayout) view.findViewById(R.id.hide_record_layout);
		mRecoder=(Button)view.findViewById(R.id.start);
		photo=(Button)view.findViewById(R.id.shutter);
		front_recod=(ImageView) view.findViewById(R.id.front_rcode);
		mSetting=(Button)view.findViewById(R.id.setting);
		mPlayblak=(Button)view.findViewById(R.id.playblack);
		lock=(Button) view.findViewById(R.id.lock);
		mTextView=(TextView) view.findViewById(R.id.recording_tips);
		layout=(StateSignLayout) view.findViewById(R.id.state_sign_layout);
		mAudio=(Button) view.findViewById(R.id.audio_record);
		mAudio.setVisibility(View.GONE);
		mHelp=(Button) view.findViewById(R.id.help);
		mLockShutterLayout=(LinearLayout) view.findViewById(R.id.lock_shutter_layout);
		if (PublicClass.getInstance().isVerticalScreen()) {
			RelativeLayout.LayoutParams params=(android.widget.RelativeLayout.LayoutParams) front_recod.getLayoutParams();
			params.topMargin=72;
			params=(LayoutParams) mTextView.getLayoutParams();
			params.topMargin=72;
			params=(LayoutParams) layout.getLayoutParams();
			params.topMargin=72;
			params=(LayoutParams) mLockShutterLayout.getLayoutParams();
			params.bottomMargin=154;
			params=(LayoutParams) mHideRecordLayout.getLayoutParams();
			params.bottomMargin=154;
		}
	}
	public synchronized void recordingHandle(boolean isInit){
		status=RecordingStatus.getInstance();
		if (status.getmRecordingStatus()==1) {
			if (mSetDialog!=null) {
				mSetDialog.dismiss();
			}
			if (plackDialog!=null) {
				plackDialog.dismiss();
			}
			if (MainActivity.activity!=null&&!MainActivity.activity.isonStop) {
				mHandler.sendEmptyMessage(16);
			}
			mRecoder.setBackgroundResource(R.drawable.d_uicommon_btncamcorderstop);
			AppIsRecordShow.getInstance().RunTask();
			isRecording=true;
		}else if (status.getmRecordingStatus()==0) {
			mRecoder.setBackgroundResource(R.drawable.d_uicommon_btncamcorder);
			AppIsRecordShow.getInstance().stopTimer();
			isRecording=false;
			front_recod.setBackgroundResource(R.drawable.circle2);
			mTextView.setVisibility(View.GONE);
		}
		/*if (status.isHasMic()) {
			mAudio.setVisibility(View.VISIBLE);
		}else {
			mAudio.setVisibility(View.GONE);
		}*/
		layout.upData(status);
		/*if (isInit) {
			return;
		}
		PublicClass.getInstance().recordStatuToast(status);*/
	}
	private Dialog plackDialog;
	private RecordingStatus status;
	class MyClick implements OnClickListener{
		@Override
		public void onClick(View view) {
			
			if (!TheApp.mApp.publicClass.isCheckeEnable()) {
				TheApp.mApp.MyTosat(R.string.str_dvr_handler);
				return;
			}
			HideLayout.getInstance().RunTask();
			status=RecordingStatus.getInstance();
			switch (view.getId()){
			case R.id.shutter:
				if (status.getIsSdWriteError()>0) {
					TheApp.mApp.MyTosat(R.string.non_writable);
					return;
				}
				if (status.isSdSpaceFull()) {
					TheApp.mApp.MyTosat(R.string.space_is_full);
					return;
				}
				if (status.getmPhotoStatus()==1) {
					TheApp.mApp.MyTosat(R.string.str_text_lock);
					return ;
				}
				if (PublicClass.getInstance().recordStatuToast(status)) {
					if (TheApp.mIsShengMaiIC) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								RainUvc.snapshot();
							}
						}).start();

						TheApp.mApp.MyTosat(R.string.str_text_photo);
					
					}else {
						TheApp.mCManager.setUvcExtenrnCall(TheApp.getDeviceId(), 
								String.valueOf(4), String.valueOf(1), String.valueOf(1));
						TheApp.mApp.MyTosat(R.string.str_text_photo);
					}
				}
				break;
			case R.id.start:
				
				if (!PublicClass.getInstance().recordStatuToast(status)) {
					return;
				}
				if (status.getIsSdWriteError()>0) {
					TheApp.mApp.MyTosat(R.string.non_writable);
					return;
				}
				if (isRecording) {
					TheApp.mCManager.stopRecord(TheApp.getDeviceId());
				}else {
					if (status.isSdSpaceFull()) {
						TheApp.mApp.MyTosat(R.string.space_is_full);
						return;
					}
					TheApp.mCManager.startRecord(TheApp.getDeviceId());
				}
				
				break;
			case R.id.lock:
				if (!isRecording) {
					TheApp.mApp.MyTosat(R.string.str_dvr_lock);
					return;
				}
				if (status.getmFileLock()==1) {
					TheApp.mApp.MyTosat(R.string.str_text_flock);
					return;
				}
				TheApp.mCManager.recordLock();
				break;
			case R.id.setting:
				if (isRecording) {
					TheApp.mApp.MyTosat(R.string.str_needs_stop_record);
					return;
				}
				
				SettingsDialog dialogUtils=new SettingsDialog();
				mSetDialog=dialogUtils.getSetingDialog();
				
				break;
			case R.id.playblack:
				if (!PublicClass.getInstance().recordStatuToast(RecordingStatus.getInstance())) {
					return;
				}
				if (isRecording) {
					TheApp.mApp.MyTosat(R.string.str_needs_stop_record);
					return;
				}
				if (TheApp.mIsShengMaiIC) {
					if (TheApp.mCManager==null) {
						break;
					}
					TheApp.mCManager.stopRecord(TheApp.getDeviceId());
					TheApp.mCManager.doStopPreview(TheApp.getDeviceId());
					if (RainUvc.setMode(2)>=0) {
						TheApp.mCManager.doCloseCamera();
						StartActivity.startAcitivity(CalendarActivity.class);
					}else {
						TheApp.mApp.MyTosat(R.string.mode_switch_failure);
						TheApp.mCManager.startPreview(TheApp.getDeviceId());
					}
				}else {
					plackDialog=SettingDialog();
				}
				break;
			case R.id.audio_record:
				if (TheApp.mIsShengMaiIC) {
					if (RainUvc.getAudioRecordStatus()==1) {
						RainUvc.recordAudio(0);
					}else {
						RainUvc.recordAudio(1);
					}
				}
				break;
			case R.id.help:
				PublicClass.getInstance().NoCameraWarning(6).show();
				break;
			default:
				break;
			
			}
		}
	}
	public void closeDialog(){
		if (mSetDialog!=null) {
			mSetDialog.dismiss();
		}
		if (plackDialog!=null) {
			plackDialog.dismiss();
		}
	}
	public class MyHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			
			case SHOW_LAYOUT:
				layoutShow=(mHideRecordLayout.getVisibility()==0);
				if (!layoutShow) {
					mHideRecordLayout.setVisibility(View.VISIBLE);
				}else {
					mHideRecordLayout.setVisibility(View.GONE);
				}
				break;
			case HandleConstant.HIDE_MENU:
				mHideRecordLayout.setVisibility(View.GONE);
				break;
			case LIST_UPDATA:
				lists.addAll(initlist);
				adapter.notifyDataSetChanged();
				break;
			case SET_LOCK_BG:
				recordingHandle(false);
				break;
			case SHOW_CIRCLE:
				if (isRecording) {
				isShow=!isShow;
				if (isShow) {
					front_recod.setBackgroundResource(R.drawable.circle);
				}else {
					front_recod.setBackgroundResource(R.drawable.crilmin);
				}
				mTextView.setVisibility(View.VISIBLE);
			}
			break;
			case 16:
			if (mCount<3) {
				LogCatUtils.showString(" hide show");
				TheApp.mApp.sendBroadCast(0);
				sendEmptyMessageDelayed(1, 1000);
				mCount++;
			}else {
				mCount=0;
			}
			break;
			default:
				break;
			}
			
		}
	}
	
	public  Dialog SettingDialog(){
		final Dialog adddalog;
    	adddalog=new Dialog(TheApp.mApp,R.style.add_dialog);
    	int []size;
    	size=PublicClass.getInstance().getWindowManeger();
    	if (size==null||size.length!=2) {
    		size=new int[]{1024,600};
		}
		ViewGroup.LayoutParams params= new ViewGroup.LayoutParams(size[0]/2, size[1]/2);
    	View view=LinearLayout.inflate(TheApp.mApp, R.layout.sele_filemanager, null);
    	adddalog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		adddalog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		adddalog.setContentView(view,params);
		initHandlerDialog(adddalog);
		adddalog.show();
		return adddalog;
	}
	private void initHandlerDialog(Dialog adddalog) {
		adddalog.findViewById(R.id.file_mode).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (isRecording) {
					TheApp.mApp.MyTosat(R.string.str_needs_stop_record);
					return;
				}
				if (TheApp.mCManager!=null) {
					
					if (StartActivity.startAcitivity("com.syu.filemanager", "com.syu.fromDvr")) {
						mswitchDivce();
					}
					SwitchDev.getInstance().setSwitch(true);
				}
			}
		});
		adddalog.findViewById(R.id.calendar_mode).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (isRecording) {
					TheApp.mApp.MyTosat(R.string.str_needs_stop_record);
					return;
				}
				SwitchDev.getInstance().setSwitch(true);
				//设备模式
				if (TheApp.mCManager!=null) {
					
					mswitchDivce();
				}
				
				StartActivity.startAcitivity(CalendarActivity.class);
			}
		});
	}
	
	private void mswitchDivce(){
		if (TheApp.mCManager==null) {
			return;
		}
		
		TheApp.mCManager.doStopPreview(TheApp.getDeviceId());
		MainActivity.isModeSwitch=true;
		TheApp.mCManager.setUvcExtenrnCall(TheApp.getDeviceId(), "0B",
				String.valueOf(0), String.valueOf(0));
	}
	
	@Override
	public void Callback() {
		if (mHandler!=null) {
			mHandler.sendEmptyMessage(SET_LOCK_BG);
		}
		
	}
	
}
