package com.syu.dvr.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.syu.dvr.R;
import com.syu.dvr.TheApp;
import com.syu.dvr.module.MediaInfor;
import com.syu.dvr.utils.GridViewUtils;
import com.syu.dvr.utils.ImageLoader;
import com.syu.dvr.utils.PublicClass;
import com.syu.dvr.widget.MGridView;

public class VFileAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context mContext;
	public  boolean  isLocked = false;
	private List<String>mTimeList;
	private boolean isOnscroll=false;
	private boolean isOnscllorlling=false;
	private boolean isDele=false;
	
	public boolean isDele() {
		return isDele;
	}

	public void setDele(boolean isDele) {
		this.isDele = isDele;
	}

	public boolean isOnscroll() {
		return isOnscroll;
	}

	public void setOnscroll(boolean isOnscroll) {
		this.isOnscroll = isOnscroll;
	}

	public boolean isOnscllorlling() {
		return isOnscllorlling;
	}

	public void setOnscllorlling(boolean isOnscllorlling) {
		this.isOnscllorlling = isOnscllorlling;
	}
	private List<List<MediaInfor>>mdataList;
	private List<GridViewAdapter.ViewHolder>mhHolder;
	public VFileAdapter(Context context ,
			List<List<MediaInfor>> datalist, List<String> mTimeList){
		this.mContext = context ;
		mInflater = LayoutInflater.from(mContext);
		this.mdataList = datalist;
		this.mTimeList=mTimeList;
		mhHolder=new ArrayList<GridViewAdapter.ViewHolder>();
		TheApp.setmHolders(mhHolder);
	}

	@Override
	public int getCount() {
		
		return mdataList.size();
	}

	@Override
	public Object getItem(int position) {
		
		return mdataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		return handlerVideo(position, convertView, parent);
	}
	
	class ViewHolder{
		TextView mText ;
		MGridView mGridView;
		GridViewAdapter adapter;
		public ViewHolder(View view){
			mGridView=(MGridView) view.findViewById(R.id.layout_gridview);
			adapter=new GridViewAdapter();
			mGridView.setAdapter(adapter);
			view.setTag(this);
		}
	}
	private List<MediaInfor>VideoList;
	public View handlerVideo(final int position, View convertView, ViewGroup parent){
		final ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_grid, null);
			holder = new ViewHolder(convertView);
			holder.mText=(TextView) convertView.findViewById(R.id.id_time);
			convertView.setTag(holder);
		}else{
			
			holder = (ViewHolder) convertView.getTag();
		}
		VideoList=mdataList.get(position);
		if (mdataList.get(position)!=null&&mdataList.get(position).size()>0){
		holder.adapter.setVideoList(VideoList);
		holder.adapter.notifyDataSetChanged();
		String tString=Integer.valueOf(mTimeList.get(position))+TheApp.mApp.getResources().getString(R.string.str_text_point);
		holder.mText.setText(tString);
		}
		if (PublicClass.getInstance().isVerticalScreen()||PublicClass.getInstance().getWindowManeger()[0]<=800) {
			GridViewUtils.updateGridViewLayoutParams(holder.mGridView, 3);
		}else {
			GridViewUtils.updateGridViewLayoutParams(holder.mGridView, 4);
		}
		
		return convertView;
	}
	
}
