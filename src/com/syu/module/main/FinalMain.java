/**
 * 版权：深圳深青联科技有限公司
 * 设计:	 柯华栋
 * 代码：深青联研发部/Android组
 * 日期：2015年1月1日
 */

package com.syu.module.main;


public class FinalMain {
	public static final int MODULE_NULL					= 0;
	public static final int MODULE_MAIN					= 1;
	
	public static final int C_APP_ID					= 0;	// APP_ID,指示当前应用类型
	public static final int C_LAMPLET_BY_TIME			= 1;	// 时间控制小灯
	public static final int C_ANY_KEY_BOOT				= 2;	// 任意键开机
	public static final int C_NAVI_ON_BOOT				= 3;	// 开机启动导航应用
	public static final int C_HANDBRAKE_ENABLE			= 4;	// 允许检测手刹状态
	public static final int C_BACKCAR_RADAR_ENABLE		= 5;	// 允许倒车显示雷达
	public static final int C_BACKCAR_TRACK_ENABLE		= 6;	// 允许倒车显示轨迹
	public static final int C_BACKCAR_MIRROR			= 7;	// 倒车镜象
	public static final int C_OSD_TIME					= 8;	// 视频叠加时间
	public static final int C_NAVI_PACKAGE				= 9;	// 导航应用包名
	// PARAM:new int[]{BRIGHT_LEVEL_XXX or 0~100}
	public static final int C_BRIGHT_LEVEL				= 10;	// 当前背光等级
	// PARAM:new int[]{0~100}
	public static final int C_BRIGHT_LEVEL_DAY			= 11;	// 白天背关等级
	// PARAM:new int[]{0~100}
	public static final int C_BRIGHT_LEVEL_NIGHT		= 12;	// 晚上背光等级
	// PARAM:new int[]{time} <=0:永不黑屏  >=30 自动黑屏时间(单位秒)
	public static final int C_AUTO_BLACK_SCREEN			= 13;	// 自动黑屏
	// PARAM:new String[]{value}
	public static final int C_MCU_SERIAL				= 14;	// MCU序列号
	public static final int C_MCU_POWER_OPTION			= 15;	// MCU电源选项(休眠/关机)
	// PARAM: new int[]{0/1/2}
	public static final int C_BLACKSCREEN				= 16;	// 黑屏
	public static final int C_MCU_ON					= 17;	// MCU开关
	// 默认0:或者null，MCU待机 1：ARM待机，显示待机LOGO.(时钟或者logo等，UI自定义)
	public static final int C_STANDBY					= 18;	// 待机状态
	public static final int C_RESET_ARM_LATER			= 19;	// n秒后复位ARM
	public static final int C_VA_CMD					= 20;	// 语音助手发送的命令
	// PARAM: new int[]{0:清除错误码  1:查询错误码}
	public static final int C_MCU_ERROR_CODE			= 21;	// MCU错误码
	// PARAM: new int[]{value1, value2}
	// value1谁发送的请求:VIDEO_ID_XXX (参见下面的常量表)
	// value2请求切到哪路视频: VIDE_ID_NULL/VIDEO_ID_XXX
	public static final int C_VIDEO_ID				= 22;
	// PARAM: new int[]{APP_ID, TYPE} new String[]{value}
	// type:0:ID3_TITLE, 1:ID3_ARTIST 2:ID3_ALBUM 
	// 3:PLAY_STATUS(ints[2]:PLAYER_CMD_XXX)
	// 4:曲目信息(ints[2]:curr;ints[3]:total 第一首为0) 
	// 5:曲目播放时间信息(ints[2]:curr;ints[3]:total 单位秒)
	// 6:循环模式：0：不循环 1：单曲循环 2：全部循环
	// 7:随机模式：0：不随机 1：随机
	// 8:文件夹名称new int[]{APP_ID, TYPE} new String[]{value}
	public static final int C_PLAY_INFO					= 23;	// 播放信息
	// PARAM: new int[]{PAGE_XXX}
	public static final int C_JUMP_PAGE					= 24;	// 进入某界面
	// PARAM: new int[]{KeyEvent.KEYCODE_XXX}
	public static final int C_KEY						= 25;
	// PARAM: new int[]{Language_xxx}
	public static final int C_LANGUAGE					= 26;
	// PARAM new int[]{TYPE} new String[]{value}
	// type:0 filePath, 1:reboot 2:带MCU文件开始位置和结束位置的升级方式int[]{2,startAddr,endAddr}
	public static final int C_MCU_UPGRADE				= 27;
	public static final int C_BACKCAR_TYPE				= 28;	// 倒车类型 0:主机倒车 1:原车倒车 
	// PARAM new int[]{0:减少/1:增加} // 直接设定类型 15/12/18
	public static final int C_PANEL_KEY_TYPE			= 29;	// 面板按键类型
	public static final int C_LAMPLET_ON_BOOT			= 30;	// 开机时,面板灯亮
	public static final int C_LAMPLET_ON_ALWAYS			= 31;	// 面板灯一直亮
	public static final int C_LAMPLET_COLOR_CTRL		= 32;	// 面板灯颜色控制开关
	public static final int C_PANORAMA_ON				= 33;	// 全景开关
	public static final int C_FAN_CYCLE					= 34;	// 风扇转动周期 0x00:不转 0xFF:常转 其他:转动周期(单位:分钟)
	// PARAM: new int[]{index, value0/1}
	public static final int C_MIRROR_UP_DOWN			= 35;	// 上下镜像开关，0:AV1镜像；1:AV2镜像；2:AV3镜像；
	// PARAM: new int[]{appId, brightness, color, contrast}
	public static final int C_VIDEO_IMAGE				= 36;
	public static final int C_OUT_BACKCAR				= 37;	// UI请求退出倒车    0:不退出 1:退出倒车
	public static final int C_FACTORY_RESET				= 38;	// 恢复出厂设置
	public static final int C_GUESTURE					= 39;	// 手势学习及开关
	public static final int C_HOST_BACKCAR_ENABLE		= 40;	// 本地倒车开关1：本机倒车 0：原车视频倒车/原车倒车
	// PARAM new String[]{key, value},注意,只允许设置sys.xxx属性(即临时系统属性)
	public static final int C_SYSTEM_PROPERTIES			= 41;	// 系统属性
	public static final int C_CUTACC_TURNOFF_LCDC		= 42;	// 断ACC关屏开关，默认关
	public static final int C_ECARLINK_ON				= 43;	// 亿连开关
	// PARAM new int[]{视频ID,水平方向起始位置,水平方向大小,垂直方向起始位置,垂直方向大小};
	public static final int C_VIDEO_POSITION			= 44;	// PX3竖屏设置图像显示的位置
	
