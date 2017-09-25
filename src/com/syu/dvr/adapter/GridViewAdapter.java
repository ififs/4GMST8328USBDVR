package com.syu.dvr.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.syu.dvr.R;
import com.syu.dvr.TheApp;
import com.syu.dvr.activity.CameraPlayBackActivity;
import com.syu.dvr.activity.FillActivity;
import com.syu.dvr.control.FileManager;
import com.syu.dvr.control.StartActivity;
import com.syu.dvr.imageloader.VideoImageLoader;
import com.syu.dvr.module.MediaInfor;
import com.syu.dvr.module.MediaTypeModule;
import com.syu.dvr.utils.Config;
import com.syu.dvr.utils.ImageLoader;
import com.syu.dvr.utils.LoadThumbnails;
import com.syu.dvr.utils.LogCatUtils;
import com.syu.dvr.utils.PublicClass;
import com.syu.dvr.widget.MGridView;
import com.uvc.jni.RainUvc;

public class GridViewAdapter extends BaseAdapter{
	private Context context;
	private boolean isSelete=false;
	public int selposition;
	private FileManager mFileManager;
	private List<ViewHolder>mViewHolders;
	private List<MediaInfor>VideoList=new ArrayList<MediaInfor>();
	private Bitmap bitmap;
	private  boolean isLock;
	private List<String>mEvent;
	private FileManager fileManager=new FileManager();
	public List<MediaInfor> getVideoList() {
		return VideoList;
	}

	public void setVideoList(List<MediaInfor> mvideoList) {
		VideoList.clear();
		VideoList.addAll(mvideoList);
		
	}

