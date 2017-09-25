package com.syu.module.sound;

import android.os.RemoteException;

import com.syu.dvr.utils.LogCatUtils;
import com.syu.ipc.FinalMainServer;
import com.syu.ipc.IRemoteToolkit;
import com.syu.module.ConnectionObserver;

public class ConnectionSound implements ConnectionObserver {
	private static final ConnectionSound INSTANCE = new ConnectionSound();
	
	public static ConnectionSound getInstance()
	{
		return INSTANCE;
	}
	
	public ConnectionSound(){};

	@Override
	public void onConnected(IRemoteToolkit toolkit) {
		try {
			DataSound.PROXY.setRemoteModule(toolkit.getRemoteModule(FinalMainServer.MODULE_CODE_SOUND));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		ModuleCallbackSound callback = ModuleCallbackSound.getInstance();
		DataSound.PROXY.register(callback, FinalSound.U_VOL, 1);
		DataSound.PROXY.register(callback, FinalSound.U_MUTE, 1);
	}

	@Override
	public void onDisconnected() {
		DataSound.PROXY.setRemoteModule(null);
	}

}
