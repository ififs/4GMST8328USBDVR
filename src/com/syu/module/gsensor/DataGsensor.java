/**
 * 版权：深圳深青联科技有限公司
 * 设计:	 柯华栋
 * 代码：深青联研发部/Android组
 * 日期：2015年1月1日
 */

package com.syu.module.gsensor;


import com.syu.ipc.RemoteModuleProxy;
import com.syu.module.UiNotifyEvent;

public class DataGsensor {
	public static final RemoteModuleProxy PROXY			= new RemoteModuleProxy();
	public static final int[] DATA						= new int[FinalGsensor.U_CNT_MAX];
	public static final UiNotifyEvent[] NOTIFY_EVENTS = new UiNotifyEvent[FinalGsensor.U_CNT_MAX];
	static {
		for (int i = 0; i < FinalGsensor.U_CNT_MAX; i++)
			NOTIFY_EVENTS[i] = new UiNotifyEvent(i);
	}
	
//-------------------------------------------------------------------------------------------------
}
