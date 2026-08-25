/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /Users/docmoates/Library/Android/sdk/build-tools/37.0.0/aidl -p/Users/docmoates/Library/Android/sdk/platforms/android-37.1/framework.aidl -o/Users/docmoates/Lawnchair/compatLib/build/generated/aidl_source_output_dir/debug/out -I/Users/docmoates/Lawnchair/compatLib/src/main/java -I/Users/docmoates/Lawnchair/compatLib/src/debug/aidl -I/Users/docmoates/.gradle/caches/9.7.1/transforms/5678b756e7305cedafb7d368432c7498/transformed/core-1.19.0/aidl -I/Users/docmoates/.gradle/caches/9.7.1/transforms/f92a0b6208819fb7599553bffe7e9174/transformed/versionedparcelable-1.1.1/aidl -d/var/folders/lf/7k355n1d2g5_v9yg4x1hy15r0000gn/T/aidl5174844651386721309.d /Users/docmoates/Lawnchair/compatLib/src/main/java/android/window/WindowAnimationState.aidl
 *
 * DO NOT CHECK THIS FILE INTO A CODE TREE (e.g. git, etc..).
 * ALWAYS GENERATE THIS FILE FROM UPDATED AIDL COMPILER
 * AS A BUILD INTERMEDIATE ONLY. THIS IS NOT SOURCE CODE.
 */
package android.window;
/**
 * Properties of a window animation at a given point in time.
 * 
 * {@hide}
 */
public class WindowAnimationState implements android.os.Parcelable
{
  public long timestamp = 0L;
  public android.graphics.RectF bounds;
  public float scale = 0.000000f;
  public float topLeftRadius = 0.000000f;
  public float topRightRadius = 0.000000f;
  public float bottomRightRadius = 0.000000f;
  public float bottomLeftRadius = 0.000000f;
  public android.graphics.PointF velocityPxPerMs;
  public static final android.os.Parcelable.Creator<WindowAnimationState> CREATOR = new android.os.Parcelable.Creator<WindowAnimationState>() {
    @Override
    public WindowAnimationState createFromParcel(android.os.Parcel _aidl_source) {
      WindowAnimationState _aidl_out = new WindowAnimationState();
      _aidl_out.readFromParcel(_aidl_source);
      return _aidl_out;
    }
    @Override
    public WindowAnimationState[] newArray(int _aidl_size) {
      return new WindowAnimationState[_aidl_size];
    }
  };
  @Override public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag)
  {
    int _aidl_start_pos = _aidl_parcel.dataPosition();
    _aidl_parcel.writeInt(0);
    _aidl_parcel.writeLong(timestamp);
    _Parcel.writeTypedObject(_aidl_parcel, bounds, _aidl_flag);
    _aidl_parcel.writeFloat(scale);
    _aidl_parcel.writeFloat(topLeftRadius);
    _aidl_parcel.writeFloat(topRightRadius);
    _aidl_parcel.writeFloat(bottomRightRadius);
    _aidl_parcel.writeFloat(bottomLeftRadius);
    _Parcel.writeTypedObject(_aidl_parcel, velocityPxPerMs, _aidl_flag);
    int _aidl_end_pos = _aidl_parcel.dataPosition();
    _aidl_parcel.setDataPosition(_aidl_start_pos);
    _aidl_parcel.writeInt(_aidl_end_pos - _aidl_start_pos);
    _aidl_parcel.setDataPosition(_aidl_end_pos);
  }
  public final void readFromParcel(android.os.Parcel _aidl_parcel)
  {
    int _aidl_start_pos = _aidl_parcel.dataPosition();
    int _aidl_parcelable_size = _aidl_parcel.readInt();
    try {
      if (_aidl_parcelable_size < 4) throw new android.os.BadParcelableException("Parcelable too small");;
      if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) return;
      timestamp = _aidl_parcel.readLong();
      if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) return;
      bounds = _Parcel.readTypedObject(_aidl_parcel, android.graphics.RectF.CREATOR);
      if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) return;
      scale = _aidl_parcel.readFloat();
      if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) return;
      topLeftRadius = _aidl_parcel.readFloat();
      if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) return;
      topRightRadius = _aidl_parcel.readFloat();
      if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) return;
      bottomRightRadius = _aidl_parcel.readFloat();
      if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) return;
      bottomLeftRadius = _aidl_parcel.readFloat();
      if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) return;
      velocityPxPerMs = _Parcel.readTypedObject(_aidl_parcel, android.graphics.PointF.CREATOR);
    } finally {
      if (_aidl_start_pos > (Integer.MAX_VALUE - _aidl_parcelable_size)) {
        throw new android.os.BadParcelableException("Overflow in the size of parcelable");
      }
      _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
    }
  }
  @Override
  public int describeContents() {
    int _mask = 0;
    _mask |= describeContents(bounds);
    _mask |= describeContents(velocityPxPerMs);
    return _mask;
  }
  private int describeContents(Object _v) {
    if (_v == null) return 0;
    if (_v instanceof android.os.Parcelable) {
      return ((android.os.Parcelable) _v).describeContents();
    }
    return 0;
  }
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
