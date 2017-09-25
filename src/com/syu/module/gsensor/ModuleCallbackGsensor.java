/**
 * 版权：深圳深青联科技有限公司
 * 设计:	 柯华栋
 * 代码：深青联研发部/Android组
 * 日期：2015年1月1日
 */

package com.syu.module.gsensor;

import com.syu.dvr.utils.LogCatUtils;
import com.syu.ipc.IModuleCallback;

import android.os.RemoteException;

public class ModuleCallbackGsensor extends IModuleCallback.Stub {
	
	private static final ModuleCallbackGsensor INSTANCE = new ModuleCallbackGsensor();
	
	public static ModuleCallbackGsensor getInstance() {
		return INSTANCE;
	}
	
	private ModuleCallbackGsensor() {}
	
	
	public static final int U_LOCK_RECORD			= 1;	// G-Sensor发生碰撞感应，告诉ARM锁定录像
	public static final int U_SLEEP_STOP_RECORD		= 2;	// 休眠状态下G-sensor触发后 告诉ui停止录像
	public static final int U_GSENSOR_ON			= 3;	// 碰撞感应器工作开关
	public static final int U_WORK_SENSITIVITY		= 4;	// 工作感应灵敏度设定
	public static final int U_STANDBY_SENSITIVITY	= 5;	// 待机感应灵敏度设定
	public static final int U_STANDBY_RECORD_TIME	= 6;	// 待机碰撞触发录像时间设定（秒）
	public static final int U_STANDBY_COLLIDE_SCREENON= 7;	// 待机碰撞触发亮屏
	public static final int U_SLEEP_30S_COLLIDE_RECORD= 8;	// 休眠30秒内碰撞触发是否录像
	
	@Override
	public void update(int updateCode, int[] ints, float[] flts, String[] strs) throws RemoteException {
		HandlerGsensor.update(updateCode, ints);
	}
}
