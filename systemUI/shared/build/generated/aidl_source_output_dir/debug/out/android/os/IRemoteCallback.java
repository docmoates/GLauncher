/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /Users/docmoates/Library/Android/sdk/build-tools/37.0.0/aidl -p/Users/docmoates/Library/Android/sdk/platforms/android-37.1/framework.aidl -o/Users/docmoates/Lawnchair/systemUI/shared/build/generated/aidl_source_output_dir/debug/out -I/Users/docmoates/Lawnchair/systemUI/shared/src -I/Users/docmoates/Lawnchair/systemUI/shared/src/debug/aidl -I/Users/docmoates/Lawnchair/compatLib/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/Lawnchair/hidden-api/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/Lawnchair/systemUI/unfold/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/Lawnchair/systemUI/plugin/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/Lawnchair/systemUI/plugin_core/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/Lawnchair/wmshell/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/Lawnchair/systemUI/log/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/.gradle/caches/9.7.1/transforms/5678b756e7305cedafb7d368432c7498/transformed/core-1.19.0/aidl -I/Users/docmoates/.gradle/caches/9.7.1/transforms/f92a0b6208819fb7599553bffe7e9174/transformed/versionedparcelable-1.1.1/aidl -d/var/folders/lf/7k355n1d2g5_v9yg4x1hy15r0000gn/T/aidl11922990837453702874.d /Users/docmoates/Lawnchair/systemUI/shared/src/android/os/IRemoteCallback.aidl
 *
 * DO NOT CHECK THIS FILE INTO A CODE TREE (e.g. git, etc..).
 * ALWAYS GENERATE THIS FILE FROM UPDATED AIDL COMPILER
 * AS A BUILD INTERMEDIATE ONLY. THIS IS NOT SOURCE CODE.
 */
package android.os;
/** @hide */
public interface IRemoteCallback extends android.os.IInterface
{
  /** Default implementation for IRemoteCallback. */
  public static class Default implements android.os.IRemoteCallback
  {
    //    @UnsupportedAppUsage
    @Override public void sendResult(android.os.Bundle date) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements android.os.IRemoteCallback
  {
    /** Construct the stub and attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an android.os.IRemoteCallback interface,
     * generating a proxy if needed.
     */
    public static android.os.IRemoteCallback asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof android.os.IRemoteCallback))) {
        return ((android.os.IRemoteCallback)iin);
      }
      return new android.os.IRemoteCallback.Stub.Proxy(obj);
    }
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      if (code >= android.os.IBinder.FIRST_CALL_TRANSACTION && code <= android.os.IBinder.LAST_CALL_TRANSACTION) {
        data.enforceInterface(DESCRIPTOR);
      }
      switch (code)
      {
        case TRANSACTION_sendResult:
        {
          android.os.Bundle _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.os.Bundle.CREATOR);
          this.sendResult(_arg0);
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static final class Proxy implements android.os.IRemoteCallback
    {
      private android.os.IBinder mRemote;
      Proxy(android.os.IBinder remote)
      {
        mRemote = remote;
      }
      @Override public android.os.IBinder asBinder()
      {
        return mRemote;
      }
      public final java.lang.String getInterfaceDescriptor()
      {
        return DESCRIPTOR;
      }
      //    @UnsupportedAppUsage
      @Override public void sendResult(android.os.Bundle date) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, date, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_sendResult, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
    }
    /** @hide */
    public static final java.lang.String DESCRIPTOR = "android.os.IRemoteCallback";
    static final int TRANSACTION_sendResult = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
  }
  //    @UnsupportedAppUsage
  public void sendResult(android.os.Bundle date) throws android.os.RemoteException;
  /** @hide */
  static class _Parcel {
    static private <T> T readTypedObject(
        android.os.Parcel parcel,
        android.os.Parcelable.Creator<T> c) {
      if (parcel.readInt() != 0) {
          return c.createFromParcel(parcel);
      } else {
          return null;
      }
    }
    static private <T extends android.os.Parcelable> void writeTypedObject(
        android.os.Parcel parcel, T value, int parcelableFlags) {
      if (value != null) {
        parcel.writeInt(1);
        value.writeToParcel(parcel, parcelableFlags);
      } else {
        parcel.writeInt(0);
      }
    }
  }
}
