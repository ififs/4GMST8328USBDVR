package com.syu.dvr.widget;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.syu.dvr.R;
import com.syu.dvr.TheApp;
import com.syu.dvr.activity.EditFileActivity;
import com.syu.dvr.adapter.GridViewAdapter;
import com.syu.dvr.control.FileManager;
import com.syu.dvr.module.MediaInfor;
import com.syu.dvr.module.MediaTypeModule;
import com.syu.dvr.utils.Config;
import com.syu.dvr.utils.LogCatUtils;
import com.syu.dvr.utils.ProgressDialog;
import com.syu.dvr.widget.ComInterface.DeleteFileCallback;

public class EditFileLayout extends LinearLayout{
	
	private TextView mEdit;
	private TextView mAllSele;
	private TextView mDelete;
	private TextView mCancle;
	private MyClick click;
	private Handler handler;
	private FileManager mFileManager;
	private List<List<MediaInfor>>allDatalist;
	private List<String>allTimeList;
	private int[] mNames=new int[]{R.string.str_text_edit,R.string.str_text_allsele,
			R.string.str_text_delete,R.string.str_tip_cancle};
	private TextView []mTextV;
	private LinearLayout.LayoutParams params;
	public EditFileLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		handler=new Handler();
		params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.weight=1;
		click=new MyClick();
		mFileManager=new FileManager();
		initEditFileLayout(context);
	}
	private void initEditFileLayout(Context context) {
		mEdit=new TextView(context);
		mAllSele=new TextView(context);
		mDelete=new TextView(context);
		mCancle=new TextView(context);
		mTextV=new TextView[]{mEdit,mAllSele,mDelete,mCancle};
		for (int i = 0; i <mTextV.length; i++) {
			mTextV[i].setGravity(Gravity.CENTER);
			mTextV[i].setTextSize(20);
			mTextV[i].setText(mNames[i]);
			mTextV[i].setId(0x1000+i);
			mTextV[i].setOnClickListener(click);
			mTextV[i].setBackgroundResource(R.drawable.select_bg);
			addView(mTextV[i], params);
		}
	}
	private FileEditCallback callback;
	public void registerCallback(FileEditCallback callback){
		this.callback=callback;
	}
	public void unregisterCallback(){
		callback=null;
	}
	class MyClick implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case 0x1000:
				handler.post(new Runnable() {
					@Override
					public void run() {
						List<GridViewAdapter.ViewHolder>mHolders=TheApp.getmHolders();
						if (mHolders!=null&&mHolders.size()>0) {
							for (int i = 0; i < mHolders.size(); i++) {
								GridViewAdapter.ViewHolder viewHolder=mHolders.get(i);
								viewHolder.mSele.setVisibility(View.VISIBLE);
								if (viewHolder.isLock) {
									viewHolder.mLock.setVisibility(View.VISIBLE);
								}
							}
						}
						TheApp.mApp.fileStatu.setExit(true);
					}
				});
				break;
			case 0x1001:
				if (!TheApp.mApp.fileStatu.isExit()) {
					return;
				}
				new	Thread( new Runnable() {
					public void run() {
						if (allDatalist==null||allDatalist==null||
								allDatalist.size()>allTimeList.size()) {
							return;
						}
						for (int i = 0; i < allTimeList.size(); i++) {
							List<MediaInfor>tempList=allDatalist.get(i);
							for (int j = 0; j < tempList.size(); j++) {
								MediaInfor infor=tempList.get(j);
								if (TheApp.mIsShengMaiIC) {
									if (infor!=null&&infor.getName()!=null&&(!MediaTypeModule.getInstance()
											.getmEvent().contains(infor.getName()))) {
										TheApp.mSeleNameList.add(infor.getName());
									}
								}else {
									
									if ((infor!=null)&&(infor.getName()!=null)&&(infor.getPath()
											!=null)&&(!infor.getName().startsWith(Config.FILE_FORMAT_C))) {
										if (TheApp.mSeleNameList.indexOf(infor.getPath())==-1) {
											TheApp.mSeleNameList.add(infor.getPath());
										}
									}
								}
							}
							
						}	
						handler.post(new Runnable() {
							public void run() {
								List<GridViewAdapter.ViewHolder>mHolders=TheApp.getmHolders();
								if (mHolders!=null&&mHolders.size()>0) {
									for (int i = 0; i < mHolders.size(); i++) {
										GridViewAdapter.ViewHolder viewHolder=mHolders.get(i);
										viewHolder.mSele.setVisibility(View.VISIBLE);
										if (!viewHolder.isLock) {
											viewHolder.mSele.setBackgroundResource(R.drawable.image_edit_selet_p);
										}
									}
								}
								TheApp.mApp.fileStatu.setAllSele(true);
							}
						});
						
					}
					
				}).start();
				
				break;
			case 0x1002:
				handler.post(new Runnable() {
					public void run() {
						if (TheApp.mSeleNameList.size()<=0) {
							return;
						}
						TheApp.mPlayOrDelete=true;
						ProgressDialog.getInstance().progressShow(R.string.str_delete_file);
						TheApp.mApp.fileStatu.setExit(false);
						List<GridViewAdapter.ViewHolder>mHolders=TheApp.getmHolders();
						if (mHolders!=null&&mHolders.size()>0) {
							for (int i = 0; i < mHolders.size(); i++) {
								GridViewAdapter.ViewHolder viewHolder=mHolders.get(i);
								viewHolder.mSele.setBackgroundResource(R.drawable.image_edit_selet);
								viewHolder.mSele.setVisibility(View.GONE);
								if (viewHolder.isLock) {
									viewHolder.mLock.setVisibility(View.GONE);
								}
							}
						}
						mFileManager.DeleteFileUp(new DeleteFileCallback() {
							@Override
							public void callback() {
								if (callback!=null) {
									callback.deleteFileCallback();
								}
							}
						});
					}
				});
				break;
			case 0x1003:
				cancelEdit();
				break;
				
			default:
				break;
			}
			
		}
	}
	public void cancelEdit(){
		TheApp.mApp.fileStatu.setExit(false);
		handler.post(new Runnable() {
			public void run() {
				List<GridViewAdapter.ViewHolder>mHolders=TheApp.getmHolders();
				if (mHolders!=null&&mHolders.size()>0) {
					for (int i = 0; i < mHolders.size(); i++) {
						GridViewAdapter.ViewHolder viewHolder=mHolders.get(i);
						viewHolder.mSele.setBackgroundResource(R.drawable.image_edit_selet);
						viewHolder.mSele.setVisibility(View.GONE);
						if (viewHolder.isLock) {
							viewHolder.mLock.setVisibility(View.GONE);
						}
					}
				}
				TheApp.mSeleNameList.clear();
				TheApp.mApp.fileStatu.setAllSele(false);
				TheApp.mApp.fileStatu.setExit(false);
			}
		});
	}
	public void setAllData(List<List<MediaInfor>>allDatalist,List<String>allTimeList){
		this.allDatalist=allDatalist;
		this.allTimeList=allTimeList;
	}
	public interface FileEditCallback{
		public void deleteFileCallback();
	}
}
