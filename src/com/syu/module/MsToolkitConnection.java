/**
 * 版权：深圳深青联科技有限公司
 * 设计:	 柯华栋
 * 代码：深青联研发部/Android组
 * 日期：2015年1月1日
 */

package com.syu.module;

import java.util.ArrayList;
import java.util.Random;

import com.syu.ipc.IRemoteToolkit;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;

public class MsToolkitConnection implements ServiceConnection {
	
	private static final MsToolkitConnection INSTANCE = new MsToolkitConnection();
	
	public static MsToolkitConnection getInstance() {
		return INSTANCE;
	}
	
	
	private IRemoteToolkit mRemoteToolkit;
	private Context mContext;
	private boolean mConnecting;
	private Handler mHandler = new Handler(Looper.getMainLooper());
	
	private ArrayList<ConnectionObserver> mConnectionObservers = new ArrayList<ConnectionObserver>();
	
	private MsToolkitConnection() {}
	
	public IRemoteToolkit getRemoteToolkit() {
		return mRemoteToolkit;
	}
	
	
	
	
	/**
	 * 调用后立刻连接
	 */
	public void connect(Context context) {
		connect(context, 0);
	}
	
	static Looper looper;
	static {
		HandlerThread thread = new HandlerThread("ConnectionThread");
		thread.start();
		looper = thread.getLooper();
	}
	
	/**
	 * 同步mConnecting mRemoteToolkit 
	 */
	private synchronized void connect(Context context, long delayMillis) {
		if (mConnecting || mRemoteToolkit != null || context == null) return;
		mContext = context.getApplicationContext();
		mConnecting = true;
		mHandler.post(mRunnableConnect);
	}
	
	//链接服务
	private Runnable mRunnableConnect = new Runnable() {
		@Override
		public void run() {
			final Intent mIntent = new Intent();
//			mIntent.setAction("com.syu.ms.toolkit");
//			mIntent.setPackage("com.syu.ms");
			
			mIntent.setClassName("com.syu.ms", "app.ToolkitService");
			
			//com.syu.ms.Toolkit
			
			if (mRemoteToolkit == null) {
				mContext.bindService(mIntent, INSTANCE, Context.BIND_AUTO_CREATE);
				mHandler.postDelayed(this, 1000 + new Random().nextInt(3000));
			} else {
				mConnecting = false;
			}
		}
	};
	
	public synchronized void addObserver(ConnectionObserver observer) {
		if (observer == null || mConnectionObservers.contains(observer))
			return;
		mConnectionObservers.add(observer);
		
		if (mRemoteToolkit != null) {
			mHandler.post(new OnServiceConnected(observer));
		}
	}
	
	public synchronized void removeObserver(ConnectionObserver observer) {
		if (observer != null)
			mConnectionObservers.remove(observer);
		if (mRemoteToolkit != null) {
			mHandler.post(new OnServiceDisconnected(observer));
		}
	}
	
	public synchronized void clearObservers() {
		if (mRemoteToolkit != null) {
			for (ConnectionObserver observer : mConnectionObservers) {
				mHandler.post(new OnServiceDisconnected(observer));
			}
		}
		mConnectionObservers.clear();
	}
	
	
	@Override
	public synchronized void onServiceConnected(ComponentName name, IBinder service) {
		mRemoteToolkit = IRemoteToolkit.Stub.asInterface(service);
		
//		try {
//			DataMoudle.PORXY_CUSTOMER.setRemoteModule(mRemoteToolkit.getRemoteModule(11));
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}
		
		for (ConnectionObserver observer : mConnectionObservers) {
			mHandler.post(new OnServiceConnected(observer));
		}
		
	}

	@Override
	public synchronized void onServiceDisconnected(ComponentName name) {
		mRemoteToolkit = null;
		
//		DataMoudle.PORXY_CUSTOMER.setRemoteModule(null);
		
		for (ConnectionObserver observer : mConnectionObservers) {
			mHandler.post(new OnServiceDisconnected(observer));
		}
		connect(mContext, 1000 + new Random().nextInt(3000));
	}
	
	private class OnServiceConnected implements Runnable {
		private ConnectionObserver observer;
		
		private OnServiceConnected(ConnectionObserver observer) {
			this.observer = observer;
		}
		
		@Override
		public void run() {
			IRemoteToolkit toolkit = mRemoteToolkit;
			if (toolkit != null && observer != null) {
				observer.onConnected(toolkit);
			}
		}
	}
	
	private class OnServiceDisconnected implements Runnable {
		private ConnectionObserver observer;
		
		private OnServiceDisconnected(ConnectionObserver observer) {
			this.observer = observer;
		}
		
		@Override
		public void run() {
			if (observer != null) {
				observer.onDisconnected();
			}
		}
	}
}
