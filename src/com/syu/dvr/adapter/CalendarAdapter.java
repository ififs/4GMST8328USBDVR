package com.syu.dvr.adapter;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.syu.dvr.R;
import com.syu.dvr.TheApp;
import com.syu.dvr.control.SpecialCalendar;
import com.syu.dvr.utils.LogCatUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class CalendarAdapter extends BaseAdapter {
	private boolean isLeapyear = false;  
	private int daysOfMonth = 0;     
	private int dayOfWeek = 0;        
	private int lastDaysOfMonth = 0;  
	private Context context;
	private String[] dayNumber = new String[42];  
	private SpecialCalendar sc = null;
	private String currentYear = "";
	private String currentMonth = "";
	private String currentDay = "";
	
	@SuppressLint("SimpleDateFormat") 
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
	private int currentFlag = -1;     
	private int[] schDateTagFlag = null;  
	
	private String showYear = "";   
	private String showMonth = "";  
	private String animalsYear = ""; 
	private String leapMonth = "";   
	private String cyclical = "";   
	
	private String sysDate = "";  
	private String sys_year = "";
	private String sys_month = "";
	private String sys_day = "";
	private List<String>mDataTime=new ArrayList<String>();
	private List<String>mLockTiem=new ArrayList<String>();
	public CalendarAdapter(){
		Date date = new Date();
		sysDate = sdf.format(date);
		sys_year = sysDate.split("-")[0];
		sys_month = sysDate.split("-")[1];
		sys_day = sysDate.split("-")[2];
	}
	
	public CalendarAdapter(int jumpMonth,int jumpYear,int year_c,int month_c,int day_c){
		this();
		this.mDataTime=TheApp.mApp.getmDataTime();
		this.mLockTiem=TheApp.mApp.getmLockTime();
		this.context= TheApp.mApp;
		sc = new SpecialCalendar();
		int stepYear = year_c+jumpYear;
		int stepMonth = month_c+jumpMonth ;
		if(stepMonth > 0){
			if(stepMonth%12 == 0){
				stepYear = year_c + stepMonth/12 -1;
				stepMonth = 12;
			}else{
				stepYear = year_c + stepMonth/12;
				stepMonth = stepMonth%12;
			}
		}else{
			stepYear = year_c - 1 + stepMonth/12;
			stepMonth = stepMonth%12 + 12;
			if(stepMonth%12 == 0){
				
			}
		}
		currentYear = String.valueOf(stepYear);
		currentMonth = String.valueOf(stepMonth);  
		currentDay = String.valueOf(day_c); 
		getCalendar(Integer.parseInt(currentYear),Integer.parseInt(currentMonth));
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dayNumber.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	SpannableString sp ;
	String d;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 ViewHolder holder;
		 String mFilePath;
		 String time;
		if(convertView == null){
			holder=new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.calendar_item, null);
			holder.mText= (TextView) convertView.findViewById(R.id.tvtext);
			holder.mView=convertView.findViewById(R.id.mark_cricl);
			holder.mLock=convertView.findViewById(R.id.mark_lock);
			convertView.setTag(holder);
		 }else {
			holder=(ViewHolder) convertView.getTag();
		}
		d="";
		if (Integer.valueOf(dayNumber[position])<10) {
			d="0"+dayNumber[position];
		}else {
			d=dayNumber[position];
		}
		sp= new SpannableString(d);
		sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, d.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new RelativeSizeSpan(1.2f) , 0, d.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		holder.mText.setText(sp);
		holder.mText.setTextColor(Color.GRAY);		
		if (position < daysOfMonth + dayOfWeek && position >= dayOfWeek) {
			holder.mText.setTextColor(Color.BLACK);// 当月字体设黑
			
			if (TheApp.mSysmanager.getExternalStorgeState()) {
				
				if (mDataTime.contains(getShowYear()+showMonth+d)) {
					
					holder.mView.setBackgroundResource(R.drawable.min_cricl);
					if (mLockTiem.contains(getShowYear()+showMonth+d)) {
						holder.mLock.setBackgroundResource(R.drawable.mark_lock);
					}
				
				}
				
			}
		}
		if(currentFlag == position){ 
			holder.mText.setBackgroundResource(R.drawable.big_cricl);
			holder.mText.setTextColor(Color.WHITE);
		}
		
		return convertView;
	}
	
	public void getCalendar(int year, int month){
		isLeapyear = sc.isLeapYear(year);              
		daysOfMonth = sc.getDaysOfMonth(isLeapyear, month);  
		dayOfWeek = sc.getWeekdayOfMonth(year, month);      
		lastDaysOfMonth = sc.getDaysOfMonth(isLeapyear, month-1); 
		getweek(year,month);
	}
	
	private void getweek(int year, int month) {
		int j = 1;
		int flag = 0;
		for (int i = 0; i < dayNumber.length; i++) {
			 if(i < dayOfWeek){  
				int temp = lastDaysOfMonth - dayOfWeek+1;
				dayNumber[i] = (temp + i)+"";
				
			}else if(i < daysOfMonth + dayOfWeek){   
				String day = String.valueOf(i-dayOfWeek+1);   
				dayNumber[i] = i-dayOfWeek+1+"";
				
				if(sys_year.equals(String.valueOf(year)) && sys_month.equals(String.valueOf(month)) && sys_day.equals(day)){
					
					currentFlag = i;
				}	
				if (month<10) {
					
				}
				setShowYear(String.valueOf(year));
				setShowMonth(String.valueOf(month));
				
			}else{   
				
				dayNumber[i] = j+"";
				j++;
			}
		}
        
        String abc = "";
        for(int i = 0; i < dayNumber.length; i++){
        	 abc = abc+dayNumber[i]+":";
        }


	}

	
	public String getDateByClickItem(int position){
		return dayNumber[position];
	}
	
	
	public int getStartPositon(){
		return dayOfWeek+7;
	}
	
	
	public int getEndPosition(){
		return  (dayOfWeek+daysOfMonth+7)-1;
	}
	
	public String getShowYear() {
		return showYear;
	}

	public void setShowYear(String showYear) {
		this.showYear = showYear;
	}

	public String getShowMonth() {
		return showMonth;
	}

	public void setShowMonth(String showMonth) {
		if (Integer.parseInt(showMonth)<10) {
			this.showMonth="0"+showMonth;
		}else {
			this.showMonth = showMonth;
		}
		
	}
	
	public String getAnimalsYear() {
		return animalsYear;
	}

	public void setAnimalsYear(String animalsYear) {
		this.animalsYear = animalsYear;
	}
	
	public String getLeapMonth() {
		return leapMonth;
	}

	public void setLeapMonth(String leapMonth) {
		this.leapMonth = leapMonth;
	}
	
	public String getCyclical() {
		return cyclical;
	}

	public void setCyclical(String cyclical) {
		this.cyclical = cyclical;
	}
	class ViewHolder{
		View mView;
		TextView mText;
		View mLock;
	}
}
