package com.syu.dvr.control;

import java.io.File;
import java.util.List;

import com.syu.dvr.R;
import com.syu.dvr.TheApp;
import com.syu.dvr.utils.LogCatUtils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.Log;

public class StartActivity {
	
	public static Context context;
	private static PackageManager manager;
	
	static{
		context=TheApp.mApp;
		manager=context.getPackageManager();
	}
	
	public static void startAcitivity(Class<?extends Activity>target){
		Intent intent =new Intent(context, target);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	public static void startAcitivity(Class<?extends Activity>target,Bundle data){
		Intent intent =new Intent(context, target);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("TimeData", data);
		context.startActivity(intent);
	}
	
	public static void sendBroadCast(String action){
		Intent intent=new Intent();
		intent.setAction(action);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.sendBroadcast(intent);
	}
	public static void startAcitivity(String apkpackage){
		PackageInfo info=null;
		try {
			info=TheApp.mApp.getPackageManager().getPackageInfo(apkpackage, 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			info=null;
			return;
		}
		if (info==null) {
			return;
		}
		Intent intent=manager.getLaunchIntentForPackage(apkpackage);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	public static boolean startAcitivity(String apkpackage,String action){
		
		
		PackageInfo info=null;
		try {
			info=TheApp.mApp.getPackageManager().getPackageInfo(apkpackage, 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			info=null;
			return startAcitivity("com.syu.powerful", "com.syu.filemanager.fromDvr");
		}
		if (info==null) {
			if (action.equals("com.syu.fromDvr")) {
				return startAcitivity("com.syu.powerful", "com.syu.filemanager.fromDvr");
			}
			return false;
		}
//		Intent intent=manager.getLaunchIntentForPackage(apkpackage);//activity主入口
		Intent intent=new Intent();
		intent.setPackage(apkpackage);
		intent.setAction(action);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			context.startActivity(intent);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return true;
	}
	public  static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }
        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);

        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);

        // Set the component to be explicit
        explicitIntent.setComponent(component);

        return explicitIntent;
    }
	public void playMeideo(String path){
		PackageManager packageManager=TheApp.mApp.getPackageManager();
		
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK 
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File(path));
		intent.setDataAndType(uri, "video/*");
		List<ResolveInfo>apps=packageManager.queryIntentActivities(intent, 0);
		if (apps==null||apps.isEmpty()) {
			
			return;
		}
		context.startActivity(intent);
	}
}
