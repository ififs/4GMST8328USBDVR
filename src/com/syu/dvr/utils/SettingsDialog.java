package com.syu.dvr.utils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.syu.dvr.R;
import com.syu.dvr.TheApp;
import com.syu.dvr.activity.SystemUpgrade;
import com.syu.dvr.control.StartActivity;
import com.syu.dvr.factory.AppGetCameraSdCard;
import com.syu.dvr.server.BootReceiver;
import com.syu.dvr.widget.SettingButton;
import com.syu.dvr.widget.SettingLayout;
import com.syu.dvr.widget.SettingSwitch;
import com.uvc.jni.RainUvc;

public class SettingsDialog {
	private SettingLayout mTimeSetting;
	private SettingLayout mResolutionSeting;
	private SettingLayout mFrequenceSeting;
	private SettingLayout mCollisionSetting;
	private SettingSwitch mAudioSwitch;
	private SettingSwitch mHdrSwitch;
	private SettingButton mFormat;
	private SettingButton mFactory;
	private SettingButton mSysUpgrade;
	private SettingButton mVersion;
	private SettingButton mAboutSoftware;
	private boolean isAudioRec;
	private boolean isHDRenable;
	private String versionVvalue="";
	private int mRecLength=0;
	private int mFrequence=0;
	private int mResolution=0;
	private int mCollision=0;
	private int temp;
	private int [] mRecLengths=new int[]{1,3,5};
	private int [] setDefuleValue;
	private int []setValue=new int [16];
	private Button[]mButtons;
	private boolean isHandleFish=false;
	private Dialog mSetting;
	public  Dialog getSetingDialog(){
		isHandleFish=false;
		mSetting=new Dialog(TheApp.mApp,R.style.setting_dialog);
    	int []size;
    	size=PublicClass.getInstance().getWindowManeger();
    	if (size==null||size.length!=2) {
    		size=new int[]{1024,600};
		}
    	LinearLayout.LayoutParams params=new LayoutParams(size[0]<=800?size[0]:size[0]*4/5, size[1]*2/3+15);
    	View view=null;
    	view=LinearLayout.inflate(TheApp.mApp, R.layout.layout_setting, null);
    	mSetting.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
    	mSetting.requestWindowFeature(Window.FEATURE_NO_TITLE);
    	mSetting.setContentView(view,params);
    	mSetting.show();
    	mSetting.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialognfor) {
				
				ProgressDialog.getInstance().progressdismiss();
			}
		});
    	initSettingView(mSetting);
		return mSetting;
	}
	private void initSettingView(Dialog setting) {
		mTimeSetting=(SettingLayout) setting.findViewById(R.id.time_setting_layout);
		mResolutionSeting=(SettingLayout) setting.findViewById(R.id.resolution_setting_layout);
		mFrequenceSeting=(SettingLayout) setting.findViewById(R.id.strobe_setting_layout);
		mCollisionSetting=(SettingLayout) setting.findViewById(R.id.collision_setting_layout);
		mAudioSwitch=(SettingSwitch) setting.findViewById(R.id.audo_setting_layout);
		mHdrSwitch=(SettingSwitch) setting.findViewById(R.id.hdr_setting_layout);
		mAboutSoftware=(SettingButton) setting.findViewById(R.id.about_software_layout);
		mFormat=(SettingButton) setting.findViewById(R.id.format_title_layout);
		mFactory=(SettingButton) setting.findViewById(R.id.factory_setting_layout);
		mSysUpgrade=(SettingButton) setting.findViewById(R.id.system_upgrade_layout);
		mVersion=(SettingButton) setting.findViewById(R.id.version_information_layout);
		initSettingData();
		onClickListener();
		if (TheApp.mApp.clientForeign()) {
			mSysUpgrade.setVisibility(View.GONE);
		}
	}
	private void onClickListener() {
		mButtons=mTimeSetting.getButtons();
		if (mButtons!=null&&mButtons.length>1) {
			for (int i = 0; i < mButtons.length; i++) {
				mButtons[i].setOnClickListener(new MySetOnclick(i){
					public void onClick(View arg0) {
						if (TheApp.mIsShengMaiIC) {
							int ret=RainUvc.setRecLength(mRecLengths[mSetID]);
							if (ret>=0) {
								mTimeSetting.updataLayout(mSetID);
								TheApp.mApp.saveInt("RECORDING_TIME", mRecLengths[mSetID]);
							}
						}else {
							setValue[2]=mRecLengths[mSetID];
							settingClick();
						}
					};
				});
			}
		}
		mButtons=mResolutionSeting.getButtons();
		if (mButtons!=null&&mButtons.length>1) {
			for (int i = 0; i < mButtons.length; i++) {
				mButtons[i].setOnClickListener(new MySetOnclick(i){
					public void onClick(View arg0) {
						if (TheApp.mIsShengMaiIC) {
							int ret=RainUvc.setResolution(mSetID);
							if (ret>=0) {
								mResolutionSeting.updataLayout(mSetID);
							}
						}else {
							
							
						}
						
					};
				});
			}
		}
		mButtons=mFrequenceSeting.getButtons();
		if (mButtons!=null&&mButtons.length>1) {
			for (int i = 0; i < mButtons.length; i++) {
				mButtons[i].setOnClickListener(new MySetOnclick(i){
					public void onClick(View arg0) {
						int ret=RainUvc.setFrequence(mSetID);
						if (ret>=0) {
							mFrequenceSeting.updataLayout(mSetID);
						}
					};
				});
			}
		}
		mButtons=mCollisionSetting.getButtons();
		if (mButtons!=null&&mButtons.length>1) {
			for (int i = 0; i < mButtons.length; i++) {
				mButtons[i].setOnClickListener(new MySetOnclick(i){
					public void onClick(View arg0) {
						if (TheApp.mIsShengMaiIC) {
							
						}else {
							setValue[4]=mSetID;
							settingClick();
						}
					};
				});
			}
		}
		mButtons=mAudioSwitch.getButtons();
		if (mButtons!=null&&mButtons.length>1) {
			for (int i = 0; i < mButtons.length; i++) {
				mButtons[i].setOnClickListener(new MySetOnclick(i){
					public void onClick(View arg0) {
						
						if (TheApp.mIsShengMaiIC) {
							int ret=RainUvc.recordAudio(mSetID);
							if (ret>=0) {
								mAudioSwitch.updataLayout(mSetID==1);
							}
						}else {
							if (isAudioRec) {
								setValue[5]=1;
							}else {
								setValue[5]=0;
							}
							
							LogCatUtils.showString("===setValue[5]=="+setValue[5]);
							settingClick();
						}
					};
				});
			}
		}
		mButtons=mHdrSwitch.getButtons();
		if (mButtons!=null&&mButtons.length>1) {
			for (int i = 0; i < mButtons.length; i++) {
				mButtons[i].setOnClickListener(new MySetOnclick(i){
					public void onClick(View arg0) {
						if (TheApp.mIsShengMaiIC) {
							
						}else {
							if (isHDRenable) {
								setValue[11]=0;
							}else{
								setValue[11]=1;
							}
							settingClick();
						}
					};
				});
			}
		}
		mFormat.getButton().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				RecordingStatus status=RecordingStatus.getInstance();
				
				if (status.getmScardStatus()==0) {
					dialogHandler(0);
				}
				if (status.getmScardStatus()==1) {
					dialogHandler(2);
				}
			}
		});
		mVersion.getButton().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogHandler(5);
			}
		});
		
		mSysUpgrade.getButton().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialogHandler(4);
				
			}
		});
		mFactory.getButton().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogHandler(3);
			}
		});
		mAboutSoftware.getButton().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialogHandler(6);
			}
		});
		
	}
	private void initSettingData() {
		if (TheApp.mIsShengMaiIC) {
			temp=RainUvc.getRecLength();
			for (int i = 0; i < mRecLengths.length; i++) {
				if (mRecLengths[i]==temp) {
					mRecLength=i;
					break;
				}
			}
			mFrequence=RainUvc.getFrequence();
			mResolution=RainUvc.getResolution();
			isAudioRec=(RainUvc.getAudioRecordStatus()==1?true:false);
			versionVvalue=RainUvc.getPlatformVersion();
			mCollisionSetting.setVisibility(View.GONE);
			mHdrSwitch.setVisibility(View.GONE);
			mAudioSwitch.setVisibility(View.GONE);
			if (!TextUtils.isEmpty(versionVvalue)&&versionVvalue.equals("1080P")) {
				mResolutionSeting.setVisibility(View.VISIBLE);
			}else {
				mResolutionSeting.setVisibility(View.GONE);
			}
		}else {
			setDefuleValue=TheApp.mCManager.setUvcExtenrnCall(TheApp.getDeviceId(),
	    			String.valueOf(1), String.valueOf(81),"");
	    	if (setDefuleValue==null) {
				return;
			}
	    	setValue[0]=1;
	    	setValue[1]=1;
	    	LogCatUtils.showString("==setDefuleValue.length=="+setDefuleValue.length);
	    	if (setDefuleValue.length!=16) {
				return;
			}
	    	for (int i = 2; i < 16; i++) {
	    		setValue[i]=setDefuleValue[i];
	    		LogCatUtils.showString("==setDefuleValue["+i+"]=="+setDefuleValue[i]);
			}
	    	TheApp.mApp.saveInt("RECORDING_TIME", setDefuleValue[2]);
	    	for (int i = 0; i < mRecLengths.length; i++) {
				if (mRecLengths[i]==setDefuleValue[2]) {
					mRecLength=i;
					break;
				}
			}
			int [] version=getversion();
			int versionid=-1;
			if (version!=null&&version.length>11) {
				versionid=version[10];
			}
			if (versionid==4||versionid==1) {
				RecordingStatus.getInstance().setHasMic(true);
				mAudioSwitch.setVisibility(View.VISIBLE);
			}else {
				RecordingStatus.getInstance().setHasMic(false);
				mAudioSwitch.setVisibility(View.GONE);
			}
			if (0<=setDefuleValue[4]&&setDefuleValue[4]<=3) {
				mCollisionSetting.setVisibility(View.VISIBLE);
				mCollision=setDefuleValue[4];
			}else {
				mCollisionSetting.setVisibility(View.GONE);
			}
			LogCatUtils.showString("===setDefuleValue[5]==0==="+setDefuleValue[5]);
			if ((setDefuleValue[5]==0)&&RecordingStatus.getInstance().isHasMic()
					/*version[7]==3&&version[8]==2&&version[9]==3*/) {
				isAudioRec=true;
			}else {
				isAudioRec=false;
			}
			if (version[7]==3&&version[8]==2&&version[9]==3) {
				mResolution=0;
			}else {
				mResolution=1;
			}
			LogCatUtils.showString("===isAudioRec==="+String.valueOf(isAudioRec));
			
			if (setDefuleValue[11]==0) {
				isHDRenable=false;
			}else if (setDefuleValue[11]==1) {
				isHDRenable=true;
			}
			mFrequenceSeting.setVisibility(View.GONE);
			mResolutionSeting.setVisibility(View.GONE);
		}
		if (TheApp.mApp.clientForeign()) {
			mAboutSoftware.setVisibility(View.GONE);
		}
		mTimeSetting.updataLayout(mRecLength);
		mResolutionSeting.updataLayout(mResolution);
		mFrequenceSeting.updataLayout(mFrequence);
		mCollisionSetting.updataLayout(mCollision);
		mAudioSwitch.updataLayout(isAudioRec);
		mHdrSwitch.updataLayout(isHDRenable);
		RecordingStatus.getInstance().setMisRecordAudio(isAudioRec);
		RecordingStatus.getInstance().setmResolution(mResolution);
		if (TheApp.mIsShengMaiIC) {
			
		}else {
			AppGetCameraSdCard.getInstance().settingCallback();
		}
	}
	private int [] getversion(){
		int [] version=TheApp.mCManager.setUvcExtenrnCall(TheApp.getDeviceId(),
    			String.valueOf(9), String.valueOf(81),"");
		if (version==null||version.length<16) {
			return null;
		}
		versionVvalue="";
		for (int i = 0; i < version.length; i++) {
			versionVvalue+=String.valueOf(version[i]);
		}
		if (TextUtils.isEmpty(versionVvalue)) {
			return null;
		}
		LogCatUtils.showString("==versionVvalue=="+versionVvalue);
		return version;
	}
	class MySetOnclick implements OnClickListener{
		public int mSetID;
		public MySetOnclick(int id) {
			this.mSetID=id;
		}
		@Override
		public void onClick(View arg0) {
			
		}
	}
	private void settingClick(){
		if (isHandleFish) {
			TheApp.mApp.MyTosat(R.string.str_dvr_handler);
			return;
		}
		isHandleFish=true;
		ProgressDialog.getInstance().progressShow(R.string.setting_camera);
		TheApp.mCManager.setCameraSetting(TheApp.getDeviceId(), setValue);	
		handler.sendEmptyMessageDelayed(0, 1000);
	}
	private Handler handler=new Handler(Looper.getMainLooper()){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (TheApp.mApp!=null) {
					if (mSetting!=null) {
						mSetting.dismiss();
						TheApp.mApp.MyTosat(R.string.factory_settings_fish);
					}
				}
				break;
			default:
				break;
			}
			initSettingData();
			ProgressDialog.getInstance().progressdismiss();
			isHandleFish=false;
			if (TheApp.mCManager!=null) {
				TheApp.mCManager.startPreview(TheApp.getDeviceId());
			}
		};
	};
	private TextView cancle;
	private TextView message;
	private TextView mTittle;
	private TextView mSure;
	private  Dialog mFormatsdDialog;
	private void dialogHandler(final int mcount){
		int []size;
    	size=PublicClass.getInstance().getWindowManeger();
    	if (size==null||size.length!=2) {
    		size=new int[]{1024,600};
		}
		if (mFormatsdDialog==null) {
			ViewGroup.LayoutParams params= new ViewGroup.LayoutParams(size[0]/2, size[1]/2);
			View view =LinearLayout.inflate(TheApp.mApp, R.layout.format_sd_layout, null);
			mFormatsdDialog=new Dialog(TheApp.mApp,R.style.add_dialog);
			mFormatsdDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
			mFormatsdDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mFormatsdDialog.setContentView(view,params);
			cancle=(TextView) mFormatsdDialog.findViewById(R.id.dialog_cancle);
			message=(TextView) mFormatsdDialog.findViewById(R.id.dialog_messege);
			mTittle=(TextView) mFormatsdDialog.findViewById(R.id.dialog_tittle);
			mSure=(TextView) mFormatsdDialog.findViewById(R.id.dialog_sure);
			message.setMovementMethod(ScrollingMovementMethod.getInstance());
		}
		cancle.setVisibility(View.VISIBLE);
		cancle.setText(R.string.str_tip_cancle);
		message.setText(R.string.str_format_message_two);
		mTittle.setText(R.string.str_format_title);
		mSure.setText(R.string.str_tip_sure);
		message.setTextColor(TheApp.mApp.getResources().getColor(R.color.red));
		message.setGravity(Gravity.CENTER_VERTICAL);
		if (mcount==1) {
			mTittle.setText(R.string.str_format_title_two);
			message.setText(R.string.str_delete_all_file);
		}
		if (mcount==2) {
			message.setText(R.string.str_tip_no_sdcard);
			cancle.setVisibility(View.GONE);
		}
		if (mcount==3) {
			mTittle.setText(R.string.str_text_factory_setting);
			message.setText(R.string.str_clear_all_setting);
		}
		if (mcount==4) {
			mTittle.setText(R.string.str_system_upgrade);
			message.setText(R.string.system_upgrade);
		}
		if (mcount==5) {
			mTittle.setText(R.string.str_version_information);
			message.setText(TextUtils.isEmpty(versionVvalue)?"":versionVvalue);
			cancle.setVisibility(View.GONE);
			message.setTextColor(TheApp.mApp.getResources().getColor(R.color.white));
		}
		if (mcount==6) {
			mTittle.setText(R.string.about_software);
			String messe=TheApp.mApp.getString(R.string.about_software_message);
			String []mVersions=TheApp.mApp.getVersions();
			if (mVersions!=null&&mVersions.length>1) {
				messe=messe.replace("@#", TextUtils.isEmpty(mVersions[0])?"":"V"+mVersions[0]);
			}
			String[] mSup=TheApp.mApp.getResources().getStringArray(R.array.fyt_sup_usb_dvr);
			String mString="";
			if (mSup!=null&&mSup.length>0) {
				for (int i = 0; i < mSup.length; i++) {
					if (i==0) {
						mString+=mSup[i];
					}else {
						mString+="¡¢ "+mSup[i];
					}
				}
			}
			if (!TextUtils.isEmpty(mString)) {
				messe=messe.replace("*@", mString);
			}
			cancle.setText(R.string.version_update);
			mSure.setText(R.string.the_official_website);
			message.setTextColor(TheApp.mApp.getResources().getColor(R.color.white));
			message.setText(messe);
		}
		cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mcount==6) {
					if (!PublicClass.getInstance().networkAvailable(TheApp.mApp)) {
						TheApp.mApp.MyTosat(R.string.netword_abnormal);
					}else {
						Intent intent=new Intent(BootReceiver.ACTION_UPGRADE);
						TheApp.mApp.sendBroadcast(intent);
					}
					if (mSetting!=null) {
						mSetting.dismiss();
					}
				}
				mFormatsdDialog.dismiss();
			}
		});
		mSure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LogCatUtils.showString("===count=="+mcount);
				if (mcount==0) {
					dialogHandler(1);
				}else if (mcount==1) {
					//¸ñÊ½»¯TF¿¨
					if (TheApp.mIsShengMaiIC) {
						int ret=RainUvc.formatTFCard();
					}else {
						if (TheApp.mCManager==null) {
							return;
						}
						TheApp.mCManager.setUvcExtenrnCall(TheApp.getDeviceId(),"a", "1", "1");
					}
					ProgressDialog.getInstance().progressShow(R.string.str_format_title_two);
					handler.postDelayed(new Runnable() {
						public void run() {
							ProgressDialog.getInstance().progressdismiss();
							if (mSetting!=null) {
								mSetting.dismiss();
							}
							TheApp.mApp.MyTosat(R.string.fromat_sd_finish);
						}
					}, 2000);
					
				}else if (mcount==3) {
					if (TheApp.mCManager==null) {
						return;
					}
					if (TheApp.mIsShengMaiIC) {
						int ret=RainUvc.loadDefaultParam();
						if (ret>=0) {
							if (mSetting!=null) {
								mSetting.dismiss();
								TheApp.mApp.MyTosat(R.string.factory_settings_fish);
							}
						}
					}else {
						TheApp.mCManager.doStopPreview(TheApp.getDeviceId());
						TheApp.mCManager.setUvcExtenrnCall(TheApp.getDeviceId(),
								String.valueOf(61), String.valueOf(1),String.valueOf(1));
						handler.sendEmptyMessageDelayed(1, 2*1000);
						ProgressDialog.getInstance().progressShow(R.string.factory_settings);
					}
				}else if (mcount==4) {
					StartActivity.startAcitivity(SystemUpgrade.class);				
				}else if (mcount==6) {
					if (!PublicClass.getInstance().networkAvailable(TheApp.mApp)) {
						TheApp.mApp.MyTosat(R.string.netword_abnormal);
					}else {
						Uri uri = Uri.parse("http://www.carsql.com");    
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);    
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						TheApp.mApp.startActivity(intent);  
					}
					if (mSetting!=null) {
						mSetting.dismiss();
					}
				}
				if (mcount!=0) {
					mFormatsdDialog.dismiss();
				}
			}
		});
		mFormatsdDialog.setCanceledOnTouchOutside(true);
		mFormatsdDialog.show();
	}
}
