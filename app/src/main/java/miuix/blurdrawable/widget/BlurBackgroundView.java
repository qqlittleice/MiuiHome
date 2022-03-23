package miuix.blurdrawable.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import com.miui.blur.sdk.drawable.BlurDrawable;

public class BlurBackgroundView extends FrameLayout {
  private BlurDrawable mBlurBackground;
  private Drawable mBlurForeground;

  public BlurBackgroundView(Context context) {
    this(context, null);
  }

  public BlurBackgroundView(Context context, AttributeSet attributeSet) {
    super(context, attributeSet);
  }

  public boolean isSupportBlur() {
    return BlurDrawable.isSupportBlurStatic();
  }

  public boolean setBlurBackground(boolean z) {
    if (!isSupportBlur()) {
      return false;
    }
    if (z) {
      if (this.mBlurBackground == null) {
        try {
          createBlurBackground();
        } catch (Exception e) {
          Log.e("Blur", "Blur creat fail e:" + e);
          this.mBlurBackground = null;
          return false;
        }
      }
      if (this.mBlurBackground == null) {
        return true;
      }
      if (getVisibility() == View.VISIBLE && getBackground() != null) {
        return true;
      }
      setVisibility(View.VISIBLE);
      setForeground(this.mBlurForeground);
      setBackground(this.mBlurBackground);
      setAlpha(1.0f);
      return true;
    } else if (getVisibility() != View.VISIBLE) {
      return true;
    } else {
      setForeground(null);
      setBackground(null);
      this.mBlurForeground = null;
      this.mBlurBackground = null;
      setVisibility(View.GONE);
      return true;
    }
  }

  private void createBlurBackground() {
    this.mBlurBackground = new BlurDrawable();
    if ((getResources().getConfiguration().uiMode & 48) == 32) {
      this.mBlurBackground.setMixColor(19, Color.argb(165, 92, 92, 92));
      this.mBlurForeground = new ColorDrawable(Color.parseColor("#80000000"));
    } else {
      this.mBlurBackground.setMixColor(18, Color.argb(165, 107, 107, 107));
      this.mBlurForeground = new ColorDrawable(Color.parseColor("#ccffffff"));
    }
    this.mBlurBackground.setBlurRatio(1.0f);
  }

  public void setAlpha(float f) {
    super.setAlpha(f);
    int i = (int) (f * 255.0f);
    Drawable drawable = this.mBlurForeground;
    if (drawable != null) {
      drawable.setAlpha(i);
    }
    BlurDrawable blurDrawable = this.mBlurBackground;
    if (blurDrawable != null) {
      blurDrawable.setAlpha(i);
    }
  }
}
