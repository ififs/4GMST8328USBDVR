package com.syu.dvr.control;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.syu.dvr.TheApp;
import com.syu.dvr.module.DeleFileInfor;
import com.syu.dvr.utils.LogCatUtils;

public class DeleteFileManager {
	private static DeleteFileManager manager;
	private List<DeleFileInfor> mInitListData;
	
	public DeleteFileManager() {
		mInitListData=new ArrayList<DeleFileInfor>();
		mInitListData=getInitData();
		sortFIle(mInitListData);
	}
	private void sortFIle(List<DeleFileInfor> mInitListData2){
		
		ComparatorInfor comparatorInfor=new ComparatorInfor();
		Collections.sort(mInitListData, comparatorInfor);
		
	}
	public static DeleteFileManager getInstance(){
		if (manager==null) {
			synchronized (DeleteFileManager.class) {
				if (manager==null) {
					manager=new DeleteFileManager();
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
	String path=null;String y_m_d=null;String h_m_s=null;String name;
	private List<DeleFileInfor>getInitData(){
		mInitListData.clear();
		final List<DeleFileInfor>list=new ArrayList<DeleFileInfor>();
		
		if (TheApp.mVideoPath==null||!TheApp.mSysmanager.getExternalStorgeState()) {
			try {
				
				TheApp.semaphore.acquire();
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//return list;
		}
		File file=new File(TheApp.mVideoPath);
		if (file!=null&&(file.exists())&&(file.listFiles().length>0)) {
			
			file.list(new FilenameFilter() {
				
				@Override
				public boolean accept(File mFile, String mFileName) {
					if ((mFileName.endsWith(".mp4"))&&(!mFileName.contains("C"))) {
						y_m_d=mFileName.substring(5, 17);
						h_m_s=(String.valueOf(100));
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
		mCountFlleSize=0;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				for (int i = 0; i < mInitListData.size(); i++) {
					DeleFileInfor infor=mInitListData.get(0);
					if (infor!=null) {
						File  file=new File(infor.getPath());
						if (file.exists()) {
							mCountFlleSize=mCountFlleSize+file.length()/1024/1024;
							file.delete();
						}
					}
					mInitListData.remove(0);
					if (mCountFlleSize>=408) {
						break;
					}
				}
				
			}
		}).start();
		
	}
	public void removeData(List<String>list){
		
		for (int i = 0; i < list.size(); i++) {
			
		}
	}
	public void addData(File file){
		
		name=file.getName();
		
		if (name.contains("C")) {
			return;
		}
		y_m_d=name.substring(5, 17);
		if (file.getAbsolutePath().contains(TheApp.mVideoPath)) {
			h_m_s=(String.valueOf(100));
		}else {
			h_m_s=(String.valueOf(120));
		}
		
		DeleFileInfor infor=new DeleFileInfor(y_m_d, h_m_s, file.getAbsolutePath());
		mInitListData.add(infor);
		
	}
}
