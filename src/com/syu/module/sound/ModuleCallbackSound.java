package com.syu.module.sound;

import android.os.RemoteException;

import com.syu.ipc.IModuleCallback;

public class ModuleCallbackSound extends IModuleCallback.Stub {
	private static final ModuleCallbackSound INSTANCE = new ModuleCallbackSound();

	public static ModuleCallbackSound getInstance() {
		return INSTANCE;
	}

	private ModuleCallbackSound() {
	}

	public boolean intsOk(int[] ints, int min) {
		return ints != null && ints.length >= min;
	}

	@Override
	public void update(int updateCode, int[] ints, float[] flts, String[] strs)
			throws RemoteException {
		if (updateCode >= 0 && updateCode < FinalSound.U_CNT_MAX) {
			switch (updateCode) {
			
			default: {
				if (intsOk(ints, 1))
					HandlerSound.update(updateCode, ints[0]);
				break;
			}
			}
		}
	}

}
