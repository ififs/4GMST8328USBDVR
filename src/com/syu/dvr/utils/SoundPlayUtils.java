package com.syu.dvr.utils;

import com.syu.dvr.R;
import com.syu.dvr.TheApp;
import com.syu.module.sound.DataSound;
import com.syu.module.sound.FinalSound;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class SoundPlayUtils {
	
	private static SoundPool soundPool;
	private static SoundPlayUtils soundPlayUtils;
	
	public static SoundPlayUtils getInstance(){
		if (soundPlayUtils==null) {
			synchronized (SoundPlayUtils.class) {
				if (soundPlayUtils==null) {
					soundPool=init(TheApp.mApp);
					soundPlayUtils=new SoundPlayUtils();
				}
			}
		}
		return soundPlayUtils;
	}
	
	@SuppressWarnings("deprecation")
	private static SoundPool init(Context context){
		
		if (Build.VERSION.SDK_INT>=21) {
			SoundPool.Builder builder=new SoundPool.Builder();
			builder.setMaxStreams(10);
			AudioAttributes.Builder atBuilder=new AudioAttributes.Builder();
			atBuilder.setLegacyStreamType(AudioManager.STREAM_SYSTEM);
			builder.setAudioAttributes(atBuilder.build());
			soundPool=builder.build();
		}else{
			soundPool=new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		}
		/*soundPool.load(context, R.raw.shutter, 1);
		soundPool.load(context, R.raw.startrecord, 1);
		soundPool.load(context, R.raw.stoprecord, 1);
		soundPool.load(context, R.raw.recording, 1);
		soundPool.load(context, R.raw.pleaserecording, 1);
		soundPool.load(context, R.raw.playbackmode, 1);*/
		return soundPool;
	}
	
	public void soundpoolPlay(int sounceID){
		if (soundPool!=null) {
			
			DataSound.PROXY.cmd(FinalSound.C_TIPS_TTS_AUDIO, 1);
			
//			soundPool.play(sounceID, 1, 1, 0, 0, 1);
		}
		
	}
	

}
