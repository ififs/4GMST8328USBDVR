/**
 * 版权：深圳深青联科技有限公司
 * 设计:	 柯华栋
 * 代码：深青联研发部/Android组
 * 日期：2015年1月1日
 */

package com.syu.ipc;

import android.os.RemoteException;

public class RemoteModuleProxy extends IRemoteModule.Stub {
	
	private IRemoteModule mRemoteModule;
	
	public IRemoteModule getRemoteModule() {
		return mRemoteModule;
	}
	
	public void setRemoteModule(IRemoteModule remoteModule) {
		mRemoteModule = remoteModule;
	}

	@Override
	public void cmd(int cmdCode, int[] ints, float[] flts, String[] strs) {
		IRemoteModule remoteModule = mRemoteModule;
		if (remoteModule != null) {
			try {
				remoteModule.cmd(cmdCode, ints, flts, strs);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	// 方便使用
	public void cmd(int cmdCode) {
		IRemoteModule remoteModule = mRemoteModule;
		if (remoteModule != null) {
			try {
				remoteModule.cmd(cmdCode, null, null, null);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	// 方便使用
	public void cmd(int cmdCode, int value) {
		IRemoteModule remoteModule = mRemoteModule;
		if (remoteModule != null) {
			try {
				remoteModule.cmd(cmdCode, new int[]{value}, null, null);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	// for 方便使用
	public void cmd(int cmdCode, String value) {
		IRemoteModule remoteModule = mRemoteModule;
		if (remoteModule != null) {
			try {
				remoteModule.cmd(cmdCode, null, null, new String[]{value});
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	// 方便使用
	public void cmd(int cmdCode, int value1, int value2) {
		IRemoteModule remoteModule = mRemoteModule;
		if (remoteModule != null) {
			try {
				remoteModule.cmd(cmdCode, new int[]{value1, value2}, null, null);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	// 方便使用
	public void cmd(int cmdCode, int ... args) {
		IRemoteModule remoteModule = mRemoteModule;
		if (remoteModule != null) {
			try {
				remoteModule.cmd(cmdCode, args, null, null);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public ModuleObject get(int getCode, int[] ints, float[] flts, String[] strs) {
		IRemoteModule remoteModule = mRemoteModule;
		if (remoteModule != null) {
			try {
				return remoteModule.get(getCode, ints, flts, strs);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 方便使用
	 */
	public ModuleObject get(int getCode, int value) {
		IRemoteModule remoteModule = mRemoteModule;
		if (remoteModule != null) {
			try {
				return remoteModule.get(getCode, new int[]{value}, null, null);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 方便使用
	 */
	public int getI(int getCode, int valueIfNotOk) {
		IRemoteModule remoteModule = mRemoteModule;
		if (remoteModule != null) {
			try {
				ModuleObject obj =  remoteModule.get(getCode, null, null, null);
				if (obj != null && obj.ints != null && obj.ints.length >= 1) {
					return obj.ints[0];
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return valueIfNotOk;
	}
	
	/**
	 * 方便使用
	 */
	public String getS(int getCode, int value) {
		IRemoteModule remoteModule = mRemoteModule;
		if (remoteModule != null) {
			try {
				ModuleObject obj =  remoteModule.get(getCode, new int[]{value}, null, null);
				if (obj != null && obj.strs != null && obj.strs.length >= 1) {
					return obj.strs[0];
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 方便使用
	 */
	public String getS(int getCode, int value1, int value2) {
		IRemoteModule remoteModule = mRemoteModule;
		if (remoteModule != null) {
			try {
				ModuleObject obj =  remoteModule.get(getCode, new int[]{value1, value2}, null, null);
				if (obj != null && obj.strs != null && obj.strs.length >= 1) {
					return obj.strs[0];
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	

	@Override
	public void register(IModuleCallback callback, int updateCode, int update) {
		IRemoteModule remoteModule = mRemoteModule;
		if (remoteModule != null) {
			try {
				remoteModule.register(callback, updateCode, update);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void unregister(IModuleCallback callback, int updateCode) {
		IRemoteModule remoteModule = mRemoteModule;
		if (remoteModule != null) {
			try {
				remoteModule.unregister(callback, updateCode);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
}
