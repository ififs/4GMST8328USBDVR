package com.syu.dvr.activity;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.syu.dvr.R;
import com.syu.dvr.TheApp;
import com.syu.dvr.adapter.CalendarAdapter;
import com.syu.dvr.factory.LoadingOverTime;
import com.syu.dvr.factory.LoadingOverTime.LoadingCallback;
import com.syu.dvr.utils.Config;
import com.syu.dvr.utils.LogCatUtils;
import com.syu.dvr.utils.ProgressDialog;
import com.syu.dvr.widget.ComInterface.LoadAllDataCallback;

public class CalendarActivity extends BaseActivity implements LoadingCallback{
	
	
	private static final int		 BACK_PLAY	   =4;
	public  static final int	   SHOW_CALENDAR_DATA=15;
	public  static final int      LOADING_FAIL=1;
	public static  CalendarActivity context;
	private  int jumpMonth = 0;      
	private  int jumpYear = 0;       
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private String currentDate = "";
	public CalendarAdapter calV;
	public GridView calendar;
	public TextView yer_mon;
	public List<String>mFileName;
	private String mSeleTime;
	private float mTouchX;
	private float mTouchY;
	private float x;
	private float y;
	private float mStartX;
	private float mStartY;
	private LinearLayout.LayoutParams params;
	private LinearLayout mTimeCtroLayout,weekLayout;
	private TextView year;
	private String sendUriPath="";
	private boolean isOnCreate=false;
	private MyHandler mHandler;
	private ImageView btn_prev_month,btn_next_month,prn_year,next_year;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.layout_calendar);
		isOnCreate=true;
		context=this;
		registerReceivers();
		TheApp.mApp.isPlayBack=true;
		backplay(true);
		mHandler=new MyHandler(this);
		initView();
		btn_prev_month.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				jumpMonth--;    
				upDataTime();
			}
		});
		btn_next_month.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				jumpMonth++;     
				upDataTime();
			}
		});
		
		prn_year.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				jumpMonth=jumpMonth-12;    
				upDataTime();
			}
		});
		
		next_year.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				jumpMonth=jumpMonth+12;     
				upDataTime();
			}
		});
		calendar.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				Rect frame = new Rect(); 
				int statusBarHeight = frame.top; 
				x = event.getRawX(); 
				y = event.getRawY() - statusBarHeight; 
				switch (event.getAction()) {
				
				case MotionEvent.ACTION_DOWN: 
					mTouchX = event.getX(); 
					mTouchY = event.getY(); 
					mStartX = x; 
					mStartY = y; 
					
					break; 
					case MotionEvent.ACTION_MOVE: 
					break; 
					case MotionEvent.ACTION_UP: 
					if (mStartX-x>100) {
						jumpMonth++;    
						upDataTime();
					}
					if (x-mStartX>100) {
						jumpMonth--;     
						upDataTime();
					}
					break;
				}
				return false;
			}
		});
	}
	@Override
	protected void onResume() {
		super.onResume();
		jumpYear=0;
		jumpMonth=0;
		initData();
		ProgressDialog.getInstance().progressShow(R.string.str_add_file);
		LoadingOverTime.getInstance().setRecordCallback(this);
		LoadingOverTime.getInstance().RunTask();
		if (!isOnCreate||TheApp.mIsShengMaiIC) {
			new MyThread().start();
		}
		isOnCreate=false;
	}
	
	private class MyThread extends Thread{
		@Override
		public void run() {
			super.run();
			if (TheApp.mSysmanager.getExternalStorgeState()) {
				
				
				TheApp.mSysmanager.getDataSeleTime(new LoadAllDataCallback() {
					
					@Override
					public void callback(List<String> listData, List<String> mDataTime,List<String>
					mLockTime) {
						
						TheApp.mApp.setmFileNameList(listData);
						TheApp.mApp.setmDataTime(mDataTime);
						TheApp.mApp.setmLockTime(mLockTime);
						if (mHandler!=null) {
							mHandler.sendEmptyMessage(SHOW_CALENDAR_DATA);
						}
					}
				});
			}else {
				if (mHandler!=null) {
					
					mHandler.sendEmptyMessage(SHOW_CALENDAR_DATA);
				}
			}
				
		}
	}
	
	public void upDataTime(){
		calV = new CalendarAdapter(jumpMonth,jumpYear,year_c,month_c,day_c);
        calendar.setAdapter(calV);
        calV.notifyDataSetChanged();
        addTextToTopTextView(yer_mon,year);
	}
	SimpleDateFormat sdf;
	private void initData() {
		Date date = new Date();
    	 sdf= new SimpleDateFormat("yyyy-M-d");
    	currentDate = sdf.format(date); 
    	year_c = Integer.parseInt(currentDate.split("-")[0]);
    	month_c = Integer.parseInt(currentDate.split("-")[1]);
    	day_c = Integer.parseInt(currentDate.split("-")[2]);
    	calV = new CalendarAdapter(jumpMonth,jumpYear,year_c,month_c,day_c);
		addGridView();
		addTextToTopTextView(yer_mon,year);
		calendar.setAdapter(calV);
		mTimeCtroLayout.setVisibility(View.VISIBLE);
		weekLayout.setVisibility(View.VISIBLE);
		calendar.setVisibility(View.VISIBLE);
	}

	private void initView() {
		calendar=(GridView) findViewById(R.id.gridview);
		yer_mon=(TextView) findViewById(R.id.tv_month);
		mTimeCtroLayout=(LinearLayout) findViewById(R.id.time_sub_add);
		weekLayout=(LinearLayout) findViewById(R.id.week_show);
		year=(TextView) findViewById(R.id.prv_year);
		btn_prev_month=(ImageView) findViewById(R.id.btn_prev_month);
		btn_next_month=(ImageView) findViewById(R.id.btn_next_month);
		prn_year=(ImageView) findViewById(R.id.prn_year);
		next_year=(ImageView) findViewById(R.id.next_year);
	}
	private void addGridView() {
		calendar.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
					String time=calV.getShowYear()+calV.getShowMonth();
				  int startPosition = calV.getStartPositon();
				  int endPosition = calV.getEndPosition();
				  if(startPosition <= position+7  && position <= endPosition-7){
					  String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0]; 
					  if (Integer.valueOf(scheduleDay)<10) {
						time=time+"0"+scheduleDay;
					  }else {
						  time=time+scheduleDay;
					  }
				  	}
				  Message msg=new Message();
				  msg.what=BACK_PLAY;
				  msg.obj=time;
				  mHandler.sendMessage(msg);
				}
			});
		}
			
		
		public void addTextToTopTextView(TextView view,TextView year){
			StringBuffer textDate = new StringBuffer();
			textDate.append(
					calV.getShowMonth()).append(getResources().getString(R.string.str_text_month)).append("\t");
			view.setText(textDate);
			
			StringBuffer yearData=new StringBuffer();
			yearData.append(calV.getShowYear()).append(getResources().getString(R.string.str_text_year)).append("\t");
			year.setText(yearData);
			textDate=null;
			yearData=null;
		}
		
		private static class MyHandler extends Handler{
			private final WeakReference<CalendarActivity>maReference;
			public MyHandler(CalendarActivity activity){
				maReference=new WeakReference<CalendarActivity>(activity);
			}
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case BACK_PLAY:
					if (!TheApp.mSysmanager.getExternalStorgeState()) {
						
						return;
					}
					String time=(String) msg.obj;
					boolean isFileExit=TheApp.mSysmanager.isFileDataExist(time);
					if (isFileExit) {
						Bundle bundle=new Bundle();
						bundle.putString("data", time);
						if (maReference==null) {
							return;
						}
						maReference.get().startAcitivity(EditFileActivity.class, bundle);
					}else {
						TheApp.mApp.MyTosat(R.string.str_text_nodata);
					}
					break;
					
				case SHOW_CALENDAR_DATA:
					if (maReference==null) {
						return;
					}
					CalendarActivity activity=maReference.get();
					LoadingOverTime.getInstance().stopTimer();
					activity.initData();
					ProgressDialog.getInstance().progressdismiss();
					break;
					
				case LOADING_FAIL:
				if (maReference==null) {
					return;
				}
				ProgressDialog.getInstance().progressdismiss();
				TheApp.mApp.MyTosat(R.string.loading_fail);	
				break;	
				default:
					break;
				}
			}
		}
		
		public String getmSeleTime() {
			return mSeleTime;
		}
		public void setmSeleTime(String mSeleTime) {
			this.mSeleTime = mSeleTime;
		}
		@Override
		protected void onPause() {
			ProgressDialog.getInstance().progressdismiss();
			super.onPause();
		}
		@Override
		protected void onDestroy() {
			context=null;
			setContentView(R.layout.null_view);
			unregisterReceiver(receiver);
			TheApp.mApp.isPlayBack=false;
			LoadingOverTime.getInstance().remRecordCallback();
			LogCatUtils.showString(" 退出当前进程");
			System.exit(0);
			super.onDestroy();
		}
		IntentFilter filter;
		private void registerReceivers() {
			filter=new IntentFilter();
			filter.addAction("android.com.syu.dvr.action.SHURTPHOTO");
			if (!TheApp.mIsShengMaiIC) {
				filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
				filter.addAction(Intent.ACTION_MEDIA_REMOVED);
				filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
				filter.addAction(Intent.ACTION_MEDIA_CHECKING);
				filter.addDataScheme("file");
			}
			registerReceiver(receiver, filter);
			
		}
		public Intent intent;
		public BroadcastReceiver receiver=new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent intent) {
				if (intent==null) {
					return;
				}
				String action=intent.getAction();
				if (action.equals(Intent.ACTION_MEDIA_UNMOUNTED)) {
					
					if (!TheApp.mSysmanager.getExternalStorgeState()) {
						Config.setSdExitence(false);
						new MyThread().start();
					}
								
				}
			if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
				String path=intent.getData().getPath();
				TheApp.mSysmanager.setPath(path);
			
				if (TheApp.mSysmanager.getExternalStorgeState()) {
					Config.setSdExitence(true);
					sendUriPath=intent.getData().getPath();
				
					new MyThread().start();
				}
					
			}
			if (action.equals(Intent.ACTION_MEDIA_CHECKING)) {}
			}
		};
		
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			
			closeActivity();
			
			return super.onKeyDown(keyCode, event);
		}
		public void closeActivity(){
			finish();
		}

		@Override
		public void LoadCallback() {
			mHandler.sendEmptyMessage(LOADING_FAIL);
		}
		@Override
		protected void onStop() {
			super.onStop();
			
		}
		
}