	public GridViewAdapter() {
		this.context=TheApp.mApp;
		mFileManager=new FileManager();
		mEvent=MediaTypeModule.getInstance().getmEvent();
		bitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.def_image);
		mViewHolders=TheApp.getmHolders();
		if (TheApp.mApp.fileStatu.isAllSele()&&VideoList!=null) {
			TheApp.mApp.fileStatu.setAllSele(false);
		}
	}
	@Override
	public int getCount() {
		
		if (VideoList==null) {
			return 0;
		}else {
			
			return VideoList.size();
		}
	}

	@Override
	public Object getItem(int position) {
		
		return VideoList.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	boolean iswhow=false;
	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		
		if (view==null) {
			view=LayoutInflater.from(context).inflate(R.layout.layout_choose_item, null);
			holder=new ViewHolder();
			holder.image=(ImageView) view.findViewById(R.id.image_thumb);
			holder.mText=(TextView) view.findViewById(R.id.text_name);
			holder.mLock=(ImageView) view.findViewById(R.id.image_lock);
			holder.mSele=(ImageView) view.findViewById(R.id.image_selet);
			holder.mPlay=(ImageView) view.findViewById(R.id.image_play);
			holder.layout=(RelativeLayout) view.findViewById(R.id.thumb_layout);
			view.setTag(holder);
		}else {
			holder=(ViewHolder) view.getTag();
		}
		if (parent instanceof MGridView) {
			if (((MGridView) parent).isOnMeasure) {
				return view;
			}
		}
		
		 final MediaInfor infor=VideoList.get(position);
		 if (TheApp.mIsShengMaiIC) {
			 isLock=(mEvent!=null&&mEvent.contains(infor.getName()));
		 }else {
			 isLock=infor.getName().startsWith(Config.FILE_FORMAT_C);
		 }
		 infor.setLock(isLock);
		if ((infor!=null)&&(infor.getName()!=null)) {
			if (infor.getName().endsWith(Config.FILE_FORMAT)
					||infor.getName().endsWith(Config.FILE_RECORDING)) {
				holder.mText.setText(mFileManager.TimeConVersion(infor.getCreatTime()));
				holder.mPlay.setVisibility(View.VISIBLE);
				holder.image.setImageBitmap(bitmap);
				if (TheApp.mIsShengMaiIC) {
					LoadThumbnails.getInstance().loadImageThumbnails(infor.getName(),holder.image,infor.getName());
				}
			}else if(infor.getName().endsWith(Config.FILE_PHOTO)&&((infor.getPath()!=null)||TheApp.mIsShengMaiIC)) {
				holder.mText.setText(mFileManager.TimeConVersion(infor.getCreatTime()));
				holder.mPlay.setVisibility(View.GONE);
				if (!TheApp.mIsShengMaiIC) {
					LoadThumbnails.getInstance().loadImageThumbnails(infor.getPath(),holder.image,infor.getName());
				}else if (TheApp.mIsShengMaiIC) {
					LoadThumbnails.getInstance().loadImageThumbnails(infor.getName(),holder.image,infor.getName());
				}
			}
		}
		if (TheApp.mApp.fileStatu.isExit()) {
			
			holder.mSele.setClickable(true);
			holder.mSele.setVisibility(View.VISIBLE);
			if (isLock) {
				holder.mLock.setVisibility(View.VISIBLE);
				holder.mLock.setClickable(true);
			}
		}

		if (isLock) {
			holder.mText.setTextColor(TheApp.mApp.getResources().getColor(R.color.oldlace));
			holder.isLock=true;
		}
		if (mViewHolders!=null&&!mViewHolders.equals(holder)) {
			mViewHolders.add(holder);
			TheApp.setmHolders(mViewHolders);
		}
		
		setSeleBG(holder,infor);
		
		holder.image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (infor==null||infor.getName()==null) {
					return;
				}
				if (!TheApp.mIsShengMaiIC) {
					if (infor.getPath()==null) {
						return;
					}
				}
				if (!TheApp.mApp.fileStatu.isExit()) {
					if (TheApp.mIsShengMaiIC) {
						if (infor.getName().endsWith(".mp4")) {
							Bundle bundle=new Bundle();
							bundle.putString("playName", infor.getName());
							int type=mFileManager.getTypeForName(infor.getName());
							LogCatUtils.showString("==type=="+type);
							if (RainUvc.isFileExist(type, infor.getName())>0) {
								TheApp.mPlayOrDelete=true;
								StartActivity.startAcitivity(CameraPlayBackActivity.class, bundle);
							}
						}else if (infor.getName().endsWith(".JPG")&&RainUvc.isFileExist(3,infor.getName())>0) {
							Bundle bundle=new Bundle();
							bundle.putString("data", infor.getName());
							StartActivity.startAcitivity(FillActivity.class, bundle);
						}
						return;
					}
					if (infor.getName().endsWith(Config.FILE_FORMAT)) {
						File file=new File(infor.getPath());
						if (file.exists()) {
							TheApp.mApp.playMeideo(infor.getPath());
						}
					}else if (infor.getName().endsWith(Config.FILE_PHOTO)) {
						File file=new File(infor.getPath());
						if (file.exists()) {
							Bundle bundle=new Bundle();
							bundle.putString("data", infor.getPath());
							TheApp.mPlayOrDelete=true;
							StartActivity.startAcitivity(FillActivity.class, bundle);
						}
					}
				}else {
					mEditData(holder, infor,position);
				}
			}
		});
		holder.mSele.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				mEditData(holder, infor,position);
			}
		});
		holder.mLock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mEditData(holder, infor,position);
			}
		});
		return view;
	}
	public void mEditData(final ViewHolder holder,final MediaInfor infor,int post){
		if (TheApp.mIsShengMaiIC) {
			isSelete=(TheApp.mSeleNameList.indexOf(infor.getName())==-1);
		}else {
			isSelete=(TheApp.mSeleNameList.indexOf(infor.getPath())==-1);
		}
		if (/*infor.getName().startsWith(Config.FILE_FORMAT_C)*/infor.isLock()&&isSelete) {
			int []size;
	    	size=PublicClass.getInstance().getWindowManeger();
	    	if (size==null||size.length!=2) {
	    		size=new int[]{1024,600};
			}	
			LinearLayout.LayoutParams params=new LayoutParams(384,240);
			View view =LinearLayout.inflate(TheApp.mApp, R.layout.format_sd_layout, null);
			final Dialog mFormatsdDialog=new Dialog(TheApp.mApp,R.style.add_dialog);
			mFormatsdDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
			mFormatsdDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mFormatsdDialog.setContentView(view,params);
			TextView cancle=(TextView) mFormatsdDialog.findViewById(R.id.dialog_cancle);
			TextView message=(TextView) mFormatsdDialog.findViewById(R.id.dialog_messege);
			TextView mTittle=(TextView) mFormatsdDialog.findViewById(R.id.dialog_tittle);
			mTittle.setText(R.string.str_tip_title);
			message.setText(R.string.str_file_islock_dele);
			cancle.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					isSelete=false;
					mFormatsdDialog.dismiss();
				}
			});
			mFormatsdDialog.findViewById(R.id.dialog_sure).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					seleHandler(holder,infor);
					mFormatsdDialog.dismiss();
				}
			});
			mFormatsdDialog.setCanceledOnTouchOutside(true);
			mFormatsdDialog.show();	
		
		}else {
			
			seleHandler(holder,infor);
		}
	
	}
	public void seleHandler(ViewHolder holder,final MediaInfor infor){
	if (isSelete) {
			if (TheApp.mIsShengMaiIC) {
				TheApp.mSeleNameList.add(infor.getName());
			}else {
				TheApp.mSeleNameList.add(infor.getPath());
			}
			holder.mSele.setBackgroundResource(R.drawable.image_edit_selet_p);		
		}else {
			if (TheApp.mIsShengMaiIC) {
				TheApp.mSeleNameList.remove(infor.getName());
			}else {
				TheApp.mSeleNameList.remove(infor.getPath());
			}
			holder.mSele.setBackgroundResource(R.drawable.image_edit_selet);	
		}
	}
	
	public void setSeleBG(ViewHolder holder,MediaInfor infor){
		if (TheApp.mSeleNameList.size()==0) {
			return;
		}
		if (TheApp.mIsShengMaiIC) {
			if (TheApp.mSeleNameList.contains(infor.getName())) {
				holder.mSele.setBackgroundResource(R.drawable.image_edit_selet_p);
			}else {
				holder.mSele.setBackgroundResource(R.drawable.image_edit_selet);
			}
		}else {
			if (TheApp.mSeleNameList.contains(infor.getPath())) {
				holder.mSele.setBackgroundResource(R.drawable.image_edit_selet_p);
			}else {
				holder.mSele.setBackgroundResource(R.drawable.image_edit_selet);
			}
		}
	}
	
	public static class ViewHolder{
		public ImageView image;
		public TextView mText;
		public ImageView mLock;
		public ImageView mSele;
		public ImageView mPlay;
		public boolean isLock=false;
		public RelativeLayout layout;
	}
	
}
