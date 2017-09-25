package com.syu.dvr.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.syu.dvr.R;
import com.syu.dvr.TheApp;
import com.syu.dvr.control.TimeSeleSort;
import com.syu.dvr.utils.LogCatUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FileSeleTimeAdapter extends BaseAdapter{
	private List<String>mDataTime=new ArrayList<String>();
	private LayoutInflater inflater;
	private String mseletime;
	@SuppressWarnings("unchecked")
	public FileSeleTimeAdapter(String mSeletime) {
		this.mseletime=mSeletime;
		this.mDataTime=TheApp.mApp.getmDataTime();
		TimeSeleSort seleSort=new TimeSeleSort();
		Collections.sort(mDataTime,seleSort);
		inflater=LayoutInflater.from(TheApp.mApp);
	}

	@Override
	public int getCount() {
		return mDataTime.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDataTime.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView==null) {
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.popwindowitem, null);
			holder.year=(TextView) convertView.findViewById(R.id.tv_year);
			holder.month=(TextView) convertView.findViewById(R.id.tv_month);
			holder.day=(TextView) convertView.findViewById(R.id.tv_data);
			holder.layout=(LinearLayout) convertView.findViewById(R.id.show_time);
			convertView.setTag(holder);
		}else {
			holder=(ViewHolder) convertView.getTag();
		}
		String time=mDataTime.get(position);
		holder.year.setText(time.subSequence(0, 4));
		holder.month.setText(time.subSequence(4, 6));
		holder.day.setText(time.subSequence(6, 8));
		if (time.contains(mseletime)) {
			holder.layout.setBackgroundColor(TheApp.mApp.getResources().getColor(R.color.moccasin));
		}
		
		return convertView;
	}
	
	class ViewHolder{
		TextView year;
		TextView month;
		TextView day;
		LinearLayout layout;
	}
}
