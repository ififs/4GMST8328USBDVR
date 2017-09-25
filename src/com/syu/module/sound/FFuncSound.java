package com.syu.module.sound;


public class FFuncSound {
	public static void showVol()
	{
		DataSound.PROXY.cmd(FinalSound.C_VOL,FinalSound.VOL_SHOW_UI);
	}

}
