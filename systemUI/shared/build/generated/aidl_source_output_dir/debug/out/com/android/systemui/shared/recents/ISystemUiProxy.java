/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /Users/docmoates/Library/Android/sdk/build-tools/37.0.0/aidl -p/Users/docmoates/Library/Android/sdk/platforms/android-37.1/framework.aidl -o/Users/docmoates/Lawnchair/systemUI/shared/build/generated/aidl_source_output_dir/debug/out -I/Users/docmoates/Lawnchair/systemUI/shared/src -I/Users/docmoates/Lawnchair/systemUI/shared/src/debug/aidl -I/Users/docmoates/Lawnchair/compatLib/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/Lawnchair/hidden-api/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/Lawnchair/systemUI/unfold/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/Lawnchair/systemUI/plugin/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/Lawnchair/systemUI/plugin_core/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/Lawnchair/wmshell/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/Lawnchair/systemUI/log/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/.gradle/caches/9.7.1/transforms/5678b756e7305cedafb7d368432c7498/transformed/core-1.19.0/aidl -I/Users/docmoates/.gradle/caches/9.7.1/transforms/f92a0b6208819fb7599553bffe7e9174/transformed/versionedparcelable-1.1.1/aidl -d/var/folders/lf/7k355n1d2g5_v9yg4x1hy15r0000gn/T/aidl16122469569322963455.d /Users/docmoates/Lawnchair/systemUI/shared/src/com/android/systemui/shared/recents/ISystemUiProxy.aidl
 *
 * DO NOT CHECK THIS FILE INTO A CODE TREE (e.g. git, etc..).
 * ALWAYS GENERATE THIS FILE FROM UPDATED AIDL COMPILER
 * AS A BUILD INTERMEDIATE ONLY. THIS IS NOT SOURCE CODE.
 */
