/**
 * 版权：深圳深青联科技有限公司
 * 设计:	 柯华栋
 * 代码：深青联研发部/Android组
 * 日期：2015年1月1日
 */

/**
 * 应用应该自行添加垃圾清理策略
 * 例如在update中添加update计数,条件下GC
 */

package com.syu.ipc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IModuleCallback extends IInterface {
	void update(int updateCode, int[] ints, float[] flts, String[] strs) throws RemoteException;

	abstract class Stub extends Binder implements IModuleCallback {
		private static final String DESCRIPTOR = "com.syu.ipc.IModuleCallback";
		
		static final int TRANSACTION_update = (IBinder.FIRST_CALL_TRANSACTION + 0);
		
		public Stub() {
			this.attachInterface(this, DESCRIPTOR);
		}

		public static IModuleCallback asInterface(IBinder obj) {
			if ((obj == null)) {
				return null;
			}
			IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
			if (((iin != null) && (iin instanceof IModuleCallback))) {
				return ((IModuleCallback) iin);
			}
			return new IModuleCallback.Stub.Proxy(obj);
		}

		@Override
		public IBinder asBinder() {
			return this;
		}

		@Override
		public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
			switch (code) {
				case INTERFACE_TRANSACTION: {
					reply.writeString(DESCRIPTOR);
					return true;
				}
				case TRANSACTION_update: {
					data.enforceInterface(DESCRIPTOR);
					int updateCode = data.readInt();
					int[] ints = data.createIntArray();
					float[] flts = data.createFloatArray();
					String[] strs = data.createStringArray();
					this.update(updateCode, ints, flts, strs);
					return true;
				}
			}
			return super.onTransact(code, data, reply, flags);
		}

		private static class Proxy implements IModuleCallback {
			private IBinder mRemote;

			Proxy(IBinder remote) {
				mRemote = remote;
			}

			@Override
			public IBinder asBinder() {
				return mRemote;
			}

//			public String getInterfaceDescriptor() {
//				return DESCRIPTOR;
//			}

			@Override
			public void update(int updateCode, int[] ints, float[] flts, String[] strs) throws RemoteException {
				Parcel data = Parcel.obtain();
				try {
					data.writeInterfaceToken(DESCRIPTOR);
					data.writeInt(updateCode);
					data.writeIntArray(ints);
					data.writeFloatArray(flts);
					data.writeStringArray(strs);
					mRemote.transact(Stub.TRANSACTION_update, data, null, IBinder.FLAG_ONEWAY);
				} finally {
					data.recycle();
				}
			}
		}
	}
}
