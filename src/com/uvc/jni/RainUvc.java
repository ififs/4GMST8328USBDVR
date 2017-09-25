package com.uvc.jni;

public class RainUvc {
	private static final String TAG = "RainUvc";
    
    public final static class FileInfo {
	   public int resolution;   //0:1080P, 1:720P, 0xFF:Unknown
	   public int frame_rate;
	   public int file_length;   //in seconds
	   public int file_size;   //in bytes
    };
    
    public final static class TimeInfo {
	   public short year;   
	   public short month;
	   public short day;
	   public short hour;
	   public short minute;
	   public short second;

	   @Override
	   public String toString() {
		   return String.format("%d-%d-%d %d:%d:%d", year,month,day,hour,minute,second); 
	   }
	};

    static {
        System.loadLibrary("rain_uvc"); 
    }
    /**
     * get the usb device pid vid
     * videopath:
     * the video node ; e:/dev/video5
     * @return
     * (pid << 16 | vid)
     */
    public static native long getPVId(String videopath);
    /**
     * setVideoPath
     * videopath:
     * the video node ; 默认:/dev/video5
     * @return
     * < 0: error
     */
    public static native int setVideoPath(String videopath);
    /**
     * mode
     * 1: Normal Mode: start liveview and record
     * 2: Playback (File browser) Mode: stop liveview and record
     * @return
     * < 0: error
     */
    public static native int setMode(int mode);
    /**
     * @return
     * 1: Normal Mode: start liveview and record
     * 2: Playback (File browser) Mode: stop liveview and record
     * < 0: error
     */
    public static native int getMode();
    /**
     * Get total files in the specified directory
     * int getTotalFiles(int type)
     * type:
     * 0: DCIM, 正常
     * 1: EVENT, 緊急
     * 2: LOCK, 加鎖
     * 3: JPEG, 拍照
     * @return int
     * < 0 error
     * >= 0 the nums in dir
     */
    public static native int getTotalFiles(int type);
    /**
     * Get the file name
     * type:
     * 0: DCIM, 正常
     * 1: EVENT, 緊急
     * 2: LOCK, 加鎖
     * 3: JPEG, 拍照
     * id:
     * the index of the file in the directory(type)
     * @return
     * == NULL: error
     */
    public static native String getFileName(int type,int id);
    /** 
     * Get the file information
     * type:
     * 0: DCIM, 正常
     * 1: EVENT, 緊急
     * 2: LOCK, 加鎖
     * 3: JPEG, 拍照
     * id:
     * the index of the file in the directory(type)
     * @return
     * == NULL: error
     */
    public static native FileInfo getFileInfo(int type,int id);
    /** 
     * Get the file information from name
     * type:
     * 0: DCIM, 正常
     * 1: EVENT, 緊急
     * 2: LOCK, 加鎖
     * 3: JPEG, 拍照
     * file:
     * the file which want to get the info
     * @return
     * == NULL: error
     */
    public static native FileInfo getFileInfoFromName(int type,String file);
    /**
     * Play file
     * type:
     * 0: DCIM, 正常
     * 1: EVENT, 緊急
     * 2: LOCK, 加鎖
     * 3: JPEG, 拍照
     * file:
     * the file which want to play
     * @return
     * < 0: error
     */
    public static native int startPlayFile(int type, String file);
    /**
     * Pause the playing file
     * @return
     * < 0: error
     */
    public static native int pausePlayFile();
    /**
     * Stop the playing file
     * @return
     * < 0: error
     */
    public static native int stopPlayFile();
    /**
     * Query the playing remaining time
     * @return
     * the remaining seconds
     * < 0: error
     */
    public static native int getPlayingTime();
    /**
     * get the playing time
     * @return
     * the playing time in seconds
     * < 0: error
     */
    public static native int queryPlayRemainTime();
    /**
     * Resume the playing file
     * @return
     * < 0: error
     */
    public static native int resumePlayFile();
    /**
     * Set the resolution
     * resolution:
     * 0: 1080P
     * 1: 720P
     * @return
     * < 0: error
     */
    public static native int setResolution(int resolution);
    /**
     * Set the recorded length
     * length:
     * the recorded length in minutes
     * @return
     * < 0: error
     */
    public static native int setRecLength(int length);
    /**
     * Set the frequence
     * frequence:
     * 0: 60Hz
     * 1: 50Hz
     * @return
     * < 0: error
     */
    public static native int setFrequence(int frequence);
    /**
     * Snapshot
     * @return
     * < 0: error
     */
    public static native int snapshot();
    /**
     * Start to record
     * @return
     * < 0: error
     */
    public static native int startRecord();
    /**
     * Stop to record
     * @return
     * < 0: error
     */
    public static native int stopRecord();
    /**
     * Enable/Disable to record audio
     * enable:
     * 0: disable to record audio
     * 1: enable to record audio
     * @return
     * < 0: error
     */
    public static native int recordAudio(int enable);
    /**
     * Format TF card
     * @return
     * < 0: error
     */
    public static native int formatTFCard();
    /**
     * Set the time information
     * time:
     * 要同步的时间
     * @return
     * < 0: error
     */
    public static native int setTime(TimeInfo time);
    /**
     * Get the video record status
     * @return
     * 0: no record, 1: recording
     * < 0: error
     */
    public static native int getVideoRecordStatus();
    /**
     * Get the audio record status
     * @return
     * 0: disable, 1: enable
     * < 0: error
     */
    public static native int getAudioRecordStatus();
    /**
     * Get the TF card status
     * @return
     * 0: no card or can not be written 1: card exist and ready for writing
     * < 0: error
     */
    public static native int getTFCardStatus();
    /**
     * Get the Gsensor status
     * @return
     * 0: no event appear, 1: evnt appear
     * < 0: error
     */
    public static native int getGSensorStatus();
    /**
     * Set the Gsensor status
     * @return
     * 0: no event appear, 1: evnt appear
     * < 0: error
     */
    public static native int setGSensorStatus(int lock);
    /**
     * Delete file
     * type:
     * 0: DCIM, 正常
     * 1: EVENT, 緊急
     * 2: LOCK, 加鎖
     * 3: JPEG, 拍照
     * file:
     * the file which want to DELETE
     * @return
     * < 0: error
     */
    public static native int deleteFile(int type, String file);
    /**
     * Get the resolution
     * @return
     * 0: 1080P  1: 720P
     * < 0: error
     */
    public static native int getResolution();
    /**
     * Get the recorded length
     * @return
     * the recorded length in minutes
     * < 0: error
     */
    public static native int getRecLength();
    /**
     * Get the frequence
     * @return
     * 0: 60Hz  1: 50Hz
     * < 0: error
     */
    public static native int getFrequence();
    /**
     * Seek playing time
     * seconds:
     * the time in seconds to seek
     * @return
     * < 0: error
     */
    public static native int seekPlayingTime(int seconds);
    /**
     * Open read file
     * type:
     * 0: DCIM, 正常
     * 1: EVENT, 緊急
     * 2: LOCK, 加鎖
     * 3: JPEG, 拍照
     * file:
     * the file which want to open
     * @return
     * < 0: error
     */
    public static native int openReadFile(int type, String file);
    /**
     * Get read file length
     * @return
     * the file length
     * < 0: error
     */
    public static native int getReadFileLength();
    /**
     * Get file data
     * seekoff
     * 單位60KB
     * @return
     * the file data buff
     * < 0: error
     */
    public static native byte[] getReadFileData(int seekoff,int remainSize);
    /**
     * Close read file
     * @return
     * < 0: error
     */
    public static native int closeReadFile();
    /**
     * pull file from the camera
     * type:
     * 0: DCIM, 正常
     * 1: EVENT, 緊急
     * 2: LOCK, 加鎖
     * 3: JPEG, 拍照
     * file: 要pull出来的文件名
     * savePath: pull 出来保存的路径（包括文件名）
     * @return
     * < 0: error
     */
    public static native int pullFile(int type, String file, String savePath);    
    /**
     * Get the encrypt ic status
     * @return
     * encrypt ic status : 0 error, 1 success, 2 checking
     * < 0: error
     */
    public static native int getEncryptStatus();

