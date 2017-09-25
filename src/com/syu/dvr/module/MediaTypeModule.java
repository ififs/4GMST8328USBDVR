package com.syu.dvr.module;

import java.util.ArrayList;
import java.util.List;

import com.syu.dvr.utils.LogCatUtils;

public class MediaTypeModule {
	private List<String> mVideo;
	private List<String>mPhoto;
	private List<String>mLock;
	private List<String>mEvent;
	public static MediaTypeModule module;
	public static MediaTypeModule getInstance(){
		if (module==null) {
			synchronized(MediaTypeModule.class){
				if (module==null) {
					module=new MediaTypeModule();
				}
			}
		}
		return module;
	}
	
	public List<String> getmEvent() {
		return mEvent;
	}

	public void setmEvent(List<String> mEvent) {
		this.mEvent = mEvent;
	}

	public List<String> getmVideo() {
		
		return this.mVideo;
	}
	public void setmVideo(List<String> mVideo) {
		this.mVideo = mVideo;
	}
	public List<String> getmPhoto() {
		return mPhoto;
	}
	public void setmPhoto(List<String> mPhoto) {
		this.mPhoto = mPhoto;
	}
	public List<String> getmLock() {
		return mLock;
	}
	public void setmLock(List<String> mLock) {
		this.mLock = mLock;
	}
	

}
