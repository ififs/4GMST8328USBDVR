/************************************************************
  Copyright (C), 2012-2017, FYT Tech. Co., Ltd.
  FileName: touchnative.java
  Author: wusheng     Version: v1.0     Date:2015-11-5
  Description:    
  Version:       v1.0   
  Function List:   
    
   
 
***********************************************************/

package com.syu.jni;

import com.syu.codec.Utils;

public class CameraNative
{
	static 
	{
		if(Utils.DEBUG) {
			System.loadLibrary("fytcamera");
		}
	}
	public	static    native    int     native_open_camera(int id);//暂时只支持id = 0，以后可能会有两个usb camera
	public	static    native    int     native_init_camera(int width, int height, int fps, int pixelformat); //设置camera的宽和高以，帧率以及图像的格式 1=MJPEG 0=YUV 暂时默认都设置为1，先测试8328 dvr 
	public	static    native    int     native_start_preview( ); //打开camera预览
	public	static    native    byte[]    native_read_frame( ); //读取预览的frame 一次返回一帧数据
	public	static    native    void    native_stop_preview( );//停止预览
	public	static    native    void    native_close_camera( );//关闭camera
	public	static    native    int    native_send_cmdto8328(byte[] send_data,byte[] recv_data);//关闭camera
}
