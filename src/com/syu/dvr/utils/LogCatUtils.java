package com.syu.dvr.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.syu.dvr.TheApp;

import android.util.Log;

public class LogCatUtils {
	private static boolean IS_DEBUG=false;
	
	private static boolean TAG_DUBUG=false;
	
	private static boolean IS_WRITE=false;
	
	private static boolean IS_SCREEN=false;
	
	private static boolean IS_Recoddebug=false;
	
	static{
		File file=new File("/sdcard", "dvrdebug");
		if (file.exists()) {
			IS_DEBUG=true;
			TAG_DUBUG=true;
			IS_WRITE=true;
		}
	}
	public static void showString(String msg){
		if (IS_DEBUG) {
				StackTraceElement mSte = new Exception().getStackTrace()[1];
				Log.i("log",mSte.getFileName()+"|line: " + mSte.getLineNumber() + " ---> " + msg);
				writeSdFile(mSte.getFileName()+"|line: " + mSte.getLineNumber() + " ---> " + msg);
				if (TAG_DUBUG) {
					Log.i("uvc_fyt",mSte.getFileName()+"|line: " + mSte.getLineNumber() + " ---> " + msg);
					LogPreview.show(mSte.getFileName()+"|line: " + mSte.getLineNumber() + " ---> " + msg);
				}
		}
	}
	public static void showTestLog(String mString){
		StackTraceElement mSte = new Exception().getStackTrace()[1];
		Log.i("TEST",mSte.getFileName()+"|line: " + mSte.getLineNumber() + " ---> " + mString);
	}
	public static void showInt(int value){
		if (IS_DEBUG) {
				StackTraceElement mSte = new Exception().getStackTrace()[1];
				Log.i(mSte.getFileName(),"|line:" + mSte.getLineNumber() + " ---> " + value);
				/*if (TAG_DUBUG) {
					
					Log.i("log",mSte.getFileName()+"|line:" + mSte.getLineNumber() + " ---> " + value);
					LogPreview.show(mSte.getFileName()+"|line:" + mSte.getLineNumber() + " ---> " + value);
				}*/
		}
		
	}
	
	public static void showBoolean(boolean value){
		if (IS_DEBUG) {
				StackTraceElement mSte = new Exception().getStackTrace()[1];
				Log.i(mSte.getFileName(), "|line: " + mSte.getLineNumber() + " ---> " + value);
				
				if (TAG_DUBUG) {
					
					Log.i("TAG",mSte.getFileName()+"|line: " + mSte.getLineNumber() + " ---> " + value);
					LogPreview.show(mSte.getFileName()+"|line: " + mSte.getLineNumber() + " ---> " + value);
				}
		}
		
	}
	public static void showException(Throwable ex){
		if (IS_DEBUG) {
				StackTraceElement[] mSte = ex.getStackTrace();
				StringBuffer sp=new StringBuffer();
				
				for (int i = 0; i < mSte.length; i++) {
					StackTraceElement element=mSte[i];
					sp.append(element.toString()+"\n");
				}
				if (TAG_DUBUG) {
					Log.e(mSte[0].getFileName(), "|line: " + mSte[0].getLineNumber() + " --->异常捕捉 =" +ex.getMessage());
					Log.e(mSte[0].getFileName(), "|line: " + mSte[0].getLineNumber() + " --->异常 详细信息=" +sp.toString());
					
					Log.i("TAG",mSte[0].getFileName()+"|line: " + mSte[0].getLineNumber() + " ---> " + sp.toString());
					LogPreview.show(mSte[0].getFileName()+"|line: " + mSte[0].getLineNumber() + " --->异常捕捉 =" +ex.getMessage());
					LogPreview.show(mSte[0].getFileName()+"|line: " + mSte[0].getLineNumber() + " --->异常 详细信息= " + sp.toString());
				}
			}
	}
	private static void writeSdFile(String write_str) {
    	FileWriter writer = null;
		
		try {
			StackTraceElement mSte = new Exception().getStackTrace()[1];
			
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
			
			//打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			writer = new FileWriter("/sdcard"+ File.separator + "DVRLOG记录.txt", true);
			writer.write(sdf.format(new Date())+": "+mSte.getFileName()+"|line: " + mSte.getLineNumber() +write_str);
			writer.write("\r\n");
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (writer!=null) {
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
}
