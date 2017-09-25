package com.syu.dvr.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.syu.dvr.TheApp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class CrashHandler implements UncaughtExceptionHandler{
	
	//final String CARSH_DIR_PATH = TheApp.mApp.mSysmanager.getSdPath();
	@SuppressLint({ "SdCardPath", "SimpleDateFormat" }) final String CARSH_DIR_PATH = "/sdcard/crash";
	@SuppressLint("SimpleDateFormat") final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
	HashMap<String, String> infos;
	String pkgName;
	Context mContext;
	
	UncaughtExceptionHandler mDefaultHandler;
	static CrashHandler mInstance;
	
	/**
	 * @return the mInstance
	 */
	public static CrashHandler getInstance(Context context) {
		if(mInstance == null)
			mInstance = new CrashHandler(context);
		return mInstance;
	}
	
	/**
	 * 
	 */
	public CrashHandler(Context context) {
		mContext = context.getApplicationContext();
		pkgName = mContext.getPackageName().replace(".", "_");
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if(!handleException(ex)) {
			if(mDefaultHandler == null) {
				mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
			}
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
            android.os.Process.killProcess(android.os.Process.myPid());  
            System.exit(1); 
          
		}
	}

	private boolean handleException(Throwable ex) {
		if(ex == null) return false;
		
		collectInfo();
		saveCarshException(ex);
		return true;
	}
	
	void collectInfo() {
		if(infos == null) {
			infos = new HashMap<String, String>();
		}
		try {  
            PackageManager pm = mContext.getPackageManager();  
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);  
            if (pi != null) {  
                String versionName = pi.versionName == null ? "null" : pi.versionName;  
                String versionCode = pi.versionCode + "";  
                infos.put("versionName", versionName);  
                infos.put("versionCode", versionCode);  
            }  
        } catch (NameNotFoundException e) {  
        }  
	}
	
	void saveCarshException (Throwable ex) {
		TheApp.mApp.sendBroadCast(0);
		StringBuffer sb = new StringBuffer();  
        for (Map.Entry<String, String> entry : infos.entrySet()) {  
            String key = entry.getKey();  
            String value = entry.getValue();  
            sb.append(key + "=" + value + "\n");  
        }  
        Writer writer = new StringWriter();  
        PrintWriter printWriter = new PrintWriter(writer);  
        ex.printStackTrace(printWriter);  
        Throwable cause = ex.getCause();  
        while (cause != null) {  
            cause.printStackTrace(printWriter);  
            cause = cause.getCause();  
        }  
        printWriter.close();  
        String result = writer.toString();  
        sb.append(result);  
        try {  
            String time = dateFormat.format(new Date());  
            String fileName = "crash-" + time + "-" + pkgName + ".txt";  
            File dir = new File(CARSH_DIR_PATH);  
            if (!dir.exists()) {  
                dir.mkdirs();  
            }  
            FileOutputStream fos = new FileOutputStream(CARSH_DIR_PATH + "/" + fileName);  
            fos.write(sb.toString().getBytes());  
            fos.close();  
        } catch (Exception e) {  
        }  
	}
}
