package com.syu.dvr.adapter;

import java.util.List;

import com.syu.dvr.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class SetAdapter extends BaseAdapter{
	public List<Integer>value;
	private LayoutInflater mInflater;
	public int mSelect=0;
	
	public void changeSelected(int postion){
		if (postion!=mSelect) {
			mSelect=postion;
			notifyDataSetChanged();
		}
	}
	 
	public SetAdapter(List<Integer>value,Context context){
		 mInflater = LayoutInflater.from(context);
		this.value=value;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return value.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return value.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		 View view = mInflater.inflate(R.layout.item_image, null);
		 ImageView imageView = (ImageView) view.findViewById(R.id.list_set_select_view);
		 imageView.setImageResource(value.get(arg0));
		 LinearLayout.LayoutParams params=(LayoutParams) imageView.getLayoutParams();
		 if (mSelect==arg0) {
			params.width=150;
			params.height=160;
		}else {
			params.width=100;
			params.height=105;
		}
		 imageView.setLayoutParams(params);
		return view;
	}

}
