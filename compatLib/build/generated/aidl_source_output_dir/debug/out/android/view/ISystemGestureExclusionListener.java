/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /Users/docmoates/Library/Android/sdk/build-tools/37.0.0/aidl -p/Users/docmoates/Library/Android/sdk/platforms/android-37.1/framework.aidl -o/Users/docmoates/Lawnchair/compatLib/build/generated/aidl_source_output_dir/debug/out -I/Users/docmoates/Lawnchair/compatLib/src/main/java -I/Users/docmoates/Lawnchair/compatLib/src/debug/aidl -I/Users/docmoates/.gradle/caches/9.7.1/transforms/5678b756e7305cedafb7d368432c7498/transformed/core-1.19.0/aidl -I/Users/docmoates/.gradle/caches/9.7.1/transforms/f92a0b6208819fb7599553bffe7e9174/transformed/versionedparcelable-1.1.1/aidl -d/var/folders/lf/7k355n1d2g5_v9yg4x1hy15r0000gn/T/aidl17733124704898639463.d /Users/docmoates/Lawnchair/compatLib/src/main/java/android/view/ISystemGestureExclusionListener.aidl
 *
 * DO NOT CHECK THIS FILE INTO A CODE TREE (e.g. git, etc..).
 * ALWAYS GENERATE THIS FILE FROM UPDATED AIDL COMPILER
 * AS A BUILD INTERMEDIATE ONLY. THIS IS NOT SOURCE CODE.
 */
package android.view;
/**
 * Listener for changes to the system gesture exclusion region
 * 
 * {@hide}
 */
public interface ISystemGestureExclusionListener extends android.os.IInterface
{
  /** Default implementation for ISystemGestureExclusionListener. */
  public static class Default implements android.view.ISystemGestureExclusionListener
  {
    /**
     * Called when the system gesture exclusion for the given display changed.
     * @param displayId the display whose system gesture exclusion changed
     * @param systemGestureExclusion a {@code Region} where the app would like priority over the
     *                               system gestures, in display coordinates. Certain restrictions
     *                               might be applied such that apps don't get all the exclusions
     *                               they request.
     * @param systemGestureExclusionUnrestricted a {@code Region} where the app would like priority
     *                               over the system gestures, in display coordinates, without
     *                               any restrictions applied. Null if no restrictions have been
     *                               applied.
     */
    @Override public void onSystemGestureExclusionChanged(int displayId, android.graphics.Region systemGestureExclusion, android.graphics.Region systemGestureExclusionUnrestricted) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements android.view.ISystemGestureExclusionListener
  {
    /** Construct the stub and attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an android.view.ISystemGestureExclusionListener interface,
     * generating a proxy if needed.
     */
    public static android.view.ISystemGestureExclusionListener asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof android.view.ISystemGestureExclusionListener))) {
        return ((android.view.ISystemGestureExclusionListener)iin);
      }
      return new android.view.ISystemGestureExclusionListener.Stub.Proxy(obj);
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
        case TRANSACTION_onSystemGestureExclusionChanged:
        {
          int _arg0;
          _arg0 = data.readInt();
          android.graphics.Region _arg1;
          _arg1 = _Parcel.readTypedObject(data, android.graphics.Region.CREATOR);
          android.graphics.Region _arg2;
          _arg2 = _Parcel.readTypedObject(data, android.graphics.Region.CREATOR);
          this.onSystemGestureExclusionChanged(_arg0, _arg1, _arg2);
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static final class Proxy implements android.view.ISystemGestureExclusionListener
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
      /**
       * Called when the system gesture exclusion for the given display changed.
       * @param displayId the display whose system gesture exclusion changed
       * @param systemGestureExclusion a {@code Region} where the app would like priority over the
       *                               system gestures, in display coordinates. Certain restrictions
       *                               might be applied such that apps don't get all the exclusions
       *                               they request.
       * @param systemGestureExclusionUnrestricted a {@code Region} where the app would like priority
       *                               over the system gestures, in display coordinates, without
       *                               any restrictions applied. Null if no restrictions have been
       *                               applied.
       */
      @Override public void onSystemGestureExclusionChanged(int displayId, android.graphics.Region systemGestureExclusion, android.graphics.Region systemGestureExclusionUnrestricted) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(displayId);
          _Parcel.writeTypedObject(_data, systemGestureExclusion, 0);
          _Parcel.writeTypedObject(_data, systemGestureExclusionUnrestricted, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onSystemGestureExclusionChanged, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
    }
    static final int TRANSACTION_onSystemGestureExclusionChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "android.view.ISystemGestureExclusionListener";
  /**
   * Called when the system gesture exclusion for the given display changed.
   * @param displayId the display whose system gesture exclusion changed
   * @param systemGestureExclusion a {@code Region} where the app would like priority over the
   *                               system gestures, in display coordinates. Certain restrictions
   *                               might be applied such that apps don't get all the exclusions
   *                               they request.
   * @param systemGestureExclusionUnrestricted a {@code Region} where the app would like priority
   *                               over the system gestures, in display coordinates, without
   *                               any restrictions applied. Null if no restrictions have been
   *                               applied.
   */
  public void onSystemGestureExclusionChanged(int displayId, android.graphics.Region systemGestureExclusion, android.graphics.Region systemGestureExclusionUnrestricted) throws android.os.RemoteException;
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
