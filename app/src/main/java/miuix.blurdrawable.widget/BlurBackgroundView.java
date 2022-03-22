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

  public BlurBackgroundView(Context var1) {
    this(var1, (AttributeSet)null);
  }

  public BlurBackgroundView(Context var1, AttributeSet var2) {
    super(var1, var2);
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

  public void setAlpha(float var1) {
    super.setAlpha(var1);
    int var2 = (int)(var1 * 255.0F);
    Drawable var3 = this.mBlurForeground;
    if (var3 != null) {
      var3.setAlpha(var2);
    }

    BlurDrawable var4 = this.mBlurBackground;
    if (var4 != null) {
      var4.setAlpha(var2);
    }

  }

  public boolean setBlurBackground(boolean var1) {
    if (!this.isSupportBlur()) {
      return false;
    } else {
      if (var1) {
        if (this.mBlurBackground == null) {
          try {
            this.createBlurBackground();
          } catch (Exception var4) {
            StringBuilder var3 = new StringBuilder();
            var3.append("Blur creat fail e:");
            var3.append(var4);
            Log.e("Blur", var3.toString());
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
