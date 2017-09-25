package com.syu.dvr.upgrade;

import java.io.File;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest.permission;
import android.app.Dialog;
import android.os.Handler;
import android.os.ISchedulingPolicyService;
import android.os.SystemProperties;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.syu.dvr.R;
import com.syu.dvr.TheApp;
import com.syu.dvr.upgrade.OkhttpDownLoadManage.NetworkCheckCallback;
import com.syu.dvr.utils.LogCatUtils;
import com.syu.dvr.utils.ProgressDialog;
import com.syu.dvr.utils.PublicClass;
import com.syu.dvr.widget.DownLoadLayout;

public class UpgradeManager {
	String url="http://apk.carsql.com/CarMarke/getCount.action?customerid=88&platformId=3&nomas=1280x400&ui=2";
	String downLoadString="http://apk.carsql.com/CarMarke/downAction.action?uid=APPID&flag=apk";
	public static UpgradeManager manager;
	private String mDownLoadFile="dvr@V.apk";
	private String mDownLoadDir="/sdcard/";
	private long startPoints=0;
	public static UpgradeManager getInstance(){
		if (manager==null) {
			synchronized (UpgradeManager.class) {
				if (manager==null) {
					manager=new UpgradeManager();
				}
			}
		}
		return manager;
	}
	private boolean mBackStage;
	public void netWordCheck(){
		mBackStage=false;
		startCheck();
	}
	public void BackStageNetWordCheck(){
		mBackStage=true;
		startCheck();
	}
	private void startCheck(){
		if (!PublicClass.getInstance().networkAvailable(TheApp.mApp)) {
			return ;
		}
		if (!mBackStage) {
			ProgressDialog.getInstance().progressShow(R.string.netword_check_is_has_up);
		}
		checkServiceVersion();
	}
	private String mAppVersion="";
	private String mAppVersionname="";
	private int mAPPid;
	private String mAppinfo="";
	private String mAppLength="";
	private boolean checkIsDownLoading(){
		boolean isDownLoad=OkhttpDownLoadManage.getInstance().isDownLoad();
		if (isDownLoad) {
			dialogHandler();
		}
		return isDownLoad;
	}
	private void checkServiceVersion(){
		try {
			if (checkIsDownLoading()) {
				ProgressDialog.getInstance().progressdismiss();
				return;
			}
			OkhttpDownLoadManage.getInstance().getAsString(url, new NetworkCheckCallback() {
				
				@Override
				public void networkcallback(String message) {
					LogCatUtils.showString("==message==="+message);
					if (!message.isEmpty()&&!message.contains("failure")) {
						JSONObject jObject;
						try {
							jObject=new JSONObject(message);
							if (jObject.has("Allapp")&&jObject.has("states")&&(jObject.getInt("states")==1)) {
								JSONArray object=jObject.getJSONArray("Allapp");
								if (object.length()>0) {
									jObject=(JSONObject) object.get(0);
									if (jObject.has("appVersion")) {
										mAppVersion=jObject.getString("appVersion");
									}
									if (jObject.has("appVersionname")) {
										mAppVersionname=jObject.getString("appVersionname");
									}
									if (jObject.has("appId")) {
										mAPPid=jObject.getInt("appId");
									}
									if (jObject.has("appLength")) {
										mAppLength=jObject.getString("appLength");
									}
									if (jObject.has("appinfo")) {
										JSONObject appinfo=jObject.getJSONObject("appinfo");
										if (appinfo.has("aiSimpleinfo")) {
											mAppinfo=appinfo.getString("aiSimpleinfo");
										}
									}
								}
							}else {
								handler.sendEmptyMessage(6);
								return;
							}
						} catch (JSONException e) {
							e.printStackTrace();
							handler.sendEmptyMessage(6);
							return;
						}
						if (!mAppVersion.isEmpty()&&mAppVersion.length()>1) {
							long local=Long.parseLong(TheApp.mApp.getVersions()[1]);
							LogCatUtils.showString("local==="+local);
							LogCatUtils.showString("==logn="+Long.parseLong(mAppVersion));
							if (local>=Long.parseLong(mAppVersion)) {
								handler.sendEmptyMessage(1);
								return;
							}
						}
						mDownLoadFile=mDownLoadFile.replace("@V",mAppVersion);
						File file=new File(mDownLoadDir+mDownLoadFile+"_temp");
						if (file.exists()) {
							startPoints=file.length();
							handler.sendEmptyMessage(2);
							return;
						}
						file=new File(mDownLoadDir+mDownLoadFile);
						if (file.exists()) {
							handler.sendEmptyMessage(4);
							return;
						}
						downLoad=null;
						view=null;
						handler.sendEmptyMessage(5);
						return;
					}else {
						handler.sendEmptyMessage(0);
					}
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (mBackStage&&(msg.what!=5||msg.what!=3)) {
				return;
			}
			switch (msg.what) {
			case 0:
				ProgressDialog.getInstance().progressdismiss();
				TheApp.mApp.MyTosat(R.string.netword_abnormal);
				break;
			case 1:
				ProgressDialog.getInstance().progressdismiss();
				TheApp.mApp.MyTosat(R.string.latest_version);
				break;
			case 2:
				ProgressDialog.getInstance().progressdismiss();
				dialogHandler();
				downloadUi(2);
				break;
			case 3:
				ProgressDialog.getInstance().progressdismiss();
				layout.setVisibility(View.VISIBLE);
				mFileDownload();
				break;
			case 4:
				ProgressDialog.getInstance().progressdismiss();
				dialogHandler();
				downloadUi(4);
				break;
			case 5:
				ProgressDialog.getInstance().progressdismiss();
				dialogHandler();
				downloadUi(5);
				break;
			case 6:
				ProgressDialog.getInstance().progressdismiss();
				TheApp.mApp.MyTosat(R.string.update_error);
				break;
			default:
				break;
			}
		}
	};
	 
	private TextView message;
	private TextView mTittle;
	private TextView cancle;
	private TextView mSure;
	private DownLoadLayout layout;
	
	private View view;
	private LinearLayout callLayout;
	private View getViewDownLoad(){
		if (view==null) {
			String def=TheApp.mApp.getString(R.string.app_info_def);
			view =LinearLayout.inflate(TheApp.mApp, R.layout.upgrade_manager_layout, null);
			layout=(DownLoadLayout) view.findViewById(R.id.download_layout);
			callLayout=(LinearLayout) view.findViewById(R.id.call_sure_layout);
			cancle=(TextView) view.findViewById(R.id.dialog_cancle);
			message=(TextView) view.findViewById(R.id.dialog_messege);
			mTittle=(TextView) view.findViewById(R.id.dialog_tittle);
			mSure=(TextView) view.findViewById(R.id.dialog_sure);
			message.setMovementMethod(ScrollingMovementMethod.getInstance());
			String string=TheApp.mApp.getString(R.string.new_software_messages);
			string=string.replace("@V", (mAppVersionname.isEmpty()?def:mAppVersionname)).
					replace("@M", (mAppinfo.isEmpty()?def:mAppinfo)).
					replace("@S", (mAppLength.isEmpty()?def:mAppLength));
			message.setText(string);
		}
		return view;
	}
	private Dialog downLoad;
	private void dialogHandler(){
		if (downLoad!=null) {
			downLoad.show();
			return;
		}
		int []size;
		size=PublicClass.getInstance().getWindowManeger();
		if (size==null||size.length!=2) {
			size=new int[]{1024,600};
		}
		ViewGroup.LayoutParams params= new ViewGroup.LayoutParams(size[0]/2, size[1]/2);
		downLoad=new Dialog(TheApp.mApp,R.style.add_dialog);
		downLoad.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		downLoad.requestWindowFeature(Window.FEATURE_NO_TITLE);
		downLoad.setContentView(getViewDownLoad(),params);
		layout.setDialog(downLoad);
		downLoad.setCanceledOnTouchOutside(true);
		downLoad.show();
	}
	private void downloadUi(final int count){
		if (count==2) {
			mTittle.setText(R.string.file_is_loading);
			layout.setVisibility(View.VISIBLE);
			message.setVisibility(View.GONE);
			callLayout.setVisibility(view.VISIBLE);
			cancle.setVisibility(View.GONE);
			mSure.setVisibility(View.VISIBLE);
			mSure.setText(R.string.continue_loading);
		}else if (count==3) {
			layout.setVisibility(View.VISIBLE);
			mTittle.setText(R.string.file_is_loading);
			callLayout.setVisibility(View.GONE);
			message.setVisibility(View.GONE);
		}else if (count==4) {
			mTittle.setText(R.string.file_is_loading);
			message.setVisibility(View.GONE);
			callLayout.setVisibility(View.VISIBLE);
			cancle.setVisibility(View.GONE);
			mSure.setText(R.string.install);
			
		}else if(count==5){
			String string=TheApp.mApp.getString(R.string.new_software_toup);
			if (!string.isEmpty()) {
				String []message=string.split("@");
				if (message!=null&&message.length>1) {
					cancle.setText(message[1]);
					mSure.setText(message[0]);
				}
			}
		}
		mSure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (count==5) {
					handler.sendEmptyMessage(3);
					downloadUi(3);
				}else if (count==2) {
					mFileDownload();
					callLayout.setVisibility(View.GONE);
				}else if (count==4) {
					PublicClass.getInstance().openFile(new File(mDownLoadDir+mDownLoadFile));
//					PublicClass.getInstance().installSlient(new File(mDownLoadDir+mDownLoadFile));
					downLoad.dismiss();
				}
				
			}
		});
		cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				downLoad.dismiss();
			}
		});
	}
	private void mFileDownload(){
		OkhttpDownLoadManage.getInstance().downloadAsyn(downLoadString.
				replace("APPID", String.valueOf(mAPPid)),
				mDownLoadDir, mDownLoadFile, startPoints, layout);
	}
}
