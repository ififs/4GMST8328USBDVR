package com.syu.dvr.widget;

import java.util.List;

import com.syu.dvr.module.MediaInfor;

import android.R.integer;
import android.app.Activity;
import android.graphics.Bitmap;

public class ComInterface {
	
	
	public interface AllMediaFileCallback{
		void getFileCallback(List<String>times,List<List<MediaInfor>>dataList);
	}
	
	public interface OutAllActivity{
		void outAllActivity();
	}
	public interface CamOpenOverCallback{
		void camerahasOpened();
	}
	public interface CamarePotoLimit{//拍照快健使用频率限制
		void camarePotoLimit(boolean isStat);
	}
	public interface CamareExit{
		void camaraExitCallbak();
	}
	public interface VideoFileEdit{
		
		void pictureFill(String paString);
		boolean deleteFileVideo(int list, int nuber);
	}
	public interface ToHandle{
		void tostartActivity(Class<? extends Activity> target, String time);
	}
	public interface FillZoom{
		void fillZoom();
	}
	public interface ExitFrament{
		void exitFrament();
	}
	public interface Swith{
		void swith();
	}
	public interface LoadLockFile{
		void loadFish(int[] array, int mCount);
	}
	public interface GetPhotoNumber{
		void photonumber(int number);
	}
	public interface CaptureVideo{
		void captureVideoCallback(int flag, String path);
	}
	public interface CapturePhotos{
		void capturePhoto(int flag, String path);
	}
	
	public interface DeleteFileCallback{
		void callback();
	}
	public interface LoadAllDataCallback{
		void callback(List<String> listData, List<String> mDataTime,List<String>mLockTime);
	}
	public interface CloseActivityFinsh{
		void closeActivityFinsh();
	}
	public interface ImageCache{
		Bitmap get(String path);
		void put(String path, Bitmap bitmap);
	}
	
}
