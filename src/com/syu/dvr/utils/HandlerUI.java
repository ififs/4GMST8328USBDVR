/**
 * 版权：深圳深青联科技有限公司
 * 设计:	 柯华栋
 * 代码：深青联研发部/Android组
 * 日期：2015年1月1日
 */

package com.syu.dvr.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * 用来刷新UI,不做额外的数据处理,有木有
 */
public class HandlerUI extends Handler {

	private static final HandlerUI INSTANCE = new HandlerUI();
	
	public static HandlerUI getInstance() {
		return INSTANCE;
	}
	
	private HandlerUI() { super(Looper.getMainLooper()); }
}
