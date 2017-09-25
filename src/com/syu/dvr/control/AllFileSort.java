package com.syu.dvr.control;

import java.util.Comparator;

import com.syu.dvr.module.MediaInfor;


public class AllFileSort implements Comparator {
	@Override
	public int compare(Object arg0, Object arg1) {
		MediaInfor infor1=(MediaInfor) arg0;
		MediaInfor infor2=(MediaInfor) arg1;
		return infor1.getH_m_s().compareTo(infor2.getH_m_s());
		
		
	}
}
