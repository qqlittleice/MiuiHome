package com.miui.blur.sdk.drawable;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import java.lang.reflect.Method;

public class BlurDrawable extends Drawable {

  private static final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
  private final long mFunctor;
  private Method mMethodCallDrawGLFunction;
  private final Paint mPaint;

  static {
    System.loadLibrary("miuiblursdk");
  }

  public BlurDrawable() {
    Paint paint = new Paint();
    this.mPaint = paint;
    paint.setColor(0);
    int mBlurWidth = this.getBounds().width();
    int mBlurHeight = this.getBounds().height();
    this.mFunctor = nCreateNativeFunctor(mBlurWidth, mBlurHeight);
    this.initMethod();
  }

  private void drawBlurBack(Canvas canvas) {
    try {
      this.mMethodCallDrawGLFunction.setAccessible(true);
      this.mMethodCallDrawGLFunction.invoke(canvas, this.mFunctor);
    } catch (Throwable ignored) {
    }
  }

  @SuppressLint("PrivateApi")
  private void initMethod() {
    {
      int i = VERSION.SDK_INT;
      if (i > 28) {
        try {
          this.mMethodCallDrawGLFunction = (Method) Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class).invoke(Class.class.getDeclaredMethod("forName", String.class).invoke((Object) null, "android.graphics.RecordingCanvas"), "callDrawGLFunction2", new Class[]{Long.TYPE});
        } catch (Exception ignored) {
        }
      } else if (i > 22) {
        try {
          this.mMethodCallDrawGLFunction = Class.forName("android.view.DisplayListCanvas").getMethod("callDrawGLFunction2", Long.TYPE);
        } catch (Exception ignored) {
        }
      }
    }
  }

  private void invalidateOnMainThread() {
    Looper l = Looper.myLooper();
    if (l != null && l.equals(Looper.getMainLooper())) {
      this.invalidateSelf();
    } else {
      mainThreadHandler.post(BlurDrawable.this::invalidateSelf);
    }
  }

  public static native long nCreateNativeFunctor(int i1, int i2);

  public static native long nDeleteNativeFunctor(long l1);

  public static native void nSetAlpha(long l, float f);

  public static native void nSetBlurRatio(long l, float f);

  public static native void nSetMixColor(long l, int i1, int i2);

  public void draw(Canvas canvas) {
    if (canvas.isHardwareAccelerated()) {
      this.drawBlurBack(canvas);
    } else {
      canvas.drawRect(this.getBounds(), this.mPaint);
    }

  }

  protected void finalize() throws Throwable {
    nDeleteNativeFunctor(this.mFunctor);
    super.finalize();
  }

  public int getOpacity() {
    return PixelFormat.UNKNOWN;
  }

  public void setAlpha(int i) {
    nSetAlpha(this.mFunctor, (float) i / 255.0F);
  }

  public void setBlurRatio(float f) {
    nSetBlurRatio(this.mFunctor, f);
    this.invalidateOnMainThread();
  }

  public void setColorFilter(ColorFilter colorFilter) {
  }

  public void setMixColor(int i1, int i2) {
    nSetMixColor(this.mFunctor, i2, i1);
    this.invalidateOnMainThread();
  }
}
