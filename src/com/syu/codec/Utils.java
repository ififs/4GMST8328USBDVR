package com.syu.codec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import com.syu.jni.CameraNative;

import android.util.Log;

public class Utils {
	public static final boolean DEBUG = true;
	public static int deviceId = -1;
	public static int videoWidth, videoHeight, fps, format;
	public static boolean inited = false;
	
	public static boolean initialize(int dev, int width, int height, int vfps, int fmt) {
		if(!DEBUG) return inited;
		deviceId = dev;
		int ret = open(deviceId);
		if(ret >= 0) {
			if(Utils.setup(width, height, vfps, fmt) >= 0) {
				videoWidth = width;
				videoHeight = height;
				fps = vfps;
				format = fmt;
				inited = true;
			}
		}
		return inited;
	}
	
	static int open(int id) {
		if(DEBUG) {
			return CameraNative.native_open_camera(id);
		}
		return -1;
	}
	
	public static void close() {
		if(DEBUG) {
			CameraNative.native_close_camera();
		}
		inited = false;
	}
	
	public static int  startPreview() {
		if(DEBUG) {
			return CameraNative.native_start_preview();
		}
		return -1;
	}
	
	public static void stopPreview() {
		if(DEBUG) {
			CameraNative.native_stop_preview();
		}
	}
	
	static int setup(int width, int height, int fps, int format) {
		if(DEBUG) {
			return CameraNative.native_init_camera(width, height, fps, format);
		}
		return -1;
	}
	
	public static long getBuffer(ByteBuffer buffer) {
		if(buffer == null) return -1;
		if(DEBUG) {
			try{
				byte [] datas = CameraNative.native_read_frame();
				if(datas != null && datas.length > 0) {
					buffer.clear();
					buffer.put(datas);
				}
				return datas != null ? datas.length : -1;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return -1;
	}
	
	public static byte[] getBuff() {
		if(DEBUG) {
			byte [] datas = CameraNative.native_read_frame();
			return datas;
		}
		return null;
	}
	
	public static byte[] getData() {
		File file = new File("/sdcard/stream.raw");
		InputStream io = null;
		byte[] buff = new byte[1024 * 128];
		try {
			io = new FileInputStream(file);
			byte[] buff1 = new byte[1024 * 128];
			int len = io.read(buff1);
			Log.e("dvr", "buff data len = " + len);
			if(len > 0) {
				buff = new byte[len];
				System.arraycopy(buff1, 0, buff, 0, len);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(io != null) {
				try {
					io.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				io = null;
				file = null;
			}
		}
		return buff;
	}
}
