/**
 * 版权：深圳深青联科技有限公司
 * 设计:	 柯华栋
 * 代码：深青联研发部/Android组
 * 日期：2015年1月1日
 */

package com.syu.module.main;

public class HandlerMain {
	public static void update(int updateCode, int[] ints) {
		if (ints == null || ints.length == 0)
			return;
			DataMain.NOTIFY_EVENTS[updateCode].onNotify(ints,null,null);
	}
	public static void update(int updateCode, int value) {
		if (DataMain.DATA[updateCode] != value) {
			DataMain.DATA[updateCode] = value;
			DataMain.NOTIFY_EVENTS[updateCode].onNotify();
		}
	}
	
}
