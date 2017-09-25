/**
 * 版权：深圳深青联科技有限公司
 * 设计:	 柯华栋
 * 代码：深青联研发部/Android组
 * 日期：2015年1月1日
 */

package com.syu.module.gsensor;

public class HandlerGsensor {
	public static void update(int updateCode, int[] ints) {
		if (ints == null || ints.length == 0)
			return;
			DataGsensor.NOTIFY_EVENTS[updateCode].onNotify(ints,null,null);
	}
	public static void update(int updateCode, int value) {
		if (DataGsensor.DATA[updateCode] != value) {
			DataGsensor.DATA[updateCode] = value;
			DataGsensor.NOTIFY_EVENTS[updateCode].onNotify();
		}
	}
	
}