    /**
     * shake hand with rain camera
     * time: the curr time
     * @return
     *  handshake result 1:success else:error
     * < 0: error
     */
    public static native int shakeHand(TimeInfo time);
    /**
     * Get platform version
     * @return
     * the version string .e:FYT302-v1.0.1
     * < 0: error
     */
    public static native String getPlatformVersion();

    /**
     * is sd space full
     * @return
     *  0 no full, else full
     * < 0: error
     */
    public static native int isSdSpaceFull();
    /**
     * load default parameter
     *
     * @return
     * < 0: error
     */
    public static native int loadDefaultParam();
    /**
     * reboot camera
     *
     * @return
     * < 0: error
     */
    public static native int reboot();
    /**
     * is sdcard write error
     * @return
     *  0 no error, else error
     * < 0: error
     */
    public static native int isSdWriteError();
    /**
     * do dvr has mic?
     * @return
     *  0 no , else yes
     * < 0: error
     */
    public static native int doHasMic();
    /**
     * is file exist?
     * @return
     *  0 no exit , else yes
     * < 0: error
     */
    public static native int isFileExist(int type, String file);
    /**
     * get file buffer from the camera
     * type:
     * 0: DCIM, 正常
     * 1: EVENT, 緊急
     * 2: LOCK, 加鎖
     * 3: JPEG, 拍照
     * file: 要pull出来的文件名
     * @return
     * the file data buff
     * < 0: error
     */
    public static native byte[] getReadFileAllData(int type, String file);
    /**
     * is TF card with speed class10?
     * @return
     *  0 no , else yes
     * < 0: error
     */
    public static native int isTFClass10();
}
