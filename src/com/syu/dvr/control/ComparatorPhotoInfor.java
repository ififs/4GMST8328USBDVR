package com.syu.dvr.control;

import java.util.Comparator;

import com.syu.dvr.module.DelePhotoInfor;


public class ComparatorPhotoInfor implements Comparator {
	@Override
	public int compare(Object arg0, Object arg1) {
		DelePhotoInfor infor1=(DelePhotoInfor) arg0;
		DelePhotoInfor infor2=(DelePhotoInfor) arg1;
		int flag=infor1.getY_m_d().compareTo(infor2.getY_m_d());
		if(flag==0){
			return infor1.getH_m_s().compareTo(infor2.getH_m_s());
		}else{
		  return flag;
		}    
	}
}
