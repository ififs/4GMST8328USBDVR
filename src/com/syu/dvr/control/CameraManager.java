package com.syu.dvr.control;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.hardware.Camera;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.view.SurfaceHolder;

import com.syu.codec.Codec.TakePictureCallback;
import com.syu.codec.PreviewView;
import com.syu.codec.Utils;
import com.syu.dvr.TheApp;
import com.syu.dvr.factory.App4GGetRecordStatus;
import com.syu.dvr.factory.AppGetCameraSdCard;
import com.syu.dvr.factory.UpSystemTimeForCamera;
import com.syu.dvr.utils.Config;
import com.syu.dvr.utils.LogCatUtils;
import com.syu.dvr.utils.PublicClass;
import com.syu.dvr.utils.RecordingStatus;
import com.syu.dvr.widget.ComInterface.CapturePhotos;
import com.syu.jni.CameraNative;
import com.uvc.jni.RainUvc;

@SuppressWarnings("deprecation")
public class CameraManager{
	public boolean[] isOpenCamare;
	public boolean []isRecord;
	public boolean []isPreview;
	public File[] mRecordFile;	
    private PreviewView mPreviewView;
	public void removePreviewView(int mCameraID) {
		mPreviewView=null;
		isPreview[mCameraID]=false;
	}
	public void setmPreviewView(PreviewView mPreviewView,int mCameraID) {
		LogCatUtils.showString("  setmpreviewView  ");
		isPreview[mCameraID]=true;
		this.mPreviewView = mPreviewView;
	}
	public CameraManager(){
	        mRecordFile=new File[Config.MAX_SUPPORT_CAMERAS];
	        isOpenCamare=new boolean[Config.MAX_SUPPORT_CAMERAS];
	        isRecord=new boolean[Config.MAX_SUPPORT_CAMERAS];
	        isPreview=new boolean[Config.MAX_SUPPORT_CAMERAS];
	        mTo8328Byte=new byte[16];
	        mGet8328Byte=new byte[16];
	 }
	public synchronized boolean doOpenCamera(final int mCameraId){
		try {
    		LogCatUtils.showString("===isOpenCamare["+mCameraId+"]=="+String.valueOf(isOpenCamare[mCameraId]));
			if (isOpenCamare[mCameraId]) {
				return true;
			}
			if (!(isOpenCamare[mCameraId]=open(mCameraId))) {
				return false;
			}
			
		} catch (Exception e) {
			doCloseCamera();
			return false;
		}
		if (!checkCamera()) {
			doCloseCamera(mCameraId);
			return false;
		}
		if (TheApp.mIsShengMaiIC) {
			int mTime=RainUvc.getRecLength();
			if (mTime>0) {
				TheApp.mApp.saveInt("RECORDING_TIME",mTime);
			}
		}else {
			setDefuleValue=setUvcExtenrnCall(TheApp.getDeviceId(),
	    			String.valueOf(1), String.valueOf(81),"");
			int [] version=setUvcExtenrnCall(TheApp.getDeviceId(),
	    			String.valueOf(9), String.valueOf(81),"");
			if (version!=null&&version.length==16) {
				if (version[10]==4||version[10]==1) {
					RecordingStatus.getInstance().setHasMic(true);
				}else {
					RecordingStatus.getInstance().setHasMic(false);
				}
			}
			if (setDefuleValue!=null&&(setDefuleValue.length==16)) {
				
				TheApp.mApp.saveInt("RECORDING_TIME", setDefuleValue[2]);
				if (setDefuleValue[5]==0&&RecordingStatus.getInstance().isHasMic()) {
					RecordingStatus.getInstance().setMisRecordAudio(true);
				}else {
					RecordingStatus.getInstance().setMisRecordAudio(false);
				}
				int mResolution;
				for (int i = 0; i < setDefuleValue.length; i++) {
//					LogCatUtils.logsendcast("===i=="+i+"   ==value=="+setDefuleValue[i]);
				}
				if (setDefuleValue[7]==3&&setDefuleValue[8]==2&&setDefuleValue[9]==3) {
					mResolution=0;
				}else {
					mResolution=1;
				}
//				LogCatUtils.logsendcast("==mResolution==="+mResolution);
				RecordingStatus.getInstance().setmResolution(mResolution);
			}
		}
		SystemProperties.set("sys.fyt.dvr","true");
		return true;
	} 
	private boolean checkCamera(){
		if (TheApp.mIsShengMaiIC) {
			String versionV=RainUvc.getPlatformVersion();
			if (!TextUtils.isEmpty(versionV)&&!versionV.contains("FYT")) {
				callback(5);
				return false;
			}
			if (getSystemData()<201612) {
				callback(4);
				return false;
			}
		}else {
			int temp=0;
			int [] version=TheApp.mCManager.setUvcExtenrnCall(TheApp.getDeviceId(),
	    			String.valueOf(9), String.valueOf(81),"");
			if (version!=null) {
				for (int i = 0; i < version.length; i++) {
					temp+=version[i];
				}
				if (temp==0) {
					LogCatUtils.showString("不支持");
					callback(5);
					return false;
				}
			}
			int id=SystemProperties.getInt("ro.build.fytmanufacturer", 0);
			if (version!=null&&version.length==16&&version[version.length-2]>2) {
				if (id!=version[version.length-2]) {
					LogCatUtils.showString("不支持");
					callback(3);
					return false;
				}
			}
			if (getSystemData()<201612) {
				callback(4);
				return false;
			}
		}
		
		return true;
	}
	private void callback(int value){
		for (int i = 0; i < callbacks.size(); i++) {
			callbacks.get(i).cameraCallback(value);
		}
	}
	private long getSystemData(){
		String string=SystemProperties.get("ro.build.date","2016-10");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		LogCatUtils.showString("==time===="+string);
		Date currentTime = new Date();
		String dateString = formatter.format(currentTime);
		LogCatUtils.showString("==time===="+dateString);
		return Long.parseLong(dateString);
	}
	int flag=0;
	
