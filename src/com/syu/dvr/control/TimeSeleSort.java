package com.syu.dvr.control;

import java.util.Comparator;

public class TimeSeleSort implements Comparator{

	@Override
	public int compare(Object lhs, Object rhs) {
		String time1=(String) lhs;
		String time2=(String) rhs;
		return time1.compareTo(time2);
	}

}
