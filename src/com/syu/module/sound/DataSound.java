package com.syu.module.sound;

import com.syu.ipc.RemoteModuleProxy;
import com.syu.module.UiNotifyEvent;

public class DataSound {
	public static final RemoteModuleProxy PROXY			= new RemoteModuleProxy();
	public static final int[] DATA						= new int[FinalSound.U_CNT_MAX];
	
	public static final UiNotifyEvent[] NOTIFY_EVENTS = new UiNotifyEvent[FinalSound.U_CNT_MAX];
	static {
		for (int i = 0; i < FinalSound.U_CNT_MAX; i++)
			NOTIFY_EVENTS[i] = new UiNotifyEvent(i);
	}
	
}
