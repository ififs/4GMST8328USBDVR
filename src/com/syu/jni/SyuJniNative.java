package com.syu.jni;

public class SyuJniNative {
	private static final SyuJniNative INSTANCE = new SyuJniNative();
	static {
		System.loadLibrary("syu_jni");
	}
	
	private SyuJniNative() {
	}
	
	public static SyuJniNative getInstance() {
		return INSTANCE;
	}
	
	public native int syu_jni_command(int type, Object inparam, Object outparam);
	
	// ��������
	public static final int JNI_EXE_CMD_0_TEST 	 			= 0;	// ���Խӿ�
	public static final int JNI_EXE_CMD_1_BACKCAR_MIRROR 	= 1;	// ��������
	public static final int JNI_EXE_CMD_2_SOUND_MIX		 	= 2;	// GPS������8288�õ�
	public static final int JNI_EXE_CMD_3_ENCARPTION_RESULT	= 3;	// �������IC��֤���
	public static final int JNI_EXE_CMD_4_AUDIO_STATE		= 4;	// ����ARM�Ƿ����������
	public static final int JNI_EXE_CMD_5_TURNOFF_LCDC		= 5;	// �ر���CLOCK
	public static final int JNI_EXE_CMD_6_MUTE_AMP			= 6;	// ��������
	public static final int JNI_EXE_CMD_7_AMP_MUTE_STATE	= 7;	// ��ȡ���ž���״̬
	public static final int JNI_EXE_CMD_8_RESET_GPS			= 8;	// ��λGPS
	public static final int JNI_EXE_CMD_9_POWERON_SCREEN	= 9;	// ������Ļ����
	public static final int JNI_EXE_CMD_10_LITTLE_HOM		= 10;	// ����С����
	public static final int JNI_EXE_CMD_11_VIDEO_NTSC_PAL	= 11;	// NP��֪ͨ�ײ� 0��N�� 1��P��
	
	public static final int JNI_EXE_CMD_104_WRITE_GAMMA 	= 104;	// ������ĻУ׼����

}
