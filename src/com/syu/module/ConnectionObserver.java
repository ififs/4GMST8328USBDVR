/**
 * 版权：深圳深青联科技有限公司
 * 设计:	 柯华栋
 * 代码：深青联研发部/Android组
 * 日期：2015年1月1日
 */

package com.syu.module;

import com.syu.ipc.IRemoteToolkit;

public interface ConnectionObserver {
	void onConnected(IRemoteToolkit toolkit);
	void onDisconnected();
}
