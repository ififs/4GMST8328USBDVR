package com.syu.dvr.widget;

import java.util.ArrayList;
import java.util.List;

import com.syu.dvr.R;
import com.syu.dvr.TheApp;
import com.syu.dvr.utils.PublicClass;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class NoCameraCarousel extends Dialog{
	private View view;
	private ViewPager mViewPager;
	private List<ImageView>data;
	private LinearLayout mLayout;
	private View[] mViews;
	private ImageView imageView;
	private LinearLayout.LayoutParams params;
	private int [] souceid={R.drawable.image1,R.drawable.image2};
	private Context mContext;
	public NoCameraCarousel(Context context, int theme) {
		super(context, theme);
		this.mContext=context;
		ViewGroup.LayoutParams params= new ViewGroup.LayoutParams(668,433);
		view =LinearLayout.inflate(TheApp.mApp, R.layout.carousel_layout, null);
		getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(view,params);
		initView();
		setPageSelect(0);
	}
	private void initData() {
		if (mLayout==null) {
			return;
		}
		mViews=new View[souceid.length];
		params=new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(8,8));
		for (int i = 0; i < souceid.length; i++) {
			View view=new View(mContext);
			imageView =new ImageView(mContext);
			imageView.setBackgroundResource(souceid[i]);
			data.add(imageView);
			mViews[i]=view;
			params.rightMargin=10;
			params.leftMargin=10;
			mLayout.addView(view,params);
		}
		mViewPager.setAdapter(new MyPagerAdapter());
	}
	private void initView(){
		mViewPager=(ViewPager) findViewById(R.id.carousel_viewpager);
		data=new ArrayList<ImageView>();
		mViewPager.addOnPageChangeListener(new MyOnPageChangeListener());
		mLayout=(LinearLayout) findViewById(R.id.view_layout);
		initData();
	}
	class MyOnPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
		
			setPageSelect(arg0);
		}
	}
	private void setPageSelect(int id){
		mViewPager.setCurrentItem(id);
		for(int i=0;i<mViews.length;i++){
			if (id==i) {
				mViews[i].setBackgroundResource(R.drawable.view);
			}else {
				mViews[i].setBackgroundResource(R.drawable.view_p);
			}
		}
	}
	class MyPagerAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0==arg1;
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			container.addView(data.get(position));
			return data.get(position);
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView(data.get(position));
		}
	}
}