	public static final int AUTO_BLACK_SCREEN_MIN_TIME	= 30;
	
	public static final int U_APP_ID					= 0;	// APP_ID
	public static final int U_MCU_ON					= 1;	// MCU开关
	public static final int U_STANDBY					= 2;	// 待机状态
	public static final int U_BLACK_SCREEN				= 3;	// 黑屏状态
	public static final int U_LAMPLET					= 4;	// 小灯状态
	public static final int U_ANY_KEY_BOOT				= 5;	// 任意键开机
	public static final int U_NAVI_ON_BOOT				= 6;	// 开机启动导航
	public static final int U_HANDBRAKE					= 7;	// 手刹状态
	public static final int U_HANDBRAKE_ENABLE			= 8;	// 手刹使能
	public static final int U_EXIST_SD1_ON_ARM			= 9;	// SD1挂载在ARM上
	public static final int U_EXIST_SD2_ON_ARM			= 10;	// SD2挂载在ARM上
	public static final int U_EXIST_USB_ON_ARM			= 11;	// USB挂载在ARM上				
	public static final int U_BACKCAR					= 12;	// 倒车中
	public static final int U_RADAR						= 13;	// 显示雷达UI
	public static final int U_RADAR_FL					= 14;	// 雷达信号 前左
	public static final int U_RADAR_FML					= 15;	// 雷达信号 前中左
	public static final int U_RADAR_FMR					= 16;	// 雷达信号 前中右
	public static final int U_RADAR_FR					= 17;	// 雷达信号 前右
	public static final int U_RADAR_RL					= 18;	// 雷达信号 后左
	public static final int U_RADAR_RML					= 19;	// 雷达信号 后中左
	public static final int U_RADAR_RMR					= 20;	// 雷达信号 后中右
	public static final int U_RADAR_RR					= 21;	// 雷达信号 后右
	public static final int U_BACKCAR_RADAR				= 22;	// 倒车显示雷达
	public static final int U_BACKCAR_TRACK_ENABLE		= 23;	// 倒车轨迹
	public static final int U_BACKCAR_MIRROR			= 24;	// 倒车镜像
	// 2015/9/16 TO DO ...
	public static final int U_CUT_ACC_POWER				= 25;	// 断ACC电源方式
	public static final int U_OSD_TIME					= 26;	// 视频显示时间
	public static final int U_BACKCAR_RADAR_ENABLE		= 27;	// 允许倒车显示雷达
	public static final int U_NAVI_PACKAGE				= 28;	// 导航应用包名
	public static final int U_LAMPLET_BY_TIME			= 29;	// 时间控制小灯
	// 2015/9/16 保留
	public static final int U_RESERVE					= 30;
	public static final int U_BRIGHT_LEVEL				= 31;	// 当前背关等级
	public static final int U_BRIGHT_LEVEL_DAY			= 32;	// 白天背光等级
	public static final int U_BRIGHT_LEVEL_NIGHT		= 33;	// 黑夜背关等级
	public static final int U_MCU_VER					= 34;	// MCU版本
	public static final int U_MCU_SERIAL				= 35;	// MCU序列号
	public static final int U_AUTO_BLACK_SCREEN			= 36;	// 自动黑屏 <=0:永不黑屏  >=30 自动黑屏时间(单位秒)
	// PARAM:new int[]{APP_ID, PLAYER_CMD}
	public static final int U_PLAYER_CMD				= 37;	// 播放器命令
	public static final int U_MCU_POWER_OPTION			= 38;	// MCU电源选项
	// PARAM new int[]{0不可见/1可见} new String[]{packageName}
	public static final int U_APP_VISIBILITY			= 39;	// 应用可见性(主要用于Launcher图标显示)
	// bit31~30 显示类型(0:value 1:none 2:low 3:high) 
	// bit29 单位(类型为value时有效  0:度C 1:F)
	// bit28~0 value (类型为value时有效 UI需要减1000)
	public static final int U_TEMP_OUT					= 40;	// 外部温度
	public static final int U_STEER_ANGLE				= 41;	// 方向盘角度
	public static final int U_VA_CMD					= 42;	// 转发语音助手命令
	public static final int U_ARM_SLEEP_WAKEUP			= 43;	// 0:ARM休眠 1:ARM起来
	// 2015/9/16 保留
	public static final int U_RESERVE2					= 44;
	public static final int U_TIP						= 45;	// 提示
	public static final int U_MCU_ERROR_CODE			= 46;	// MCU错误码
	public static final int U_BACKCAR_TYPE				= 47;	// 倒车类型 0:主机 1:原车
	public static final int U_TIP_MCU_UPGRADE			= 48;	// MCU升级提示
	public static final int U_ID3_TITLE					= 49;	// ID3_TITLE
	public static final int U_ACC_ON					= 50;	// ACC状态
	public static final int U_PANEL_KEY_TYPE			= 51;	// 面板按键类型
	public static final int U_LAMPLET_ON_BOOT			= 52;	// 开机时,面板灯亮
	public static final int U_LAMPLET_ON_ALWAYS			= 53;	// 面板灯一直亮
	public static final int U_LAMPLET_COLOR_CTRL		= 54;	// 面板灯颜色控制开关
	public static final int U_PANORAMA_ON				= 55;	// 全景开关
	public static final int U_FAN_CYCLE					= 56;	// 风扇转动周期 0x00:不转 0xFF:常转 其他:转动周期(单位:分钟)
	public static final int U_VA_AUDIO_OCCUPIED			= 57;	// 语音助手占用声音 new int[]{0/1}
	public static final int U_MIRROR_UP_DOWN			= 58;	// 上下镜像开关，返回的是int数组int[]{av1,av2.av2}
	public static final int U_MCU_REQUEST_VIDEO			= 59;	// 0:切倒车视频（1） 1：AUX视频（2） 2：切数字电视视频（7）；博悦众恒添加，其他客户可沿用
	public static final int U_MCU_DIEECTION_KEY			= 60;	// MCU回传的方向按键，UI自行处理
	public static final int U_RESET_ARM_LATER			= 61;	// n秒后复位ARM 注册的时候服务返回-1，无效值
	public static final int U_MCU_BOOT_ON				= 62;	// MCU第一次上电
	public static final int U_PANEL_KEY_TYPE_CNT		= 63;	// 面板按键类型总数
	public static final int U_GUESTURE					= 64;	// 手势学习及开关
	public static final int U_HOST_BACKCAR_ENABLE		= 65;	// 本地倒车开关1：本机倒车 0：原车视频倒车/原车倒车
	public static final int U_CUTACC_TURNOFF_LCDC		= 66;	// 断ACC关屏开关，默认关
	public static final int U_ECARLINK_ON				= 67;	// 亿连开关
	// int[0]:视频ID ints[1]:执行开关Camera操作,如果CAMERA已经是开或者关的状态，则不用操作
	public static final int U_REQUEST_CAMERA			= 68;	// 通知具体的UI打开/关闭CAMERA
	public static final int U_SIGNAL_ON			        = 69;	//视频信号  0：无信号   1：有信号
	
