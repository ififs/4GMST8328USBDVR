/**
 * 版权：深圳深青联科技有限公司
 * 设计:	 柯华栋
 * 代码：深青联研发部/Android组
 * 日期：2015年1月1日
 */

package com.syu.module.main;

import com.syu.ipc.IModuleCallback;

import android.os.RemoteException;

public class ModuleCallbackMain extends IModuleCallback.Stub {
	
	private static final ModuleCallbackMain INSTANCE = new ModuleCallbackMain();
	
	public static ModuleCallbackMain getInstance() {
		return INSTANCE;
	}
	
	private ModuleCallbackMain() {}
	int mCount=0;
	@Override
	public void update(int updateCode, int[] ints, float[] flts, String[] strs) throws RemoteException {
		
		
		if (updateCode==FinalMain.U_REQUEST_CAMERA) {
			if(ints!= null && ints.length>1){//
				HandlerMain.update(updateCode, ints);
			}
		}else {
			if(ints!= null && ints.length>0){
				HandlerMain.update(updateCode, ints);
			}
		}
	}
}
