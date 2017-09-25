package com.syu.dvr.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.TextView;

import com.syu.dvr.R;
import com.syu.dvr.TheApp;
import com.syu.dvr.adapter.VFileAdapter;
import com.syu.dvr.module.MediaInfor;
import com.syu.dvr.module.VideoInfo;
import com.syu.dvr.utils.LogCatUtils;
import com.syu.dvr.utils.ProgressDialog;
import com.syu.dvr.widget.ComInterface.AllMediaFileCallback;
import com.syu.dvr.widget.EditFileLayout;
import com.syu.dvr.widget.EditFileLayout.FileEditCallback;
import com.uvc.jni.RainUvc;

public class EditFileActivity extends BaseActivity implements FileEditCallback{
	public ListView mList;
	public VFileAdapter adapter;
	public List<VideoInfo> lists;
	public static EditFileActivity context;
	private List<List<MediaInfor>>allDatalist;
	private List<String>allTimeList;
	private List<List<MediaInfor>>newdatalist;
	private List<String>newTimeList;
	public String mSeleTime;
	public TextView mDeleTex;
	public static final int  DELETE_DATA=0X111;
	public static final int EDIT_DATA   =0112;
	public static final int CANCLE_EDIT =0X123;
	public static final int EDIT_FISH =0X128;
	public static final int CANLE_DATA=0X124;
	public static final int SWITCH_DATA=0X125;
	public static final int MSELE_ALL=0X126;
	public static final int ADD_DATA=0x000;
	List<MediaInfor>list;
	List<MediaInfor>newMediainfo;
	private int type;
	private String name;
	private int retValue;
	private EditFileLayout mEditLayout;
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		addActivity(this);
		setContentView(R.layout.layout_edit_file);
		context=EditFileActivity.this;
		Intent intent=getIntent();
		if (intent.hasExtra("TimeData")) {
			Bundle bundle=intent.getBundleExtra("TimeData");
			if (bundle.containsKey("data")) {
				mSeleTime=bundle.getString("data");
			}
		}
		initView();
	}
	private void initView() {
		mList=(ListView)findViewById(R.id.recList);
		mEditLayout=(EditFileLayout) findViewById(R.id.id_edit_layout);
		initData();
	}
	@Override
	protected void onResume() {
		super.onResume();
		mEditLayout.registerCallback(this);
		TheApp.mPlayOrDelete=false;
	}
	@Override
	protected void onStop() {
		super.onStop();
		mEditLayout.unregisterCallback();
	}
	private void initData() {
		allDatalist=new ArrayList<List<MediaInfor>>();
		allTimeList=new ArrayList<String>();
		newTimeList=new ArrayList<String>();
		newdatalist=new ArrayList<List<MediaInfor>>();
		adapter=new VFileAdapter(TheApp.mApp, 
				allDatalist,allTimeList);
		mList.setAdapter(adapter);
		ProgressDialog.getInstance().progressShow(R.string.str_add_file);
		new Thread(new  Runnable() {
			public void run() {
				TheApp.mSysmanager.getAllVideoFile(mSeleTime,new AllMediaFileCallback() {
					@Override
					public void getFileCallback(List<String> times,
							List<List<MediaInfor>> dataList) {
						allDatalist.clear();
						allDatalist.addAll(dataList);
						allTimeList.clear();
						allTimeList.addAll(times);
						handler.sendEmptyMessage(ADD_DATA);
					}
				});
			}
		}).start();
	}
	private Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ADD_DATA:
				addData();
				break;
			case EDIT_FISH:
				TheApp.mPlayOrDelete=false;
				TheApp.mSeleNameList.clear();
				TheApp.mApp.fileStatu.setAllSele(false);
				TheApp.mApp.fileStatu.setExit(false);
				sendEmptyMessage(ADD_DATA);
				break;
			default:
				break;
			}
		}
	};
	private void addData(){
		if (allDatalist!=null&&allTimeList!=null) {
			adapter.notifyDataSetChanged();
			mEditLayout.setAllData(allDatalist, allTimeList);
		}
		ProgressDialog.getInstance().progressdismiss();
	}
	@Override
	public void deleteFileCallback() {
		newdatalist.clear();
		newTimeList.clear();
		upDataup();
		handler.sendEmptyMessageDelayed(EDIT_FISH, 0);
	}
	private void upDataup(){
		for (int i = 0; i < allDatalist.size(); i++) {
			list=allDatalist.get(i);
			newMediainfo=new ArrayList<MediaInfor>();
			for (int j = 0; j < list.size(); j++) {
				MediaInfor mediaInfor=list.get(j);
				if (TheApp.mIsShengMaiIC) {
					name=mediaInfor.getName();
					if (name.isEmpty()) {
						continue;
					}
					if (name.endsWith(".mp4")) {
						if (mediaInfor.isLock()) {
							type=1;
						}else {
							type=0;
						}
					}else if (name.endsWith(".JPG")) {
						type=3;
					}
					retValue=RainUvc.isFileExist(type, name);
					if (retValue>0) {
						newMediainfo.add(mediaInfor);
						continue;
					}
				}else {
					File file=new File(mediaInfor.getPath());
					if (file.exists()&&file.length()>0) {
						newMediainfo.add(mediaInfor);
					}
				}
			}
			if (newMediainfo.size()>0) {
				newdatalist.add(newMediainfo);
				if (allTimeList.size()>i) {
					newTimeList.add(allTimeList.get(i));
				}
			}
		}
		allDatalist.clear();
		allTimeList.clear();
		allDatalist.addAll(newdatalist);
		allTimeList.addAll(newTimeList);
	}
	@Override
	public boolean onKeyDown(int arg0, KeyEvent arg1) {
		if (arg0==KeyEvent.KEYCODE_BACK&&arg1.getAction()==KeyEvent.ACTION_DOWN) {
			if (TheApp.mApp.fileStatu.isExit()&&mEditLayout!=null) {
				mEditLayout.cancelEdit();
				return true;
			}else {
				finish();
			}
		}
		return false;
	}
}