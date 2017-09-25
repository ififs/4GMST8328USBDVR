package com.syu.dvr.control;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.jar.Attributes.Name;

import com.syu.dvr.TheApp;
import com.syu.dvr.module.MediaTypeModule;
import com.syu.dvr.utils.Config;
import com.syu.dvr.utils.LogCatUtils;
import com.syu.dvr.utils.ProgressDialog;
import com.syu.dvr.widget.ComInterface.CapturePhotos;
import com.syu.dvr.widget.ComInterface.DeleteFileCallback;
import com.syu.dvr.widget.ComInterface.LoadAllDataCallback;
import com.uvc.jni.RainUvc;

import android.R.integer;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
public class FileManager {
	
	private Handler handler;
	public FileManager() {
		handler=new Handler(Looper.getMainLooper());
	}
	
	public static String getFileNameForTime(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
		return sdf.format(new Date());
	}
	public String[] getSystemTimeSetCamera(){
		String value=getFileNameForTime();
		String []time=new String[6];
		if (!value.isEmpty()) {
			time[0]=value.substring(0, 4);
			time[1]=value.substring(4, 6);
			time[2]=value.substring(6, 8);
			time[3]=value.substring(8,10);
			time[4]=value.substring(10, 12);
			time[5]=value.substring(12, 14);
		}
		return time;
	}
	public String[] getSystemTimeToCamera(int flag){
		flag=1;
		
		if (flag!=0&&flag!=1) {
			flag=1;
		}
		String []time=new String[6];
		String value="";
		if (flag==1) {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
			value=sdf.format(new Date());
			
		}else if (flag==0) {
			SimpleDateFormat sdf2=new SimpleDateFormat("yyyyMMddhhmmss", Locale.getDefault());
			value=sdf2.format(new Date());
		}
		if (!value.isEmpty()) {
			time[0]=value.substring(2, 4);
			time[1]=value.substring(4, 6);
			time[2]=value.substring(6, 8);
			time[3]=value.substring(8,10);
			
			if (flag==1) {
				
				time[3]=String.valueOf(Integer.valueOf(value.substring(8, 10)) & 0xbf);
				
			}else if (flag==0) {
				time[3]=String.valueOf(Integer.valueOf(value.substring(8, 10)) | 0x40 );
			}
			time[4]=value.substring(10, 12);
			time[5]=value.substring(12, 14);
		}
		return time;
	}
	private int getValueOf(String value){
		return Integer.valueOf(value, 16);
	}
	public String getFileCreateTime(String time){
			String mCreatTime="";
			if (time.startsWith("VID")||time.startsWith(Config.PHOTO_MARK)) {
				mCreatTime=time.substring(11, 17);
			}
		return mCreatTime;
	}
	public static String getHandleTime(){
		SimpleDateFormat sdf=new SimpleDateFormat("HHmmssSSS", Locale.getDefault());
		return sdf.format(new Date());
	}
	public String TimeConVersion(String timString){
		return timString.substring(0,2)+":"+timString.substring(2, 4)+":"+timString.substring(4, 6);
	}
	int type;String name;
	public void DeleteFile(final DeleteFileCallback callback){
		final List<String>list=TheApp.mSeleNameList;
		new Thread(new Runnable() {
			public void run() {
				for (int i = 0; i < list.size(); i++) {
					if (TheApp.mIsShengMaiIC) {
						name=list.get(i);
						type=getTypeForName(name);
						if (type>=0&&!name.isEmpty()) {
							RainUvc.deleteFile(type,name);
						}else {
							continue;
						}
					}else {
						File mFile=new File(list.get(i));
						if (mFile.exists()) {
							mFile.delete();
						}
					}
					
				}
				
				TheApp.mSysmanager.getDataSeleTime(new LoadAllDataCallback() {
					
					@Override
					public void callback(List<String> listData, List<String> mDataTime,List<String>
					mLockTime) {
						TheApp.mApp.setmFileNameList(listData);
						TheApp.mApp.setmDataTime(mDataTime);
						TheApp.mApp.setmLockTime(mLockTime);
						callback.callback();
					}
				});
			}
		}).start();
	}
	public int getTypeForName(String name){
		if (name.endsWith(".JPG")) {
			return 3;
		}else if (name.endsWith(".mp4")&&name.startsWith("ch0")) {
			if (MediaTypeModule.getInstance().getmEvent()!=null&&
					MediaTypeModule.getInstance().getmEvent().contains(name)) {
				return 1;
			}else {
				return 0;
			}
		}
		return -1;
	}
	public void DeleteFileUp(final DeleteFileCallback callback){
		final List<String>list=TheApp.mSeleNameList;
		ProgressDialog.getInstance().setmSetuptime(1+(int) (list.size()*1.5));
		new Thread(new Runnable() {
			public void run() {
				if (TheApp.mIsShengMaiIC) {
					for (int i = 0; i < list.size(); i++) {
						name=list.get(i);
						type=getTypeForName(name);
						if (type>=0&&!name.isEmpty()) {
							long temp=System.currentTimeMillis();
							RainUvc.deleteFile(type,name);
							LogCatUtils.showString("==temp=="+(System.currentTimeMillis()-temp));
						}
					}
				}else {
					for (int i = 0; i < list.size(); i++) {
						File mFile=new File(list.get(i));
						if (mFile.exists()) {
							mFile.delete();
					}
				}
			}
			callback.callback();
		}
		}).start();
	}
	
	public void saveCapturePhotos(Bitmap bitmap, CapturePhotos callball){
		if(new File("/sdcard").exists()){
			String path="/sdcard";
		try {
			String mFilepath=path+File.separator+
					"IMG"+getFileNameForTime()+Config.FILE_PHOTO;
				BufferedOutputStream outputStream=new BufferedOutputStream(new FileOutputStream(mFilepath));
				bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
				outputStream.write(new byte[1024]);
				outputStream.flush();
				outputStream.close();
				if (callball==null) {
					return;
				}
				callball.capturePhoto(1,mFilepath);
		} catch (IOException e) {
			if (callball==null) {
				return;
			}
			callball.capturePhoto(-1,null);
		}
	
		}
		
	}
}
