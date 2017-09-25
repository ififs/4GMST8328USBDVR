package com.syu.codec;

import com.syu.codec.Codec.TakePictureCallback;

public interface ICodec {
	//public void setType(int type);
	//初始化
//	void setup(int width, int height, int fps, int format);
	public void start();
	public void pause();
	public void stop();
	/**
	 * 拍照，保存格式png
	 * @param path 照片保存路径
	 * @param quality 压缩质量  0-100
	 */
	public void takePicture(String path, int quality, TakePictureCallback callback);
}
