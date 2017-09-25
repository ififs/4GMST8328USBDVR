package com.syu.dvr.imageloader;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.syu.dvr.TheApp;
import com.syu.dvr.utils.LogCatUtils;
import com.syu.dvr.widget.ComInterface.ImageCache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DiskCache implements ImageCache{
	
	static String cacheDir="";
	
	public  DiskCache() {
		if (TheApp.mSysmanager!=null) {
			
			cacheDir=TheApp.mSysmanager.getPath()+File.separator+"Cache";
		}
	}

	Bitmap bitmap=null;
    InputStream inputStream=null;
	@Override
	public Bitmap get(String name) {
		if (!TheApp.mSysmanager.getExternalStorgeState()) {
			return null;
		}
		if (name.isEmpty()||cacheDir.isEmpty()) {
			return null;
		}
		
		File file=new File(cacheDir);
		if (!file.exists()) {
			return null;
		}
		String cachename ="";
		if ((cachename=getFileName(name)).length()<=10) {
			return null;
		}
        try {
            inputStream=new FileInputStream(cacheDir+File.separator+cachename);
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inTempStorage=new byte[100*1024];
            options.inPreferredConfig=Bitmap.Config.RGB_565;
            bitmap=BitmapFactory.decodeStream(inputStream,null,options);
 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }      
		return bitmap;
	}

	@Override
	public void put(String name, Bitmap bitmap) {
		
		if (bitmap==null) {
			return;
		}
		if (!TheApp.mSysmanager.getExternalStorgeState()) {
			return;
		}
		if (name.isEmpty()||cacheDir.isEmpty()) {
			return ;
		}
		File file=new File(cacheDir);
		if (!file.exists()){
			return;
		}
		String cachename ="";
		if ((cachename=getFileName(name)).length()<=10) {
			return ;
		}
		BufferedOutputStream outputStream=null;
		try {
			String newFile=cacheDir+File.separator+cachename;
			outputStream=new BufferedOutputStream(new FileOutputStream(newFile));
			if (bitmap!=null) {
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if (outputStream!=null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	private String getFileName(String path){
		return path.replace("-", "").substring(0,15);
	}
	
	public void deleData(String pString) {
		
		if (!TheApp.mSysmanager.getExternalStorgeState()) {
			return;
		}
		String path=TheApp.mCachePath+File.separator+getFileName(pString);
		File file=new File(path);
		if (file.exists()) {
			file.delete();
		}
	
	}

}
