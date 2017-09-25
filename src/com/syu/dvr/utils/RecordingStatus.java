package com.syu.dvr.utils;


public class RecordingStatus {
	
	private int mRecordingStatus=0;
	
	private int mFileStatus=0;
	
	private int mPhotoStatus=0;
	
	private int mScardStatus=0;
	
	private int mFileLock=0;
	
	private int mResolution=0;
	
	private boolean misRecordAudio;
	
	private boolean isSdSpaceFull;
	
	private int isSdWriteError=0;
	
	private boolean isHasMic=false;
	
	
	public static RecordingStatus status;
	public static RecordingStatus getInstance(){
		if (status==null) {
			synchronized (RecordingStatus.class) {
				if (status==null) {
					status=new RecordingStatus();
				}
			}
		}
		return status;
	}
	
	public int getIsSdWriteError() {
		return isSdWriteError;
	}

	public void setIsSdWriteError(int isSdWriteError) {
		this.isSdWriteError = isSdWriteError;
	}

	public boolean isHasMic() {
		return isHasMic;
	}

	public void setHasMic(boolean isHasMic) {
		this.isHasMic = isHasMic;
	}

	public void clearStatus(){
		if (status!=null) {
			status=null;
		}
	}
	public boolean isSdSpaceFull() {
		return isSdSpaceFull;
	}


	public void setSdSpaceFull(boolean isSdSpaceFull) {
		this.isSdSpaceFull = isSdSpaceFull;
	}


	public int getmResolution() {
		return mResolution;
	}

	public void setmResolution(int mResolution) {
		this.mResolution = mResolution;
	}

	public boolean isMisRecordAudio() {
		return misRecordAudio;
	}

	public void setMisRecordAudio(boolean misRecordAudio) {
		this.misRecordAudio = misRecordAudio;
	}

	public int getmFileLock() {
		return mFileLock;
	}

	public void setmFileLock(int mFileLock) {
		this.mFileLock = mFileLock;
		
	}

	public int getmRecordingStatus() {
		return mRecordingStatus;
	}

	public void setmRecordingStatus(int mRecordingStatus) {
		this.mRecordingStatus = mRecordingStatus;
	}

	public int getmFileStatus() {
		return mFileStatus;
	}

	public void setmFileStatus(int mFileStatus) {
		this.mFileStatus = mFileStatus;
	}
	public int getmPhotoStatus() {
		return mPhotoStatus;
	}

	public void setmPhotoStatus(int mPhotoStatus) {
		this.mPhotoStatus = mPhotoStatus;
	}

	public int getmScardStatus() {
		return mScardStatus;
	}

	public void setmScardStatus(int mScardStatus) {
		this.mScardStatus = mScardStatus;
	}
}
