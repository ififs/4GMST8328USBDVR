/**
 * 版权：深圳深青联科技有限公司
 * 设计:	 柯华栋
 * 代码：深青联研发部/Android组
 * 日期：2015年1月1日
 */

package com.syu.module.gsensor;

import android.R.integer;
import android.content.ComponentName;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;

import com.syu.dvr.TheApp;
import com.syu.dvr.server.CollisionVideoService;
import com.syu.dvr.utils.LogCatUtils;
import com.syu.ipc.FinalMainServer;
import com.syu.ipc.IRemoteToolkit;
import com.syu.module.ConnectionObserver;

public class ConnectionGsensor implements ConnectionObserver {
	
	private static final ConnectionGsensor INSTANCE = new ConnectionGsensor();
	
	public static ConnectionGsensor getInstance() {
		return INSTANCE;
	}
	
	private ConnectionGsensor() {}
	
	@Override
	public void onConnected(IRemoteToolkit toolkit) {
		try {
			DataGsensor.PROXY.setRemoteModule(toolkit.getRemoteModule(FinalMainServer.MODULE_CODE_GSENSOR));
			LogCatUtils.showString("==MODULE_CODE_GSENSOR==");
		} catch (RemoteException e) {
			e.printStackTrace();
			return;
		}
		
		ModuleCallbackGsensor callback = ModuleCallbackGsensor.getInstance();
		for (int i = 0; i < FinalGsensor.U_CNT_MAX; i++) {
			DataGsensor.PROXY.register(callback, i, 1);
		}
		
	}
	@Override
	public void onDisconnected() {
		DataGsensor.PROXY.setRemoteModule(null);
		Log.d("dvr", "onDisconnected onDisconnectedonDisconnected");
	}
}
