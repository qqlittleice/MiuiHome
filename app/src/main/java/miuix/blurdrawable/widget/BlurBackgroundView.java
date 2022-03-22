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
    this(context, (AttributeSet)null);
  }

  public BlurBackgroundView(Context context, AttributeSet attributeSet) {
    super(context, attributeSet);
  }

  private void createBlurBackground() {
    this.mBlurBackground = new BlurDrawable();
    if ((this.getResources().getConfiguration().uiMode & 48) == 32) {
      this.mBlurBackground.setMixColor(19, Color.argb(165, 92, 92, 92));
      this.mBlurForeground = new ColorDrawable(Color.parseColor("#80000000"));
    } else {
      this.mBlurBackground.setMixColor(18, Color.argb(165, 107, 107, 107));
      this.mBlurForeground = new ColorDrawable(Color.parseColor("#ccffffff"));
    }
    this.mBlurBackground.setBlurRatio(1.0F);
  }

  public boolean isSupportBlur() {
    return true;
  }

  public void setAlpha(float f) {
    super.setAlpha(f);
    int i = (int)(f * 255.0F);
    Drawable mBlurForeground = this.mBlurForeground;
    if (mBlurForeground != null) {
      mBlurForeground.setAlpha(i);
    }
    BlurDrawable mBlurBackground = this.mBlurBackground;
    if (mBlurBackground != null) {
      mBlurBackground.setAlpha(i);
    }
  }

  public boolean setBlurBackground(boolean b) {
    if (!this.isSupportBlur()) {
      return false;
    } else {
      if (b) {
        if (this.mBlurBackground == null) {
          try {
            this.createBlurBackground();
          } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Blur creat fail e:");
            stringBuilder.append(e);
            Log.e("Blur", stringBuilder.toString());
            this.mBlurBackground = null;
            return false;
          }
        }
        if (this.mBlurBackground != null && (this.getVisibility() != View.VISIBLE || this.getBackground() == null)) {
          this.setVisibility(View.VISIBLE);
          this.setForeground(this.mBlurForeground);
          this.setBackground(this.mBlurBackground);
          this.setAlpha(1.0F);
        }
      } else if (this.getVisibility() == View.VISIBLE) {
        this.setForeground((Drawable)null);
        this.setBackground((Drawable)null);
        this.mBlurForeground = null;
        this.mBlurBackground = null;
        this.setVisibility(View.GONE);
      }
      return true;
    }
  }
}
