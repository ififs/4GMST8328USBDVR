package com.syu.dvr.utils;

import com.syu.dvr.TheApp;

import android.annotation.SuppressLint;

/**
 * @author Administrator
 *
 */
@SuppressLint("NewApi")
public class Config {

	public static final String  CAMERE_PROFIEL				="com.syu.dvr.camera.profile.size";	
	/***摄像头类型和设置的存�??***/
	public static final int MAX_SUPPORT_CAMERAS        = 2;
	public static final String FILE_RECORDING			= ".mp4";
	public static final String 	FILE_FORMAT				= ".MOV";
	public static final String 	FILE_FORMAT_C			= "EMER";
	public static final String  PHOTO_MARK				="IMG";	
	public static final	String  FILE_NOSAVE				="FILE";
	public static final String 	FILE_PHOTO				= ".JPG";
	public static final String 	REMOTE_PHOTO			= ".png";
	public static final String  PHOTO					="Photo";
	public static final String  NORMAL					="Normal";
	public static final String  EVENT					="Event";
	
	
	
	public static final String KEY_COLOR_EFFECT = "pref_camera_color_effect";
	public static final String KEY_COLOR_BG_ID = "pref_camera_color_bg_id";
	public static final String KEY_WHITE_BALANCE = "pref_camera_whitebalance_key";
	public static final String KEY_WHITE_BG_ID = "pref_camera_whitebalance_key_bg_id";
	public static final String KEY_EXPOSURE = "pref_camera_exposure_key";
	public static final String KEY_EXPOSURE_BG_ID = "pref_camera_exposure_key_bg_id";
	public static final String EXPOSURE_DEFAULT_VALUE = "0";
	public static final String TIME_FORMAT="com.syu.dvr.setting.time.format";
	 
	public static boolean		isRecorderAudio			= false;
	public static final int		SOURCE_CAMCORDER_CAMERA	= 1;
	public static int     mCollisionRecord        =-1;
	/****************************服务Action****************************/
	private static String mServiceAction;
	private static String msgId;
	private static int     mFlag=-1;
	private static int     mCollRecoFlag=-1;//碰撞录制标志位,防止同时有多个录像请求
	private static String  type="";

	
	public static String getType() {
		return type;
	}

	public static void setType(String type) {
		Config.type = type;
	}

	public static int getmCollRecoFlag() {
		return mCollRecoFlag;
	}

	public static void setmCollRecoFlag(int mCollRecoFlag) {
		Config.mCollRecoFlag = mCollRecoFlag;
	}

	public static String getMsgId() {
		return msgId;
	}

	public static void setMsgId(String msgId) {
		Config.msgId = msgId;
	}

	
	public static String getmServiceAction() {
		return mServiceAction;
	}
	public static void  setmServiceAction(String action) {
		mServiceAction = action;
	}

	public static int getmFlag() {
		return mFlag;
	}

	public static void setmFlag(int mFlag) {
		Config.mFlag = mFlag;
	}
	
	
	/***********************开机限制操作***********************************/
	private static boolean isShutUp =false;//第一次启动十秒内不让操作
	private static boolean isSdExitence=false;
	private static boolean isFirstPlayVideo=false;//开始视频播放，视频播放器标志位，防止播放器下一曲时产生状态误差
	private static boolean isAccOff=false;
	private static boolean isAccUp=false;
	public static boolean isCollisionRecord=false;//碰撞录制
	private static boolean isCanOpenCamera=false;
	private static boolean isSwith=false;
	private static boolean isBackCarReturn=false;
	private static boolean isVideoPlay=false;
	
	private static String mSavaPath="null";
	
	

	public static boolean isVideoPlay() {
		return isVideoPlay;
	}

	public static void setVideoPlay(boolean isVideoPlay) {
		Config.isVideoPlay = isVideoPlay;
	}

	public static boolean isBackCarReturn() {
		return isBackCarReturn;
	}

	public static void setBackCarReturn(boolean isBackCarReturn) {
		Config.isBackCarReturn = isBackCarReturn;
	}

	public static boolean isSwith() {
		return isSwith;
	}

	public static void setSwith(boolean isSwith) {
		Config.isSwith = isSwith;
	}

	public static boolean isCollisionRecord() {
		return isCollisionRecord;
	}

	public static void setCollisionRecord(boolean isCollisionRecord) {
		Config.isCollisionRecord = isCollisionRecord;
	}

	public static boolean isAccUp() {
		return isAccUp;
	}

	public static void setAccUp(boolean isAccUp) {
		Config.isAccUp = isAccUp;
	}

	public static boolean isAccOff() {
		return isAccOff;
	}

	public static void setAccOff(boolean isAccOff) {
		Config.isAccOff = isAccOff;
	}

	public static boolean isCanOpenCamera() {
		return isCanOpenCamera;
	}

	public static void setCanOpenCamera(boolean isCanOpenCamera) {
		Config.isCanOpenCamera = isCanOpenCamera;
	}

	public static boolean isFirstPlayVideo() {
		return isFirstPlayVideo;
	}

	public static void setFirstPlayVideo(boolean isFirstPlayVideo) {
		Config.isFirstPlayVideo = isFirstPlayVideo;
	}

	public static boolean isSdExitence() {
		return isSdExitence;
	}

	public static void setSdExitence(boolean isSdExitence) {
		Config.isSdExitence = isSdExitence;
	}

	public static boolean isShutUp() {
		return isShutUp;
	}

	public static void setShutUp(boolean isShutUp) {
		Config.isShutUp = isShutUp;
	}
	
	
/**************加锁设置****************/
private static   boolean       isCanLock	=false;

public static boolean isLock() {
	
	return isCanLock;
}

public static void setLock(boolean isLock) {
	Config.isCanLock=isLock;
	
	
	}
/***********状态存储常量名***************/

public final static String PREVIEW_RESOLUTION="com.syu.dvr.Preview_resolution"; 
public final static String RECORD_LENGTH="com.syu.dvr.RECORD_LENGTH"; 
public final static String STROBE="com.syu.dvr.STROBE";
}
