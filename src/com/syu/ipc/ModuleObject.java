/**
 * 版权：深圳深青联科技有限公司
 * 设计:	 柯华栋
 * 代码：深青联研发部/Android组
 * 日期：2015年1月1日
 */

package com.syu.ipc;

public class ModuleObject {
	public int[] 	ints;
	public float[]	flts;
	public String[] strs;
	
	public ModuleObject() {}
	
	public ModuleObject(int value) {
		this.ints = new int[] {value};
	}
	
	public ModuleObject(int value, String strValue) {
		this.ints = new int[] {value};
		this.strs = new String[] {strValue};
	}
	
	public ModuleObject(int[] ints) {
		this.ints = ints;
	}
	
	public ModuleObject(String value) {
		this.strs = new String[] {value};
	}
	
	/**
	 * 方便使用
	 */
	public static boolean checkInts(ModuleObject obj, int min) {
		return obj != null && obj.ints != null && obj.ints.length >= min;
	}
	
	/**
	 * 方便使用
	 */
	public static int get(ModuleObject obj, int valueIfNotOk) {
		if (obj != null && obj.ints != null && obj.ints.length >= 1) {
			return obj.ints[0];
		}
		return valueIfNotOk;
	}
	
	public static String get(ModuleObject obj, String valueIfNotOk) {
		if (obj != null && obj.strs != null && obj.strs.length >= 1) {
			return obj.strs[0];
		}
		return valueIfNotOk;
	}
	
	/**
	 * 方便使用
	 */
	public static int get(RemoteModuleProxy proxy, int getCode, int valueIfNotOk) {
		ModuleObject obj = proxy.get(getCode, null, null, null);
		if (obj != null && obj.ints != null && obj.ints.length >= 1) {
			return obj.ints[0];
		}
		return valueIfNotOk;
	}
}