	public static final int U_PLAY_STATUS    			= 74;	// 视频播放器状态
	public static final int U_RADAR_POWER    			= 75;	// 雷达电源
	
	public static final int U_CNT_MAX					= 100;	
	
	// APP类型
	public static final int G_APP_ID					= 0;	// APP_ID
	
	// APP类型
//	public static final int MCU_STATE_LAST				= -2; 	// ?
	public static final int APP_ID_LAST					= -1;	// 上一个应用
	public static final int APP_ID_NULL					= 0;	// 无
	public static final int APP_ID_RADIO				= 1;	// 收音机
	public static final int APP_ID_BTPHONE				= 2;	// 蓝牙电话
	public static final int APP_ID_BTAV					= 3;	// 蓝牙音频
	public static final int APP_ID_DVD					= 4;	// DVD
	public static final int APP_ID_AUX					= 5;	// AUX
	public static final int APP_ID_TV					= 6;	// 电视
	public static final int APP_ID_IPOD					= 7;	// 苹果设备
	public static final int APP_ID_AUDIO_PLAYER			= 8;	// 音频播放器
	public static final int APP_ID_VIDEO_PLAYER			= 9;	// 视频播放器
	public static final int APP_ID_THIRD_PLAYER			= 10;	// 第三方播放器
	public static final int APP_ID_CAR_RADIO			= 11;	// 原车收音机
	public static final int APP_ID_CAR_BTPHONE			= 12;	// 原车蓝牙
	public static final int APP_ID_CAR_USB				= 13;	// 原车USB
	public static final int APP_ID_DVR					= 14;	// DVR
	public static final int APP_ID_CNT_MAX				= 20;
	