	private boolean open(int cameraId){
		int flag=TheApp.getDeviceId();
		boolean init = false;
		if (flag==0) {
			init=Utils.initialize(0, 1280, 720, 30, 0);
		}else if (flag==1) {
			init=Utils.initialize(0, PublicClass.getInstance().getWindowManeger()[0], 
					PublicClass.getInstance().getWindowManeger()[0], 20, 1);
		}
		LogCatUtils.showString("====init=="+String.valueOf(init));
		if (init) {
			if (!TheApp.mIsShengMaiIC) {
				AppGetCameraSdCard.getInstance().RunTask();
			}else if (TheApp.mIsShengMaiIC&&!TheApp.mApp.isPlayBack) {
				App4GGetRecordStatus.getInstance().RunTask();
			} 
		}else {
			return false;
		}
		if (!TheApp.mApp.isPlayBack) {
	    	UpSystemTimeForCamera.getInstance().RunTask();
	    }
		return true;
	}
	public void startRecord(int mCameraId){
		LogCatUtils.showString("=====startRecord==");
		if (TheApp.mIsShengMaiIC&&RainUvc.getMode()==1) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					RainUvc.startRecord();
				}
			}).start();

		}else {
			setUvcExtenrnCall(mCameraId, String.valueOf(7), String.valueOf(1), String.valueOf(1));
		}
	}
	public void stopRecord(int mCameraId){
		LogCatUtils.showString("=====stopRecord==");
		if (TheApp.mIsShengMaiIC) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					RainUvc.stopRecord();
				}
			}).start();
		}else {
			TheApp.mApp.sendBroadCast(0);
			setUvcExtenrnCall(mCameraId, String.valueOf(7), String.valueOf(1), String.valueOf(0));
		}
		
	}
	public void setCameraSetting(int mCameraID,int[]value){
		if (value==null||value.length<16) {
			return;
		}
		if (!isOpenCamare[mCameraID]) {
			return;
		}
		byte []tem=new byte[16];
		for (int i = 0; i < value.length; i++) {
			mTo8328Byte[i]=(byte) value[i];
		}
		CameraNative.native_send_cmdto8328(mTo8328Byte, mGet8328Byte);
		int []temp=getAscii(mGet8328Byte);
		for (int i = 0; i < temp.length; i++) {
			if (temp[i]==-1) {
				if (i==15) {
//					CameraErrorToValue.getInstance().cameraIsError();
				}
			}else {
				break;
			}
		}
	}
	private byte[]mTo8328Byte;
	private byte[]mGet8328Byte;
	private int []temp;
	public synchronized void recordLock(){
		if (TheApp.mIsShengMaiIC) {
			RainUvc.setGSensorStatus(1);
			return;
		}
		setUvcExtenrnCall(TheApp.getDeviceId(),
				String.valueOf(3), String.valueOf(1), String.valueOf(6));
	}
	public synchronized int [] setUvcExtenrnCall(int mCameraId,String parameter,String parameter2,String parameter3){
		if (!isOpenCamare[mCameraId]) {
			return null;
		}
		mTo8328Byte[0]=(byte) getValueOf(String.valueOf(parameter));
		mTo8328Byte[1]=(byte) getValueOf(String.valueOf(parameter2));
		if (!parameter3.isEmpty()) {
			mTo8328Byte[2]=(byte) getValueOf(String.valueOf(parameter3));
		}
		CameraNative.native_send_cmdto8328(mTo8328Byte, mGet8328Byte);
		
		return getAscii(mGet8328Byte);
	}
	
	public int getValueOf(String value){
		return Integer.valueOf(value, 16);
	}
	int []value=null;
	private int[] getAscii(byte [] bytes){
		value=null;
		if (bytes==null) {
			return value;
		}
		if (bytes.length==0) {
			return value;
		}
		value=new int[bytes.length];
		for(int i=0;i<bytes.length;i++){
			int temp=bytes[i];
			if (temp==127) {
				temp=0;
			}else if (temp==126) {
				temp=59;
			}else if (temp==125) {
				temp=61;
			}
			value[i]=temp;
		}
		return value;
	  }
   
	public void MyCarCollitionPhoto(int cameraId,final CapturePhotos callball){
		if (mPreviewView!=null&&isPreview[cameraId]) {
			String mFilepath="/sdcard"+File.separator+
					"IMG"+FileManager.getFileNameForTime()+Config.REMOTE_PHOTO;
			mPreviewView.takePicture(mFilepath, 50,new TakePictureCallback() {
				@Override
				public void onResult(boolean success, String path) {
					if (success) {
						callball.capturePhoto(1, path);
					}else {
						LogCatUtils.showString("===拍照失败====");
						callball.capturePhoto(-1, null);
					}
				}
			});
		}else {
			callball.capturePhoto(-1,null);
		}
		return;
	}
    
	int []setDefuleValue;
	public void startPreview(final int cameraID){
		if (mPreviewView!=null&&isOpenCamare[cameraID]) {
			mPreviewView.start();	
			isPreview[cameraID]=true;
		}
	}
	public boolean startPreview(int cameraId,SurfaceHolder holder){
		if (isOpenCamare[cameraId]) {
			return true;
		}else if (!doOpenCamera(cameraId)) {
			return false;
		}
		return true;
	}
	public void doStopPreview(int cameraId){
		if (mPreviewView!=null&&isOpenCamare[cameraId]) {
			mPreviewView.pause();
			isPreview[cameraId]=false;
		}
	}
	public void doCloseCamera(int cameraId){
		TheApp.mApp.sendBroadCast(0);
		Utils.close();
		UpSystemTimeForCamera.getInstance().stopTimer();
		App4GGetRecordStatus.getInstance().stopTimer();
		AppGetCameraSdCard.getInstance().stopTimer();
		isPreview[cameraId]=false;
		isOpenCamare[cameraId]=false;
	}
	public void doCloseCamera(){
		for (int cameraId = 0; cameraId < Camera.getNumberOfCameras(); cameraId++) {
			doCloseCamera(cameraId);
		}
	}
	public void upDataTime(int cameraID,int i,int[] mTimes) {
		if (!isOpenCamare[cameraID]) {
			return;
		}
		if (mTimes!=null&&mTimes.length==6) {
		mTo8328Byte[0]=(byte) (getValueOf("60"));
		mTo8328Byte[1]=(byte) (getValueOf("1"));
		for (int j = 0; j < mTimes.length; j++) {
			int tem=Integer.valueOf(mTimes[j]);
			mTo8328Byte[j+2]=(byte) tem;
		}
		CameraNative.native_send_cmdto8328(mTo8328Byte, mGet8328Byte);
		}
	}
	public boolean mCameraIsUse(int mCameraID){
		if (!isOpenCamare[mCameraID]) {
			return false;
		}
		return true;
		
	}
	private List<CameraManagerCallback> callbacks=new ArrayList<CameraManager.CameraManagerCallback>();
	public void registerCallback(CameraManagerCallback callback){
		if (!callbacks.equals(callback)) {
			callbacks.add(callback);
		}
	}
	public void unregisterCallback(CameraManagerCallback callback){
		if (callbacks.equals(callback)) {
			callbacks.remove(callback);
		}
	}
	public interface CameraManagerCallback{
		public void cameraCallback(int value);
	}
}