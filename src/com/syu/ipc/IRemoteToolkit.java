/**
 * 版权：深圳深青联科技有限公司
 * 设计:	 柯华栋
 * 代码：深青联研发部/Android组
 * 日期：2015年1月1日
 */

package com.syu.ipc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IRemoteToolkit extends IInterface {
	
	IRemoteModule getRemoteModule(int moduleCode) throws RemoteException;
	
	abstract class Stub extends Binder implements	IRemoteToolkit {
		private static final java.lang.String DESCRIPTOR = "com.syu.ipc.IRemoteToolkit";
		
		static final int TRANSACTION_getRemoteModule = (IBinder.FIRST_CALL_TRANSACTION + 0);

		public Stub() {
			this.attachInterface(this, DESCRIPTOR);
		}

		public static IRemoteToolkit asInterface(IBinder obj) {
			if ((obj == null)) {
				return null;
			}
			IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
			if (((iin != null) && (iin instanceof IRemoteToolkit))) {
				return ((IRemoteToolkit) iin);
			}
			return new IRemoteToolkit.Stub.Proxy(obj);
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
				case TRANSACTION_getRemoteModule: {
					data.enforceInterface(DESCRIPTOR);
					int moduleCode = data.readInt();
					IRemoteModule result = this.getRemoteModule(moduleCode);
					reply.writeNoException();
					reply.writeStrongBinder((((result != null)) ? (result.asBinder()) : (null)));
					return true;
				}
			}
			return super.onTransact(code, data, reply, flags);
		}

		private static class Proxy implements IRemoteToolkit {
			private IBinder mRemote;

			Proxy(IBinder remote) {
				mRemote = remote;
			}

			@Override
			public IBinder asBinder() {
				return mRemote;
			}

//			public java.lang.String getInterfaceDescriptor() {
//				return DESCRIPTOR;
//			}

			@Override
			public IRemoteModule getRemoteModule(int moduleCode) throws RemoteException {
				Parcel data = Parcel.obtain();
				Parcel reply = Parcel.obtain();
				IRemoteModule result;
				try {
					data.writeInterfaceToken(DESCRIPTOR);
					data.writeInt(moduleCode);
					mRemote.transact(Stub.TRANSACTION_getRemoteModule, data, reply, 0);
					reply.readException();
					result = IRemoteModule.Stub.asInterface(reply.readStrongBinder());
				} finally {
					reply.recycle();
					data.recycle();
				}
				return result;
			}
		}		
	}
}
