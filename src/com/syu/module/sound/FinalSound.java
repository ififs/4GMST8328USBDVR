/**
 * ��Ȩ�������������Ƽ����޹�˾
 * ���:	 �»���
 * ���룺�������з���/Android��
 * ���ڣ�2015��1��1��
 */

package com.syu.module.sound;

public class FinalSound {
	public static final int MODULE_NULL					= 0;
	public static final int MODULE_BD3702				= 1;
	public static final int MODULE_SPHE8288				= 2;
	public static final int MODULE_SPHE8700				= 3;
	public static final int MODULE_SYS					= 4;	// ϵͳ�������Ч
	public static final int MODULE_BD3702_PORT			= 5;
	
	// cmd(C_VOL, VOL_SHOW_UI)
	public static final int C_VOL						= 0;	// ��������
	public static final int C_EQ_GAIN					= 1;	
	public static final int C_EQ_MODE					= 2;
	public static final int C_BAL_FADE					= 3;
	public static final int C_FIELD_MODE				= 4;
	public static final int C_LOUD						= 5;
	public static final int C_SB						= 6;
	// PARAM new int[]{0/1/2}
	public static final int C_AMP						= 7;
	// PARAM new int[]{0/1/2}
	public static final int C_BEEP						= 8;
	public static final int C_NAVI_MIX					= 9;
	public static final int C_NON_NAVI_MIX_PERCENT		= 10;	// �ǵ���Ӧ�û������� [0~20]
	public static final int C_NAVI_MIX_PERCENT			= 11;
	public static final int C_DEF_VOL_ON_BOOT			= 12;	// ����Ĭ����������
	public static final int C_BACKCAR_MUTE				= 13;
	public static final int C_DEF_VOL					= 14;	// Ĭ������ֵ
	// ���ֹ��������� PARAM new int[]{index, cf} cfΪƵ�� 20~20000
	public static final int C_EQ_CF						= 15;
	public static final int C_EQ_Q						= 16;
	public static final int C_SRS_CF					= 17;
	// PARAM new int[]{index, value}
	public static final int C_SRS_GAIN					= 18;
	// PARAM new int[]{1:���������� 1~2��ָ� 0:100ms�ָ�����}
	public static final int C_STREAM_PLAYER_LOSE		= 19;
	// ������������//(0~20)
	public static final int C_BACKCAR_PERCENT			= 20;
	public static final int C_SPECTRUM_ENABLE			= 21;
	// TTS����״̬ 1����ʼ������1s��һ��  0������������
	public static final int C_TIPS_TTS_AUDIO			= 22;
	
	public static final int STREAM_MASTER				= 0;
	public static final int STREAM_PLAYER				= 1;
	public static final int U_SPECTRUM					= 0;	// Ƶ������
	public static final int U_MODULE_ID					= 1;	// ģ��ID
	// PARAM new int[]{value / value, op}
	public static final int U_VOL						= 2;	// ����
	public static final int U_MUTE						= 3;	// ��������
	public static final int U_BEEP						= 4;	// ����������
	public static final int U_DEF_VOL_ON_BOOT			= 5;	// ����Ĭ������
	public static final int U_DEF_VOL					= 6;	// Ĭ������
	public static final int U_BACKCAR_MUTE				= 7;	// ��������
	public static final int U_BAL_FADE					= 8;	// ����
	public static final int U_EQ_GAIN					= 9;	// EQƵ�� index, value
	public static final int U_EQ_MODE					= 10;	// EQģʽ
	public static final int U_LOUD						= 11;	// �����
	public static final int U_SB						= 12;	// ����ƽ��
	public static final int U_AMP						= 13;	// ���ſ���
	public static final int U_NAVI_MIX					= 14;	// 
	public static final int U_NON_NAVI_MIX_PERCENT		= 15;
	public static final int U_NAVI_MIX_PERCENT			= 16;
	// ��ʹ����ԭ����EQ, EQ���������Ҫ�����仯  (֮������USE_CAR_EQ,��ΪCANBUS������CAR_EQ,���ǿͻ�����Ҫ)
	public static final int U_USE_CAR_EQ				= 17;
	public static final int U_EQ_CF						= 18;
	// ͬU_USE_CAR_EQ,������Ҫע�����ȼ� USE_CAR_EQ>U_USE_AMP_EQ>3702/8288��EQ
	public static final int U_USE_AMP_EQ				= 19;	
	public static final int U_EQ_Q						= 20;
	public static final int U_SRS_CF					= 21;
	public static final int U_SRS_GAIN					= 22;
	// ������������
	public static final int U_BACKCAR_PERCENT			= 23;	//(0~10)
	public static final int U_SPECTRUM_ENABLE			= 24;
	public static final int U_CNT_MAX					= 50;
	
	public static final int FIELD_BAL					= 0;
	public static final int FIELD_FADE					= 1;
	
	
	public static final int EQ_BAND_BASS				= 0;
	public static final int EQ_BAND_SENOR				= 1;
	public static final int EQ_BAND_TREBLE				= 2;
	public static final int EQ_BAND_CNT_MAX				= 36;
	
	public static final int EQ_MODE_CUSTOM				= 0;	// �û�
	public static final int EQ_MODE_STANDARD			= 1;	// ��׼
	public static final int EQ_MODE_ROCK				= 2;	// ҡ��
	public static final int EQ_MODE_SOFT				= 3;	// ���
	public static final int EQ_MODE_CLASSIC				= 4;	// �ŵ�
	public static final int EQ_MODE_POP					= 5;	// ����
	public static final int EQ_MODE_HALL				= 6;	// ����
	public static final int EQ_MODE_JAZZ				= 7;	// ��ʿ
	public static final int EQ_MODE_CINEMA				= 8;	// ��Ժ
	public static final int SRS_BAND_CNT_MAX			= 36;
	
	public static final int SRS_MODE_CUSTOM				= 0;	// �û�
	
	// FIELD_MODEֻ������,������,UI�İ�ť��������(15/01/23����)
	public static final int FIELD_MODE_DRIVER			= 0;
	public static final int FIELD_MODE_FRONT			= 1;
	public static final int FIELD_MODE_ALL				= 2;
	public static final int FIELD_MODE_REAR				= 3;
	
	// ��������
	public static final int VOL_UP						= -1;
	public static final int VOL_DOWN					= -2;
	public static final int VOL_MUTE					= -3;
	public static final int VOL_UNMUTE					= -4;	// �Ǿ���
	public static final int VOL_MUTE_SWITCH				= -5;
	public static final int VOL_SHOW_UI					= -6;
	
	public static final int SB_RADIO					= 0;
	public static final int SB_DVD						= 1;
	public static final int SB_BTPHONE					= 2;
	public static final int SB_BTAV						= 3;
	public static final int SB_IPOD						= 4;
	public static final int SB_AUX						= 5;
	public static final int SB_PLAYER					= 6;
	public static final int SB_NAVI						= 7;
	public static final int SB_TV						= 8;
	public static final int SB_CAR_BTPHONE				= 9;
	public static final int SB_CAR_USB					= 10;
	public static final int SB_CAR_RADIO				= 11;
	public static final int SB_CNT_MAX					= 48;
}
