package com.syu.dvr.adapter;

import java.util.List;

import com.syu.dvr.R;
import com.syu.dvr.module.FileInfor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FolderAdapter extends BaseAdapter{
	private List<FileInfor> lists;
	private Context context;

	public FolderAdapter(Context context, List<FileInfor> lists) {
		this.lists=lists;
		this.context=context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return lists.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		ViewHolder holder;
		if (view==null) {
			holder=new ViewHolder();
			view=LayoutInflater.from(context).inflate(R.layout.layout_choose_item, null);
			holder.image=(ImageView) view.findViewById(R.id.image_thumb);
			holder.mText=(TextView) view.findViewById(R.id.text_name);
			view.setTag(holder);
		}else {
			holder=(ViewHolder) view.getTag();
		}
		FileInfor infor=lists.get(arg0);
		holder.mText.setText(infor.getFileName());
		if (infor.isRoot()) {
			holder.image.setBackgroundResource(R.drawable.icon_devicer);
		}else {
			holder.image.setBackgroundResource(R.drawable.icon_folder);
		}
		return view;
	}
	class ViewHolder{
		ImageView image;
		TextView mText;
	}
}