	public static final int BRIGHT_LEVEL_STEP_UP		= -1;	// 按步数+
	public static final int BRIGHT_LEVEL_STEP_DOWN		= -2;	// 按步数-
	public static final int BRIGHT_LEVEL_STEP_BY_SERVER	= -3;	// 服务自定义
	public static final int BRIGHT_LEVEL_UP				= -4;	// +1
	public static final int BRIGHT_LEVEL_DOWN			= -5;	// -1
	
	public static final int PLAYER_CMD_PLAY				= 0;
	public static final int PLAYER_CMD_PLAYPAUSE		= 1;
	public static final int PLAYER_CMD_PAUSE			= 2;
	public static final int PLAYER_CMD_STOP				= 3;
	public static final int PLAYER_CMD_PREV				= 4;
	public static final int PLAYER_CMD_NEXT				= 5;
	public static final int PLAYER_CMD_FF				= 6;
	public static final int PLAYER_CMD_FB				= 7;
	public static final int PLAYER_CMD_NUM				= 8;//0~9
	
	public static final int MCU_POWER_OPTION_SHUTDOWN	= 0;
	public static final int MCU_POWER_OPTION_SLEEP		= 1;
	
	public static final int TIP_NO_NAVI_SET				= 0;	// 没有设置导航应用
	public static final int TIP_KEY_POWER				= 1;	// POWER按键被按下，UI提示
	
	public static final int BACKCAR_TYPE_HOST			= 0;	// 主机倒车
	public static final int BACKCAR_TYPE_CAR			= 1;	// 原车倒车
	
	public static final int PAGE_NAVI					= 0;	// 导航
	public static final int PAGE_RECENT_TASK			= 1;	// 最近任务列表
	
	public static final int LANG_EN						= 0;	//英文
	public static final int LANG_ZH						= 1;	//简体中文
	public static final int LANG_TW						= 2;	//繁体中文
	
	public static final int VIDEO_ID_NULL				= 0;	// 关闭视频
	public static final int VIDEO_ID_BACKCAR			= 1;	// 服务使用,UI不要使用
	public static final int VIDEO_ID_AUX				= 2;
	public static final int VIDEO_ID_DVD				= 3;
	public static final int VIDEO_ID_TV_ANALOG			= 4;	// 模拟电视
	public static final int VIDEO_ID_DVR				= 5;
	public static final int VIDEO_ID_RIGHTCAMERA		= 6;
	public static final int VIDEO_ID_TV_DIGIT			= 7;	// 数字电视
	public static final int VIDEO_ID_CAR_VIDEO			= 8;	// 原车视频
	public static final int VIDEO_ID_MAX_CNT			= 10;
	
	public static final int MCU_DIEECTION_KEY_ENTER		= 0;
	public static final int MCU_DIEECTION_KEY_UP		= 1;
	public static final int MCU_DIEECTION_KEY_DOWN		= 2;
	public static final int MCU_DIEECTION_KEY_LEFT		= 3;
	public static final int MCU_DIEECTION_KEY_RIGHT		= 4;
	
}
