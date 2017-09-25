/**
 * 版权：深圳深青联科技有限公司
 * 设计:	 柯华栋
 * 代码：深青联研发部/Android组
 * 日期：2015年1月1日
 */

package com.syu.module.gsensor;


public class FinalGsensor {
	public static final int MODULE_GSENSOR_NULL		= 0;
	public static final int MODULE_GSENSOR_BY_MCU	= 1;
	
	public static final int C_START_RECORD			= 0;	// 1/0 告诉MCU开始/停止录像
	public static final int C_GSENSOR_ON			= 1;	// 碰撞感应器工作开关
	public static final int C_WORK_SENSITIVITY		= 2;	// 工作感应灵敏度设定
	public static final int C_STANDBY_SENSITIVITY	= 3;	// 待机感应灵敏度设定
	public static final int C_STANDBY_RECORD_TIME	= 4;	// 待机碰撞触发录像时间设定（秒）
	public static final int C_STANDBY_COLLIDE_SCREENON= 5;	// 待机碰撞触发亮屏
	public static final int C_SLEEP_30S_COLLIDE_RECOED= 6;	// 休眠30秒内碰撞触发是否录像
	
	public static final int U_EXIST_GSENSOR			= 0;	// 检测硬件是否有G-Sensor功能
	public static final int U_LOCK_RECORD			= 1;	// G-Sensor发生碰撞感应，告诉ARM锁定录像
	public static final int U_SLEEP_STOP_RECORD		= 2;	// 休眠状态下G-sensor触发后 告诉ui停止录像
	public static final int U_GSENSOR_ON			= 3;	// 碰撞感应器工作开关
	public static final int U_WORK_SENSITIVITY		= 4;	// 工作感应灵敏度设定
	public static final int U_STANDBY_SENSITIVITY	= 5;	// 待机感应灵敏度设定
	public static final int U_STANDBY_RECORD_TIME	= 6;	// 待机碰撞触发录像时间设定（秒）
	public static final int U_STANDBY_COLLIDE_SCREENON= 7;	// 待机碰撞触发亮屏
	public static final int U_SLEEP_30S_COLLIDE_RECORD= 8;	// 休眠30秒内碰撞触发是否录像
	
	// 工作/待机感应灵敏度设定
	public static final int GSENSOR_SENSITIVITY_LEVEL_0 = 0;//非常灵敏
	public static final int GSENSOR_SENSITIVITY_LEVEL_1 = 1;//灵敏
	public static final int GSENSOR_SENSITIVITY_LEVEL_2 = 2;//标准
	public static final int GSENSOR_SENSITIVITY_LEVEL_3 = 3;//不灵敏
	public static final int GSENSOR_SENSITIVITY_LEVEL_4 = 4;//非常不灵敏
	
	// 待机碰撞触发录像时间设定（秒）
	public static final int STANDBY_RECORD_TIME_30	= 0;	// 30s
	public static final int STANDBY_RECORD_TIME_60	= 1;	// 60s
	public static final int STANDBY_RECORD_TIME_90	= 2;	// 90s
	public static final int STANDBY_RECORD_TIME_120	= 3;	// 120s
	
	public static final int U_CNT_MAX				= 10;
}
