package com.syu.dvr.control;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.syu.dvr.TheApp;
import com.syu.dvr.module.DeleFileInfor;
import com.syu.dvr.utils.LogCatUtils;

public class DeletePhotoManager {
	private static DeletePhotoManager manager;
	private List<DeleFileInfor> mInitListData;
	
	public DeletePhotoManager() {
		mInitListData=new ArrayList<DeleFileInfor>();
		mInitListData=getInitData();
		sortFIle(mInitListData);
	}
	public static DeletePhotoManager getInstance(){
		if (manager==null) {
			synchronized (DeletePhotoManager.class) {
				if (manager==null) {
					manager=new DeletePhotoManager();
				}
			}
		}
		return manager;
	}
	public void upDataFile(){
		mInitListData.clear();
		mInitListData=getInitData();
		sortFIle(mInitListData);
	}
	private void sortFIle(List<DeleFileInfor> mInitListData2){
		
		ComparatorInfor comparatorInfor=new ComparatorInfor();
		Collections.sort(mInitListData, comparatorInfor);
		
	}
	String path=null;String y_m_d=null;String h_m_s=null;String name;
	private List<DeleFileInfor>getInitData(){
		mInitListData.clear();
		mCountFlleSize=0;
		final List<DeleFileInfor>list=new ArrayList<DeleFileInfor>();
		
		if (TheApp.mPhotoPath==null||!TheApp.mSysmanager.getExternalStorgeState()) {
			return list;
		}
		File file=new File(TheApp.mPhotoPath);
		if (file!=null&&(file.exists())&&((mCountFlleSize=file.listFiles().length)>0)) {
			
			file.list(new FilenameFilter() {
				
				@Override
				public boolean accept(File mFile, String mFileName) {
					if (mFileName.endsWith(".jpg")) {
						y_m_d=mFileName.substring(5, 17);
						h_m_s=(String.valueOf(100));//如果时间一样就手机设置数字前录在前
						DeleFileInfor infor=new DeleFileInfor(y_m_d, h_m_s, mFile.getAbsolutePath()+File.separator+mFileName);
						list.add(infor);
					}
					return false;
				}
			});
		}
		
		
		return list;
	}
	long mCountFlleSize=0;
	public void removeData(){
		if (mInitListData.size()<=0) {
			return;
		}
	
		DeleFileInfor infor=mInitListData.get(0);
	
		if (infor!=null) {
		File  file=new File(infor.getPath());
		if (file.exists()) {
			
			file.delete();
		}
	}
	mInitListData.remove(0);
	
		
	}
	public void removeData(List<String>list){
		
		for (int i = 0; i < list.size(); i++) {
			
		}
	}
	public void addData(final File file){
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				name=file.getName();
				y_m_d=name.substring(5, 17);
				if (file.getAbsolutePath().contains(TheApp.mPhotoPath)) {
					h_m_s=(String.valueOf(100));
				}else {
					h_m_s=(String.valueOf(120));
				}
				
				DeleFileInfor infor=new DeleFileInfor(y_m_d, h_m_s, file.getAbsolutePath());
				mInitListData.add(infor);
				if (mInitListData.size()>=51) {
					
					removeData();
				}
				
			}
		}).start();
	}
}
