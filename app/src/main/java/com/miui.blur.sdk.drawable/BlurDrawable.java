package com.miui.blur.sdk.drawable;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Build.VERSION;
import android.util.Log;
import java.lang.reflect.Method;

public class BlurDrawable extends Drawable {
  private static final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
  private int mAlpha;
  private boolean mBlurEnabled = true;
  private int mBlurHeight = this.getBounds().height();
  private int mBlurWidth = this.getBounds().width();
  private long mFunctor = 0L;
  private Method mMethodCallDrawGLFunction;
  private Paint mPaint;

  static {
    try {
      if (isSupportBlurStatic()) {
        System.loadLibrary("miuiblursdk");
      }
    } catch (Throwable var6) {
      Log.e("BlurDrawable", "Failed to load miuiblursdk library", var6);
    }

  }

  public BlurDrawable() {
    Paint var1 = new Paint();
    this.mPaint = var1;
    var1.setColor(0);
    if (this.isSupportBlur()) {
      this.mFunctor = nCreateNativeFunctor(this.mBlurWidth, this.mBlurHeight);
      this.initMethod();
    }

  }

  private void drawBlurBack(Canvas var1) {
    try {
      this.mMethodCallDrawGLFunction.setAccessible(true);
      this.mMethodCallDrawGLFunction.invoke(var1, this.mFunctor);
    } catch (Throwable var3) {
      Log.e("BlurDrawable", "canvas function [callDrawGLFunction()] error", var3);
    }

  }

  @SuppressLint("PrivateApi")
  private void initMethod() {
    Exception var10000;
    label51: {
      int var1;
      try {
        var1 = VERSION.SDK_INT;
      } catch (Exception var8) {
        var10000 = var8;
        break label51;
      }

      if (var1 > 28) {
        try {
          this.mMethodCallDrawGLFunction = (Method)Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class).invoke((Class)Class.class.getDeclaredMethod("forName", String.class).invoke((Object)null, "android.graphics.RecordingCanvas"), "callDrawGLFunction2", new Class[]{Long.TYPE});
          return;
        } catch (Exception var3) {
          var10000 = var3;
        }
      } else if (var1 > 22) {
        try {
          this.mMethodCallDrawGLFunction = Class.forName("android.view.DisplayListCanvas").getMethod("callDrawGLFunction2", Long.TYPE);
          return;
        } catch (Exception var4) {
          var10000 = var4;
        }
      } else if (var1 == 21) {
        try {
          this.mMethodCallDrawGLFunction = Class.forName("android.view.HardwareCanvas").getMethod("callDrawGLFunction", Long.TYPE);
          return;
        } catch (Exception var5) {
          var10000 = var5;
        }
      } else if (var1 == 22) {
        try {
          this.mMethodCallDrawGLFunction = Class.forName("android.view.HardwareCanvas").getMethod("callDrawGLFunction2", Long.TYPE);
          return;
        } catch (Exception var6) {
          var10000 = var6;
        }
      } else {
        try {
          this.mMethodCallDrawGLFunction = Class.forName("android.view.HardwareCanvas").getMethod("callDrawGLFunction", Integer.TYPE);
          return;
        } catch (Exception var7) {
          var10000 = var7;
        }
      }
    }

    Exception var2 = var10000;
    Log.e("BlurDrawable", "canvas function [callDrawGLFunction()] error", var2);
  }

  private void invalidateOnMainThread() {
    Looper var1 = Looper.myLooper();
    if (var1 != null && var1.equals(Looper.getMainLooper())) {
      this.invalidateSelf();
    } else {
      mainThreadHandler.post(BlurDrawable.this::invalidateSelf);
    }

  }

  public static boolean isSupportBlurStatic() {
    return true;
  }

  public static native long nCreateNativeFunctor(int var0, int var1);

  public static native long nDeleteNativeFunctor(long var0);

  public static native void nSetAlpha(long var0, float var2);

  public static native void nSetBlurRatio(long var0, float var2);

  public static native void nSetMixColor(long var0, int var2, int var3);

  public void draw(Canvas var1) {
    Log.e("BlurDrawable", "draw");
    if (var1.isHardwareAccelerated() && this.mBlurEnabled && this.isSupportBlur()) {
      this.drawBlurBack(var1);
    } else {
      var1.drawRect(this.getBounds(), this.mPaint);
    }

  }

  protected void finalize() throws Throwable {
    if (this.isSupportBlur()) {
      nDeleteNativeFunctor(this.mFunctor);
    }

    Log.e("BlurDrawable", "finalize");
    super.finalize();
  }

  public int getOpacity() {
    return PixelFormat.UNKNOWN;
  }

  public boolean isSupportBlur() {
    return true;
  }

  public void setAlpha(int var1) {
    this.mAlpha = var1;
    nSetAlpha(this.mFunctor, (float)var1 / 255.0F);
  }

  public void setBlurRatio(float var1) {
    if (this.isSupportBlur()) {
      nSetBlurRatio(this.mFunctor, var1);
      this.invalidateOnMainThread();
    }
  }

  public void setColorFilter(ColorFilter var1) {
    Log.d("BlurDrawable", "nothing in setColorFilter");
  }

  public void setMixColor(int var1, int var2) {
    if (this.isSupportBlur()) {
      nSetMixColor(this.mFunctor, var2, var1);
      this.invalidateOnMainThread();
    }
  }
}
