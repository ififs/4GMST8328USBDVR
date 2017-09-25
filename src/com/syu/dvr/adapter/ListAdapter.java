package com.syu.dvr.adapter;

import java.util.List;

import com.syu.dvr.R;
import com.syu.dvr.module.SettingInfor;
import com.syu.dvr.utils.LogCatUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;

public class ListAdapter extends BaseAdapter {
    private final String TAG = "CAM_ListAdapter";

    private Context mContext;
    private LayoutInflater mInflater;
    private List<SettingInfor> mLists;

    public ListAdapter(Context context,List<SettingInfor> mLists) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mLists=mLists;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mLists.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = mInflater.inflate(R.layout.item_set, null);
        RadioButton button = (RadioButton) view.findViewById(R.id.radio_button);
        ImageView imageView = (ImageView) view.findViewById(R.id.item_set_img);
        SettingInfor item = mLists.get(position);
        
        button.setText(item.getLabelRes());
        button.setChecked(item.isChecked());
        imageView.setImageResource(item.getDrawableRes());
     
        return view;
    }

}
