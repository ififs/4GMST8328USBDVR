/**
 * 版权：深圳深青联科技有限公司
 * 设计:	 柯华栋
 * 代码：深青联研发部/Android组
 * 日期：2015年1月1日
 */

package com.syu.module.main;

import com.syu.dvr.TheApp;
import com.syu.dvr.activity.BaseActivity;
import com.syu.dvr.activity.MainActivity;
import com.syu.dvr.utils.Config;
import com.syu.dvr.utils.LogCatUtils;
import com.syu.dvr.utils.PublicClass;
import com.syu.dvr.widget.ComInterface.OutAllActivity;
import com.syu.ipc.FinalMainServer;
import com.syu.ipc.IRemoteToolkit;
import com.syu.module.ConnectionObserver;
import com.syu.module.IUiNotify;

import android.app.Activity;
import android.os.Handler;
import android.os.RemoteException;

public class ConnectionMain implements ConnectionObserver {
	public static final ConnectionMain INSTANCE = new ConnectionMain();
	private PublicClass publicClass=new PublicClass();
	public static ConnectionMain getInstance() {
		return INSTANCE;
	}
	
	public ConnectionMain() {}
	
	@Override
	public void onConnected(IRemoteToolkit toolkit) {//连接后注册回调
		try {
			DataMain.PROXY.setRemoteModule(toolkit.getRemoteModule(FinalMainServer.MODULE_CODE_MAIN));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		//注册回调数据  0表示不与服务同步，1表示与服务同步，注册时也会返回状态
		ModuleCallbackMain callback = ModuleCallbackMain.getInstance();
		DataMain.PROXY.register(callback, FinalMain.U_ARM_SLEEP_WAKEUP, 0);   //ACC状态更新
		DataMain.PROXY.register(callback, FinalMain.U_ACC_ON, 0);   //ACC状态更新
		DataMain.NOTIFY_EVENTS[FinalMain.U_ARM_SLEEP_WAKEUP].addNotify(notify, 1);  //摄像头监听回调
		DataMain.NOTIFY_EVENTS[FinalMain.U_ACC_ON].addNotify(notify, 1);   //ACC监听回调
	}

	@Override
	public void onDisconnected() {
		
		DataMain.PROXY.setRemoteModule(null);
	}
	
	IUiNotify notify =new IUiNotify() {
		
		@Override
		public void onNotify(int updateCode, int[] ints, float[] flts, String[] strs) {
			switch (updateCode) {
			case FinalMain.U_ARM_SLEEP_WAKEUP:
				if (ints==null||ints.length<=0) {
					return;
				}
				if (ints[0]==0) {
					if (TheApp.mCManager!=null) {
						TheApp.mCManager.stopRecord(TheApp.getDeviceId());
					}
				}
				break;
				
			case FinalMain.U_ACC_ON:
				if (ints==null||ints.length<=0) {
					return;
				}
				if (ints[0]==0) {
					if (BaseActivity.app==null||BaseActivity.app.isFinishing()) {
						return;
					}
					OutAllActivity outAllActivity=BaseActivity.app;
					outAllActivity.outAllActivity();
				}
				break;
				
			default:
				break;
			}
			
		}
	};
	private Handler handler=new Handler();

}