package com.android.systemui.shared.recents;
/** Temporary callbacks into SystemUI. */
public interface ISystemUiProxy extends android.os.IInterface
{
  /** Default implementation for ISystemUiProxy. */
  public static class Default implements com.android.systemui.shared.recents.ISystemUiProxy
  {
    /** Begins screen pinning on the provided {@param taskId}. */
    @Override public void startScreenPinning(int taskId) throws android.os.RemoteException
    {
    }
    /** Notifies SystemUI that Overview is shown. */
    @Override public void onOverviewShown(boolean fromHome) throws android.os.RemoteException
    {
    }
    /**
     * Proxies motion events from the homescreen UI to the status bar. Only called when
     * swipe down is detected on WORKSPACE. The sender guarantees the following order of events on
     * the tracking pointer.
     * 
     * Normal gesture: DOWN, MOVE/POINTER_DOWN/POINTER_UP)*, UP or CANCLE
     */
    @Override public void onStatusBarTouchEvent(android.view.MotionEvent event) throws android.os.RemoteException
    {
    }
    /** Proxies the assistant gesture's progress started from navigation bar. */
    @Override public void onAssistantProgress(float progress) throws android.os.RemoteException
    {
    }
    /**
     * Proxies the assistant gesture fling velocity (in pixels per millisecond) upon completion.
     * Velocity is 0 for drag gestures.
     */
    @Override public void onAssistantGestureCompletion(float velocity) throws android.os.RemoteException
    {
    }
    /** Start the assistant. */
    @Override public void startAssistant(android.os.Bundle bundle) throws android.os.RemoteException
    {
    }
    /**
     * Indicates that the given Assist invocation types should be handled by Launcher via
     * LauncherProxy#onAssistantOverrideInvoked and should not be invoked by SystemUI.
     * 
     * @param invocationTypes The invocation types that will henceforth be handled via
     *         LauncherProxy (Launcher); other invocation types should be handled by SysUI.
     */
    @Override public void setAssistantOverridesRequested(int[] invocationTypes) throws android.os.RemoteException
    {
    }
    /** Notifies that the accessibility button in the system's navigation area has been clicked */
    @Override public void notifyAccessibilityButtonClicked(int displayId) throws android.os.RemoteException
    {
    }
    /** Notifies that the accessibility button in the system's navigation area has been long clicked */
    @Override public void notifyAccessibilityButtonLongClicked() throws android.os.RemoteException
    {
    }
    /** Ends the system screen pinning. */
    @Override public void stopScreenPinning() throws android.os.RemoteException
    {
    }
    /**
     * Notifies that quickstep will switch to a new task
     * @param rotation indicates which Surface.Rotation the gesture was started in
     */
    @Override public void notifyPrioritizedRotation(int rotation) throws android.os.RemoteException
    {
    }
    /** Notifies to expand notification panel. */
    @Override public void expandNotificationPanel() throws android.os.RemoteException
    {
    }
    /** Notifies SystemUI of a back KeyEvent. */
    @Override public void onBackEvent(android.view.KeyEvent keyEvent) throws android.os.RemoteException
    {
    }
    /** Sets home rotation enabled. */
    @Override public void setHomeRotationEnabled(boolean enabled) throws android.os.RemoteException
    {
    }
    /** Notifies when taskbar status updated */
    @Override public void notifyTaskbarStatus(boolean visible, boolean stashed) throws android.os.RemoteException
    {
    }
    /**
     * Notifies sysui when taskbar requests autoHide to stop auto-hiding
     * If called to suspend, caller is also responsible for calling this method to un-suspend
     * @param suspend should be true to stop auto-hide, false to resume normal behavior
     */
    @Override public void notifyTaskbarAutohideSuspend(boolean suspend) throws android.os.RemoteException
    {
    }
    /** Notifies that the IME switcher button has been pressed. */
    @Override public void onImeSwitcherPressed() throws android.os.RemoteException
    {
    }
    /** Notifies to toggle notification panel. */
    @Override public void toggleNotificationPanel() throws android.os.RemoteException
    {
    }
    /** Handle the screenshot request. */
    @Override public void takeScreenshot(com.android.internal.util.ScreenshotRequest request) throws android.os.RemoteException
    {
    }
    /**
     * Dispatches trackpad status bar motion event to the notification shade. Currently these events
     * are from the input monitor in {@link TouchInteractionService}. This is different from
     * {@link #onStatusBarTouchEvent} above in that, this directly dispatches motion events to the
     * notification shade, while {@link #onStatusBarTouchEvent} relies on setting the launcher
     * window slippery to allow the frameworks to route those events after passing the initial
     * threshold.
     */
    @Override public void onStatusBarTrackpadEvent(android.view.MotionEvent event) throws android.os.RemoteException
    {
    }
    /**
     * Animate the nav bar being long-pressed.
     * 
     * @param isTouchDown {@code true} if the button is starting to be pressed ({@code false} if
     *                                released or canceled)
     * @param shrink {@code true} if the handle should shrink, {@code false} if it should grow
     * @param durationMs how long the animation should take (for the {@code isTouchDown} case, this
     *                   should be the same as the amount of time to trigger a long-press)
     */
    @Override public void animateNavBarLongPress(boolean isTouchDown, boolean shrink, long durationMs) throws android.os.RemoteException
    {
    }
    /**
     * Set the override value for home button long press duration in ms and slop multiplier and
     * haptic.
     */
    @Override public void setOverrideHomeButtonLongPress(long duration, float slopMultiplier, boolean haptic) throws android.os.RemoteException
    {
    }
    /** Notifies to toggle quick settings panel. */
    @Override public void toggleQuickSettingsPanel() throws android.os.RemoteException
    {
    }
    /** Notifies that the IME Switcher button has been long pressed. */
    @Override public void onImeSwitcherLongPress() throws android.os.RemoteException
    {
    }
    /** Updates contextual education stats when target gesture type is triggered. */
    @Override public void updateContextualEduStats(boolean isTrackpadGesture, java.lang.String gestureType) throws android.os.RemoteException
    {
    }
    /** Sent after layout is performed for the "recents" button and it is visible on screen. */
    @Override public void notifyRecentsButtonPositionChanged(android.graphics.Rect position) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements com.android.systemui.shared.recents.ISystemUiProxy
  {
    /** Construct the stub and attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an com.android.systemui.shared.recents.ISystemUiProxy interface,
     * generating a proxy if needed.
     */
    public static com.android.systemui.shared.recents.ISystemUiProxy asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof com.android.systemui.shared.recents.ISystemUiProxy))) {
        return ((com.android.systemui.shared.recents.ISystemUiProxy)iin);
      }
      return new com.android.systemui.shared.recents.ISystemUiProxy.Stub.Proxy(obj);
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
        case TRANSACTION_startScreenPinning:
        {
          int _arg0;
          _arg0 = data.readInt();
          this.startScreenPinning(_arg0);
          break;
        }
        case TRANSACTION_onOverviewShown:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.onOverviewShown(_arg0);
          break;
        }
        case TRANSACTION_onStatusBarTouchEvent:
        {
          android.view.MotionEvent _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.view.MotionEvent.CREATOR);
          this.onStatusBarTouchEvent(_arg0);
          break;
        }
        case TRANSACTION_onAssistantProgress:
        {
          float _arg0;
          _arg0 = data.readFloat();
          this.onAssistantProgress(_arg0);
          break;
        }
        case TRANSACTION_onAssistantGestureCompletion:
        {
          float _arg0;
          _arg0 = data.readFloat();
          this.onAssistantGestureCompletion(_arg0);
          break;
        }
        case TRANSACTION_startAssistant:
        {
          android.os.Bundle _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.os.Bundle.CREATOR);
          this.startAssistant(_arg0);
          break;
        }
        case TRANSACTION_setAssistantOverridesRequested:
        {
          int[] _arg0;
          _arg0 = data.createIntArray();
          this.setAssistantOverridesRequested(_arg0);
          break;
        }
        case TRANSACTION_notifyAccessibilityButtonClicked:
        {
          int _arg0;
          _arg0 = data.readInt();
          this.notifyAccessibilityButtonClicked(_arg0);
          break;
        }
        case TRANSACTION_notifyAccessibilityButtonLongClicked:
        {
          this.notifyAccessibilityButtonLongClicked();
          break;
        }
        case TRANSACTION_stopScreenPinning:
        {
          this.stopScreenPinning();
          break;
        }
        case TRANSACTION_notifyPrioritizedRotation:
        {
          int _arg0;
          _arg0 = data.readInt();
          this.notifyPrioritizedRotation(_arg0);
          break;
        }
        case TRANSACTION_expandNotificationPanel:
        {
          this.expandNotificationPanel();
          break;
        }
        case TRANSACTION_onBackEvent:
        {
          android.view.KeyEvent _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.view.KeyEvent.CREATOR);
          this.onBackEvent(_arg0);
          break;
        }
        case TRANSACTION_setHomeRotationEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setHomeRotationEnabled(_arg0);
          break;
        }
        case TRANSACTION_notifyTaskbarStatus:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.notifyTaskbarStatus(_arg0, _arg1);
          break;
        }
        case TRANSACTION_notifyTaskbarAutohideSuspend:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.notifyTaskbarAutohideSuspend(_arg0);
          break;
        }
        case TRANSACTION_onImeSwitcherPressed:
        {
          this.onImeSwitcherPressed();
          break;
        }
        case TRANSACTION_toggleNotificationPanel:
        {
          this.toggleNotificationPanel();
          break;
        }
        case TRANSACTION_takeScreenshot:
        {
          com.android.internal.util.ScreenshotRequest _arg0;
          _arg0 = _Parcel.readTypedObject(data, com.android.internal.util.ScreenshotRequest.CREATOR);
          this.takeScreenshot(_arg0);
          break;
        }
        case TRANSACTION_onStatusBarTrackpadEvent:
        {
          android.view.MotionEvent _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.view.MotionEvent.CREATOR);
          this.onStatusBarTrackpadEvent(_arg0);
          break;
        }
        case TRANSACTION_animateNavBarLongPress:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          long _arg2;
          _arg2 = data.readLong();
          this.animateNavBarLongPress(_arg0, _arg1, _arg2);
          break;
        }
        case TRANSACTION_setOverrideHomeButtonLongPress:
        {
          long _arg0;
          _arg0 = data.readLong();
          float _arg1;
          _arg1 = data.readFloat();
          boolean _arg2;
          _arg2 = (0!=data.readInt());
          this.setOverrideHomeButtonLongPress(_arg0, _arg1, _arg2);
          break;
        }
        case TRANSACTION_toggleQuickSettingsPanel:
        {
          this.toggleQuickSettingsPanel();
          break;
        }
        case TRANSACTION_onImeSwitcherLongPress:
        {
          this.onImeSwitcherLongPress();
          break;
        }
        case TRANSACTION_updateContextualEduStats:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.updateContextualEduStats(_arg0, _arg1);
          break;
        }
        case TRANSACTION_notifyRecentsButtonPositionChanged:
        {
          android.graphics.Rect _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.graphics.Rect.CREATOR);
          this.notifyRecentsButtonPositionChanged(_arg0);
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static final class Proxy implements com.android.systemui.shared.recents.ISystemUiProxy
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
      /** Begins screen pinning on the provided {@param taskId}. */
      @Override public void startScreenPinning(int taskId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(taskId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_startScreenPinning, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Notifies SystemUI that Overview is shown. */
      @Override public void onOverviewShown(boolean fromHome) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((fromHome)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_onOverviewShown, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /**
       * Proxies motion events from the homescreen UI to the status bar. Only called when
       * swipe down is detected on WORKSPACE. The sender guarantees the following order of events on
       * the tracking pointer.
       * 
       * Normal gesture: DOWN, MOVE/POINTER_DOWN/POINTER_UP)*, UP or CANCLE
       */
      @Override public void onStatusBarTouchEvent(android.view.MotionEvent event) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, event, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onStatusBarTouchEvent, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Proxies the assistant gesture's progress started from navigation bar. */
      @Override public void onAssistantProgress(float progress) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeFloat(progress);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onAssistantProgress, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /**
       * Proxies the assistant gesture fling velocity (in pixels per millisecond) upon completion.
       * Velocity is 0 for drag gestures.
       */
      @Override public void onAssistantGestureCompletion(float velocity) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeFloat(velocity);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onAssistantGestureCompletion, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Start the assistant. */
      @Override public void startAssistant(android.os.Bundle bundle) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, bundle, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_startAssistant, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /**
       * Indicates that the given Assist invocation types should be handled by Launcher via
       * LauncherProxy#onAssistantOverrideInvoked and should not be invoked by SystemUI.
       * 
       * @param invocationTypes The invocation types that will henceforth be handled via
       *         LauncherProxy (Launcher); other invocation types should be handled by SysUI.
       */
      @Override public void setAssistantOverridesRequested(int[] invocationTypes) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeIntArray(invocationTypes);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setAssistantOverridesRequested, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Notifies that the accessibility button in the system's navigation area has been clicked */
      @Override public void notifyAccessibilityButtonClicked(int displayId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(displayId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_notifyAccessibilityButtonClicked, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Notifies that the accessibility button in the system's navigation area has been long clicked */
      @Override public void notifyAccessibilityButtonLongClicked() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_notifyAccessibilityButtonLongClicked, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Ends the system screen pinning. */
      @Override public void stopScreenPinning() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_stopScreenPinning, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /**
       * Notifies that quickstep will switch to a new task
       * @param rotation indicates which Surface.Rotation the gesture was started in
       */
      @Override public void notifyPrioritizedRotation(int rotation) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(rotation);
          boolean _status = mRemote.transact(Stub.TRANSACTION_notifyPrioritizedRotation, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Notifies to expand notification panel. */
      @Override public void expandNotificationPanel() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_expandNotificationPanel, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Notifies SystemUI of a back KeyEvent. */
      @Override public void onBackEvent(android.view.KeyEvent keyEvent) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, keyEvent, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onBackEvent, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Sets home rotation enabled. */
      @Override public void setHomeRotationEnabled(boolean enabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setHomeRotationEnabled, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Notifies when taskbar status updated */
      @Override public void notifyTaskbarStatus(boolean visible, boolean stashed) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((visible)?(1):(0)));
          _data.writeInt(((stashed)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_notifyTaskbarStatus, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /**
       * Notifies sysui when taskbar requests autoHide to stop auto-hiding
       * If called to suspend, caller is also responsible for calling this method to un-suspend
       * @param suspend should be true to stop auto-hide, false to resume normal behavior
       */
      @Override public void notifyTaskbarAutohideSuspend(boolean suspend) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((suspend)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_notifyTaskbarAutohideSuspend, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Notifies that the IME switcher button has been pressed. */
      @Override public void onImeSwitcherPressed() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onImeSwitcherPressed, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Notifies to toggle notification panel. */
      @Override public void toggleNotificationPanel() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_toggleNotificationPanel, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Handle the screenshot request. */
      @Override public void takeScreenshot(com.android.internal.util.ScreenshotRequest request) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, request, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_takeScreenshot, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /**
       * Dispatches trackpad status bar motion event to the notification shade. Currently these events
       * are from the input monitor in {@link TouchInteractionService}. This is different from
       * {@link #onStatusBarTouchEvent} above in that, this directly dispatches motion events to the
       * notification shade, while {@link #onStatusBarTouchEvent} relies on setting the launcher
       * window slippery to allow the frameworks to route those events after passing the initial
       * threshold.
       */
      @Override public void onStatusBarTrackpadEvent(android.view.MotionEvent event) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, event, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onStatusBarTrackpadEvent, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /**
       * Animate the nav bar being long-pressed.
       * 
       * @param isTouchDown {@code true} if the button is starting to be pressed ({@code false} if
       *                                released or canceled)
       * @param shrink {@code true} if the handle should shrink, {@code false} if it should grow
       * @param durationMs how long the animation should take (for the {@code isTouchDown} case, this
       *                   should be the same as the amount of time to trigger a long-press)
       */
      @Override public void animateNavBarLongPress(boolean isTouchDown, boolean shrink, long durationMs) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((isTouchDown)?(1):(0)));
          _data.writeInt(((shrink)?(1):(0)));
          _data.writeLong(durationMs);
          boolean _status = mRemote.transact(Stub.TRANSACTION_animateNavBarLongPress, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /**
       * Set the override value for home button long press duration in ms and slop multiplier and
       * haptic.
       */
      @Override public void setOverrideHomeButtonLongPress(long duration, float slopMultiplier, boolean haptic) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeLong(duration);
          _data.writeFloat(slopMultiplier);
          _data.writeInt(((haptic)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setOverrideHomeButtonLongPress, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Notifies to toggle quick settings panel. */
      @Override public void toggleQuickSettingsPanel() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_toggleQuickSettingsPanel, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Notifies that the IME Switcher button has been long pressed. */
      @Override public void onImeSwitcherLongPress() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onImeSwitcherLongPress, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Updates contextual education stats when target gesture type is triggered. */
      @Override public void updateContextualEduStats(boolean isTrackpadGesture, java.lang.String gestureType) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((isTrackpadGesture)?(1):(0)));
          _data.writeString(gestureType);
          boolean _status = mRemote.transact(Stub.TRANSACTION_updateContextualEduStats, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Sent after layout is performed for the "recents" button and it is visible on screen. */
      @Override public void notifyRecentsButtonPositionChanged(android.graphics.Rect position) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, position, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_notifyRecentsButtonPositionChanged, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
    }
    static final int TRANSACTION_startScreenPinning = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_onOverviewShown = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_onStatusBarTouchEvent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
    static final int TRANSACTION_onAssistantProgress = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
    static final int TRANSACTION_onAssistantGestureCompletion = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
    static final int TRANSACTION_startAssistant = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
    static final int TRANSACTION_setAssistantOverridesRequested = (android.os.IBinder.FIRST_CALL_TRANSACTION + 53);
    static final int TRANSACTION_notifyAccessibilityButtonClicked = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
    static final int TRANSACTION_notifyAccessibilityButtonLongClicked = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
    static final int TRANSACTION_stopScreenPinning = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
    static final int TRANSACTION_notifyPrioritizedRotation = (android.os.IBinder.FIRST_CALL_TRANSACTION + 25);
    static final int TRANSACTION_expandNotificationPanel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 29);
    static final int TRANSACTION_onBackEvent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 44);
    static final int TRANSACTION_setHomeRotationEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 45);
    static final int TRANSACTION_notifyTaskbarStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 47);
    static final int TRANSACTION_notifyTaskbarAutohideSuspend = (android.os.IBinder.FIRST_CALL_TRANSACTION + 48);
    static final int TRANSACTION_onImeSwitcherPressed = (android.os.IBinder.FIRST_CALL_TRANSACTION + 49);
    static final int TRANSACTION_toggleNotificationPanel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 50);
    static final int TRANSACTION_takeScreenshot = (android.os.IBinder.FIRST_CALL_TRANSACTION + 51);
    static final int TRANSACTION_onStatusBarTrackpadEvent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 52);
    static final int TRANSACTION_animateNavBarLongPress = (android.os.IBinder.FIRST_CALL_TRANSACTION + 54);
    static final int TRANSACTION_setOverrideHomeButtonLongPress = (android.os.IBinder.FIRST_CALL_TRANSACTION + 55);
    static final int TRANSACTION_toggleQuickSettingsPanel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 56);
    static final int TRANSACTION_onImeSwitcherLongPress = (android.os.IBinder.FIRST_CALL_TRANSACTION + 57);
    static final int TRANSACTION_updateContextualEduStats = (android.os.IBinder.FIRST_CALL_TRANSACTION + 58);
    static final int TRANSACTION_notifyRecentsButtonPositionChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 59);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "com.android.systemui.shared.recents.ISystemUiProxy";
  /** Begins screen pinning on the provided {@param taskId}. */
  public void startScreenPinning(int taskId) throws android.os.RemoteException;
  /** Notifies SystemUI that Overview is shown. */
  public void onOverviewShown(boolean fromHome) throws android.os.RemoteException;
  /**
   * Proxies motion events from the homescreen UI to the status bar. Only called when
   * swipe down is detected on WORKSPACE. The sender guarantees the following order of events on
   * the tracking pointer.
   * 
   * Normal gesture: DOWN, MOVE/POINTER_DOWN/POINTER_UP)*, UP or CANCLE
   */
  public void onStatusBarTouchEvent(android.view.MotionEvent event) throws android.os.RemoteException;
  /** Proxies the assistant gesture's progress started from navigation bar. */
  public void onAssistantProgress(float progress) throws android.os.RemoteException;
  /**
   * Proxies the assistant gesture fling velocity (in pixels per millisecond) upon completion.
   * Velocity is 0 for drag gestures.
   */
  public void onAssistantGestureCompletion(float velocity) throws android.os.RemoteException;
  /** Start the assistant. */
  public void startAssistant(android.os.Bundle bundle) throws android.os.RemoteException;
  /**
   * Indicates that the given Assist invocation types should be handled by Launcher via
   * LauncherProxy#onAssistantOverrideInvoked and should not be invoked by SystemUI.
   * 
   * @param invocationTypes The invocation types that will henceforth be handled via
   *         LauncherProxy (Launcher); other invocation types should be handled by SysUI.
   */
  public void setAssistantOverridesRequested(int[] invocationTypes) throws android.os.RemoteException;
  /** Notifies that the accessibility button in the system's navigation area has been clicked */
  public void notifyAccessibilityButtonClicked(int displayId) throws android.os.RemoteException;
  /** Notifies that the accessibility button in the system's navigation area has been long clicked */
  public void notifyAccessibilityButtonLongClicked() throws android.os.RemoteException;
  /** Ends the system screen pinning. */
  public void stopScreenPinning() throws android.os.RemoteException;
  /**
   * Notifies that quickstep will switch to a new task
   * @param rotation indicates which Surface.Rotation the gesture was started in
   */
  public void notifyPrioritizedRotation(int rotation) throws android.os.RemoteException;
  /** Notifies to expand notification panel. */
  public void expandNotificationPanel() throws android.os.RemoteException;
  /** Notifies SystemUI of a back KeyEvent. */
  public void onBackEvent(android.view.KeyEvent keyEvent) throws android.os.RemoteException;
  /** Sets home rotation enabled. */
  public void setHomeRotationEnabled(boolean enabled) throws android.os.RemoteException;
  /** Notifies when taskbar status updated */
  public void notifyTaskbarStatus(boolean visible, boolean stashed) throws android.os.RemoteException;
  /**
   * Notifies sysui when taskbar requests autoHide to stop auto-hiding
   * If called to suspend, caller is also responsible for calling this method to un-suspend
   * @param suspend should be true to stop auto-hide, false to resume normal behavior
   */
  public void notifyTaskbarAutohideSuspend(boolean suspend) throws android.os.RemoteException;
  /** Notifies that the IME switcher button has been pressed. */
  public void onImeSwitcherPressed() throws android.os.RemoteException;
  /** Notifies to toggle notification panel. */
  public void toggleNotificationPanel() throws android.os.RemoteException;
  /** Handle the screenshot request. */
  public void takeScreenshot(com.android.internal.util.ScreenshotRequest request) throws android.os.RemoteException;
  /**
   * Dispatches trackpad status bar motion event to the notification shade. Currently these events
   * are from the input monitor in {@link TouchInteractionService}. This is different from
   * {@link #onStatusBarTouchEvent} above in that, this directly dispatches motion events to the
   * notification shade, while {@link #onStatusBarTouchEvent} relies on setting the launcher
   * window slippery to allow the frameworks to route those events after passing the initial
   * threshold.
   */
  public void onStatusBarTrackpadEvent(android.view.MotionEvent event) throws android.os.RemoteException;
  /**
   * Animate the nav bar being long-pressed.
   * 
   * @param isTouchDown {@code true} if the button is starting to be pressed ({@code false} if
   *                                released or canceled)
   * @param shrink {@code true} if the handle should shrink, {@code false} if it should grow
   * @param durationMs how long the animation should take (for the {@code isTouchDown} case, this
   *                   should be the same as the amount of time to trigger a long-press)
   */
  public void animateNavBarLongPress(boolean isTouchDown, boolean shrink, long durationMs) throws android.os.RemoteException;
  /**
   * Set the override value for home button long press duration in ms and slop multiplier and
   * haptic.
   */
  public void setOverrideHomeButtonLongPress(long duration, float slopMultiplier, boolean haptic) throws android.os.RemoteException;
  /** Notifies to toggle quick settings panel. */
  public void toggleQuickSettingsPanel() throws android.os.RemoteException;
  /** Notifies that the IME Switcher button has been long pressed. */
  public void onImeSwitcherLongPress() throws android.os.RemoteException;
  /** Updates contextual education stats when target gesture type is triggered. */
  public void updateContextualEduStats(boolean isTrackpadGesture, java.lang.String gestureType) throws android.os.RemoteException;
  /** Sent after layout is performed for the "recents" button and it is visible on screen. */
  public void notifyRecentsButtonPositionChanged(android.graphics.Rect position) throws android.os.RemoteException;
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
