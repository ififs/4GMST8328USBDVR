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

public interface IRemoteModule extends IInterface {
	void cmd(int cmdCode, int[] ints, float[] flts, String[] strs) throws RemoteException;
	ModuleObject get(int getCode, int[] ints, float[] flts, String[] strs) throws RemoteException;
	void register(IModuleCallback callback, int updateCode, int update) throws RemoteException;
	void unregister(IModuleCallback callback, int updateCode) throws RemoteException;
	
	abstract class Stub extends Binder implements	IRemoteModule {
		private static final String DESCRIPTOR = "com.syu.ipc.IRemoteModule";
		
		static final int TRANSACTION_cmd = (IBinder.FIRST_CALL_TRANSACTION + 0);
		static final int TRANSACTION_get = (IBinder.FIRST_CALL_TRANSACTION + 1);
		static final int TRANSACTION_register = (IBinder.FIRST_CALL_TRANSACTION + 2);
		static final int TRANSACTION_unregister = (IBinder.FIRST_CALL_TRANSACTION + 3);

		public Stub() {
			this.attachInterface(this, DESCRIPTOR);
		}

		public static IRemoteModule asInterface(IBinder obj) {
			if ((obj == null)) {
				return null;
			}
			IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
			if (((iin != null) && (iin instanceof IRemoteModule))) {
				return ((IRemoteModule) iin);
			}
			return new IRemoteModule.Stub.Proxy(obj);
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
				case TRANSACTION_cmd: {
					data.enforceInterface(DESCRIPTOR);
					int cmdCode = data.readInt();
					int[] ints = data.createIntArray();
					float[] flts = data.createFloatArray();
					String[] strs = data.createStringArray();
					this.cmd(cmdCode, ints, flts, strs);
//					reply.writeNoException();
					return true;
				}
				case TRANSACTION_get: {
					data.enforceInterface(DESCRIPTOR);
					int getCode = data.readInt();
					int[] ints = data.createIntArray();
					float[] flts = data.createFloatArray();
					String[] strs= data.createStringArray();
					ModuleObject result = this.get(getCode, ints, flts, strs);
					reply.writeNoException();
					if ((result != null)) {
						reply.writeInt(1);
						reply.writeIntArray(result.ints);
						reply.writeFloatArray(result.flts);
						reply.writeStringArray(result.strs);
					} else {
						reply.writeInt(0);
					}
					return true;
				}
				case TRANSACTION_register: {
					data.enforceInterface(DESCRIPTOR);
					IModuleCallback callback = IModuleCallback.Stub.asInterface(data.readStrongBinder());
					int updateCode = data.readInt();
					int update = data.readInt();
					this.register(callback, updateCode, update);
//					reply.writeNoException();
					return true;
				}
				case TRANSACTION_unregister: {
					data.enforceInterface(DESCRIPTOR);
					IModuleCallback callback = IModuleCallback.Stub.asInterface(data.readStrongBinder());
					int updateCode = data.readInt();
					this.unregister(callback, updateCode);
//					reply.writeNoException();
					return true;
				}
			}
			return super.onTransact(code, data, reply, flags);
		}

		private static class Proxy implements com.syu.ipc.IRemoteModule {
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
			public void cmd(int cmdCode, int[] ints, float[] flts, String[] strs) throws RemoteException {
				Parcel data = Parcel.obtain();
				Parcel reply = Parcel.obtain();
				try {
					data.writeInterfaceToken(DESCRIPTOR);
					data.writeInt(cmdCode);
					data.writeIntArray(ints);
					data.writeFloatArray(flts);
					data.writeStringArray(strs);
					mRemote.transact(Stub.TRANSACTION_cmd, data, reply, IBinder.FLAG_ONEWAY);
					reply.readException();
				} finally {
					reply.recycle();
					data.recycle();
				}
			}

			@Override
			public ModuleObject get(int getCode, int[] ints, float[] flts, String[] strs) throws RemoteException {
				Parcel data = Parcel.obtain();
				Parcel reply = Parcel.obtain();
				ModuleObject result;
				try {
					data.writeInterfaceToken(DESCRIPTOR);
					data.writeInt(getCode);
					data.writeIntArray(ints);
					data.writeFloatArray(flts);
					data.writeStringArray(strs);
					mRemote.transact(Stub.TRANSACTION_get, data, reply, 0);
					reply.readException();
					if ((0 != reply.readInt())) {
						result = new ModuleObject();
						result.ints = reply.createIntArray();
						result.flts = reply.createFloatArray();
						result.strs = reply.createStringArray();
					} else {
						result = null;
					}
				} finally {
					reply.recycle();
					data.recycle();
				}
				return result;
			}

			@Override
			public void register(IModuleCallback callback, int updateCode, int update) throws RemoteException {
				Parcel data = Parcel.obtain();
				Parcel reply = Parcel.obtain();
				try {
					data.writeInterfaceToken(DESCRIPTOR);
					data.writeStrongBinder((((callback != null)) ? (callback.asBinder()) : (null)));
					data.writeInt(updateCode);
					data.writeInt(update);
					mRemote.transact(Stub.TRANSACTION_register, data, reply, IBinder.FLAG_ONEWAY);
					reply.readException();
				} finally {
					reply.recycle();
					data.recycle();
				}
			}

			@Override
			public void unregister(IModuleCallback callback, int updateCode) throws RemoteException {
				Parcel data = Parcel.obtain();
				Parcel reply = Parcel.obtain();
				try {
					data.writeInterfaceToken(DESCRIPTOR);
					data.writeStrongBinder((((callback != null)) ? (callback.asBinder()) : (null)));
					data.writeInt(updateCode);
					mRemote.transact(Stub.TRANSACTION_unregister, data, reply, IBinder.FLAG_ONEWAY);
					reply.readException();
				} finally {
					reply.recycle();
					data.recycle();
				}
			}
		}
	}
}
