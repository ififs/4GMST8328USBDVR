package com.syu.dvr.control;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes.Name;

import com.syu.dvr.R;
import com.syu.dvr.TheApp;
import com.syu.dvr.module.FileInfor;
import com.syu.dvr.module.MediaInfor;
import com.syu.dvr.module.MediaTypeModule;
import com.syu.dvr.utils.Config;
import com.syu.dvr.utils.LogCatUtils;
import com.syu.dvr.widget.ComInterface.AllMediaFileCallback;
import com.syu.dvr.widget.ComInterface.LoadAllDataCallback;
import com.uvc.jni.RainUvc;

import android.R.integer;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.text.TextUtils;

public class SystemManager {
	public List<String> list = new ArrayList<String>();
	public List<String>noSave=new ArrayList<String>();
	public List<String>pathList=new ArrayList<String>();
	private List<String>mDataTime=new ArrayList<String>();
	private List<String>mLockFile=new ArrayList<String>();
	private static String ABLE = Environment.MEDIA_MOUNTED;
	private String path="";
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
//		this.path = getSdPath(path);
		
		File file=new File(path);
		if (!file.exists()) {
			return ;
		}
		File []files=file.listFiles();
		if (files.length==0) {
			
			return ;
		}
		int osVersion = Integer.valueOf(android.os.Build.VERSION.SDK_INT);
		LogCatUtils.showString("===osVersion=="+osVersion);
		for (File tempFile:files) {
			File newFile=new File(path+(osVersion>=23?"":File.separator+tempFile.getName())+File.separator
					+Config.NORMAL);
			LogCatUtils.showString("==newFile=="+newFile.getAbsolutePath()+"  ==="+newFile.exists());
			File newFile2=new File(path+(osVersion>=23?"":File.separator+tempFile.getName())+File.separator
					+Config.PHOTO);
			LogCatUtils.showString("==newFile2=="+newFile2.getAbsolutePath()+"  ==="+newFile2.exists());
			File newFile3=new File(path+(osVersion>=23?"":File.separator+tempFile.getName())+File.separator
					+Config.EVENT);
			LogCatUtils.showString("==newFile3=="+newFile3.getAbsolutePath()+"  ==="+newFile3.exists());
			if (newFile.exists()&&newFile2.exists()&&newFile3.exists()) {
				this.path= path+(osVersion>=23?"":File.separator+tempFile.getName());
				LogCatUtils.showString("==path=="+path);
				return;
			}
		}
	}
	public boolean getExternalStorgeState() {
		if (TheApp.mIsShengMaiIC) {
			if (RainUvc.getTFCardStatus()==1) {
				return true;
			}
		}
		
		if (TextUtils.isEmpty(path)) {
			return false;
		}
		return new File(path).exists();
	}
	boolean isFilePath=false;
	public String getSdPath(String path) {
		if (getExternalStorgeState()) {
			File file=new File(path);
			if (!file.exists()) {
				return path;
			}
			File []files=file.listFiles();
			if (files.length==0) {
				
				return path;
			}
			for (File tempFile:files) {
				File newFile=new File(path+File.separator+tempFile.getName()+File.separator
						+Config.NORMAL);
				File newFile2=new File(path+File.separator+tempFile.getName()+File.separator
						+Config.PHOTO);
				File newFile3=new File(path+File.separator+tempFile.getName()+File.separator
						+Config.EVENT);
				if (newFile.exists()&&newFile2.exists()&&newFile3.exists()) {
					return path+File.separator+tempFile.getName();
				}
			}
			
		}
		return path;
	}
	public String getSDName(){
		File file=new File(getPath());
		return file.getName();
	}
	public boolean isEnablePath(String path, boolean isUsered) {
		boolean flage = false;
		File file = new File(path);
		if (file.exists() && file.isDirectory()) {
			flage = true;
		}
		return flage;
	}

	
	public void getAllVideoFile(String seleTime,AllMediaFileCallback callback){
		List<String>mTimeList=new ArrayList<String>();
		List<List<MediaInfor>>dataLists=new ArrayList<List<MediaInfor>>();
		for (int i = 23; i >=0; i--) {
			List<MediaInfor> lisMap = new ArrayList<MediaInfor>();
			lisMap=loadVideoAndPhotoFile2(seleTime, String.valueOf(i));
			if (lisMap != null && lisMap.size() > 0) {
				dataLists.add(lisMap);
				if (Integer.valueOf(i)<10) {
					mTimeList.add("0"+i);
				}else {
					mTimeList.add(String.valueOf(i));
				}
			}
		}
		callback.getFileCallback(mTimeList, dataLists);
		
	}
	
	@SuppressWarnings("unchecked")
	private List<MediaInfor>loadVideoAndPhotoFile2(String seleTime, String valueOf) {
		if (Integer.valueOf(valueOf)<10) {
			valueOf="0"+valueOf;
		}
		List<MediaInfor>list=new ArrayList<MediaInfor>();
		MediaInfor info2=new MediaInfor();
		String name;
		List<String>dataList=TheApp.mApp.getmFileNameList();
		if (dataList.isEmpty()) {
			return list;
		}
		for(int i=0;i<dataList.size();i++){
			MediaInfor infor ;
			name=dataList.get(i);
			if (TextUtils.isEmpty(name)) {
				continue;
			}
			String []times = null;
			if (name.endsWith(Config.FILE_PHOTO)) {
				
				times=mIsCameraPhoto(name);
				
			}else if (name.endsWith(Config.FILE_FORMAT)||
					name.endsWith(Config.FILE_RECORDING)) {
				
				times=mIsRecordVideo(name);
			}
			if (times==null||times.length<1) {
				continue;
			}
			
			if (!(times[0]+times[1]).contains(seleTime+valueOf)) {
				continue ;
			}
			try {
				infor=info2.clone();
				if (!TheApp.mIsShengMaiIC) {
					if (name.endsWith(Config.FILE_PHOTO)) {
						String path=getPath()+File.separator+Config.PHOTO+File.separator+name;
						File file=new File(path);
						if (file.exists()) {
							infor.setPath(path);
						}
					}else if (name.startsWith(Config.FILE_NOSAVE)) {
						String path=getPath()+File.separator+Config.NORMAL+File.separator+name;
						File file=new File(path);
						if (file.exists()) {
							infor.setPath(path);
						}
					}else if (name.startsWith(Config.FILE_FORMAT_C)) {
						String path=getPath()+File.separator+Config.EVENT+File.separator+name;
						File file=new File(path);
						if (file.exists()) {
							infor.setPath(path);
						}
					}
				}
				infor.setCreatTime(times[1]);
				infor.setName(name);
				infor.setH_m_s(times[1]);
				list.add(infor);
				
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (list!=null&&list.size()>0) {
			AllFileSort comparatorInfor=new AllFileSort();
			Collections.sort(list, comparatorInfor);
			//listmp.put(valueOf, list);
		}
		return list;
	}
	
	
	File file;
	String mFilepath;
	public List<String>getDataSeleTime(final LoadAllDataCallback callback){
		list.clear();mDataTime.clear();mLockFile.clear();
		//4G模块
		if (TheApp.mIsShengMaiIC) {
			return getDataSeleTimeIs4gMode(callback);
		}
		
		boolean mFileIsExist=getExternalStorgeState();
		if (!mFileIsExist) {
			return list;
		}
		mFilepath=getPath()+File.separator+Config.NORMAL;
		file=new File(mFilepath);
		file.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String filename) {
				
				if (filename.endsWith(".MOV")&&filename.startsWith("FILE")) {
					String[] time=mIsRecordVideo(filename);
					LogCatUtils.showString("time===="+time+"   "+filename);
					if (time!=null&&time.length>0) {
						list.add(filename);
						if (!mDataTime.contains(time[0])) {
							mDataTime.add(time[0]);
						}
					}
				}
				
				return false;
			}
			
		});
		return getPhotoDataFile(list,mDataTime,callback);
	}
	/**
     * Get total files in the specified directory
     * int getTotalFiles(int type)
     * type:
     * 0: DCIM, 正常
     * 1: EVENT, 緊急
     * 2: LOCK, 加鎖
     * 3: JPEG, 拍照
     * @return int
     * < 0 error
     * >= 0 the nums in dir
     */
	private int DCIM=0;
	private int EVENT=1;
	private int LOCK=2;
	private int JPEG=3;
	private int mFileNumber=0;
	private int[]type=new int[]{DCIM,EVENT,LOCK,JPEG};
	
	String name;
	private List<String> getDataSeleTimeIs4gMode(LoadAllDataCallback callback) {
		for (int i = 0; i < type.length; i++) {
			fileParsing(type[i]);
		}
		callback.callback(list, mDataTime, mLockFile);
		return null;
	}
	String tempTime;
	private void fileParsing(int type){
		List<String>mediaTemp=new ArrayList<String>();
		mFileNumber=RainUvc.getTotalFiles(type);
		if (mFileNumber>0) {
			for (int i = 0; i < mFileNumber; i++) {
				name=RainUvc.getFileName(type, i);
				if (TextUtils.isEmpty(name)) {
					continue;
				}
				tempTime=fileTimeParsing(type,name);
				
				if (!tempTime.isEmpty()&&
						!mDataTime.contains(tempTime)) {
					mDataTime.add(tempTime);
					
				}
				if (type==1&&!mLockFile.contains(tempTime)) {
					mLockFile.add(tempTime);
				}
				mediaTemp.add(name);
			}
		}
		if (type==DCIM) {
			MediaTypeModule.getInstance().setmVideo(mediaTemp);
		}else if (type==JPEG) {
			MediaTypeModule.getInstance().setmPhoto(mediaTemp);
		}else if (type==LOCK) {
			MediaTypeModule.getInstance().setmLock(mediaTemp);
		}else if (type==EVENT) {
			MediaTypeModule.getInstance().setmEvent(mediaTemp);
		}
		list.addAll(mediaTemp);
			
	}
	private String [] mTimes;
	private String fileTimeParsing(int type,String name) {
		if (type==JPEG&&name.endsWith(".JPG")) {
			mTimes=name.split("_");
			if (mTimes==null||mTimes.length<2) {
				return "";
			}
			return mTimes[0];
		}else if (name.endsWith(".mp4")&&name.startsWith("ch0")) {
			mTimes=name.split("_");
			if (mTimes==null||mTimes.length<3) {
				return "";
			}
			return mTimes[1].substring(0, 8);
		}
		return "";
	}
	private String[] mIsRecordVideo(String name){
		String []returnValue=new String [2];
		if ((TextUtils.isEmpty(name))||(!((name.contains("-")||name.contains("_"))))) {
			return null;
		}
		if (TheApp.mIsShengMaiIC) {
			times=name.split("_");
			if (times.length!=3) {
				return null;
			}
			data=times[1].substring(0,8);
			time=times[1].substring(8,14);
		}else {
			times=name.split("-");
			if (times.length!=2) {
				return null;
			}
			data=times[0].substring(4);
			time=times[1].substring(0,6);
		}
		for (int i = 0; i < data.length(); i++) {
			if (!Character.isDigit(data.charAt(i))) {
				return returnValue;
			}
		}
		for (int i = 0; i < time.length(); i++) {
			
			if (!Character.isDigit(time.charAt(i))) {
				return returnValue;
			}
		}
		if (TheApp.mIsShengMaiIC) {
			if (data.length()!=8||time.length()!=6) {
				return null;
			}
			returnValue[0]=data;
			returnValue[1]=time;
			return returnValue;
		}
		returnValue[0]=20+data;
		returnValue[1]=time;
		return returnValue;
	}
	
	
	private List<String> getPhotoDataFile(final List<String> list, final List<String> mDataTime, LoadAllDataCallback callback) {
		mFilepath=getPath()+File.separator+Config.PHOTO;
		file=new File(mFilepath);
		file.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String filename) {
				if (filename.endsWith(Config.FILE_PHOTO)&&filename.startsWith(Config.PHOTO_MARK)) {
					String[] time=mIsCameraPhoto(filename);
					if (time!=null&&time.length>0) {
						list.add(filename);
						if (!mDataTime.contains(time[0])) {
							mDataTime.add(time[0]);
						}
					}
				}
				return false;
			}
			
		});
		
		return loadEMERFile(list,mDataTime,callback);
	}
	
	private List<String> loadEMERFile(final List<String> list, final List<String> mDataTime, 
			LoadAllDataCallback callback) {
		String path=getPath()+File.separator+Config.EVENT;
		File file=new File(path);
		file.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String filename) {
				if (filename.endsWith(Config.FILE_FORMAT)&&filename.startsWith(Config.FILE_FORMAT_C)) {
					String[] time=mIsRecordVideo(filename);
					if (time!=null&&time.length>0) {
						list.add(filename);
						if (!mDataTime.contains(time[0])) {
							mDataTime.add(time[0]);
						}
						if (!mLockFile.contains(time[0])) {
							mLockFile.add(time[0]);
						}
					}
				}
				return false;
			}
			
		});
		
		callback.callback(list, mDataTime,mLockFile);
		
		return null;
	}
	String data;String time;String [] times;
	protected String[] mIsCameraPhoto(String filename) {
		String []returnValue=new String [2];
		
		if ((filename==null)||((!filename.contains("-"))&&(!filename.contains("_")))) {
			return null;
		}
		if (TheApp.mIsShengMaiIC) {
			times=filename.split("_");
		}else {
			times=filename.split("-");
		}
		if (times.length!=2) {
			return null;
		}
		if (TheApp.mIsShengMaiIC) {
			data=times[0];
		}else {
			data=times[0].substring(3);
		}
		time=times[1].substring(0,6);
		
		for (int i = 0; i < data.length(); i++) {
			if (!Character.isDigit(data.charAt(i))) {
				return null;
			}
		}
		for (int i = 0; i < time.length(); i++) {
			if (!Character.isDigit(time.charAt(i))) {
				return null;
			}
		}
		if (TheApp.mIsShengMaiIC) {
			if (data.length()!=8||time.length()!=6) {
				return null;
			}
			returnValue[0]=data;
			returnValue[1]=time;
			return returnValue;
		}
		if (data.length()!=6||time.length()!=6) {
			return null;
		}
		
		returnValue[0]=20+data;
		returnValue[1]=time;
		return returnValue;
		
	}
	public int getVideoTime(String path) {
		MediaMetadataRetriever mRetriever = new MediaMetadataRetriever();
		mRetriever.setDataSource(path);
		String time = mRetriever
				.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
		int seconds = Integer.valueOf(time) / 1000;
		return seconds;
	}

	public boolean isFileDataExist(String seleTime){
		List<String>mDataTime=TheApp.mApp.getmDataTime();
		if (list.isEmpty()) {
			return false;
		}
		return mDataTime.contains(seleTime);
	}

	
}