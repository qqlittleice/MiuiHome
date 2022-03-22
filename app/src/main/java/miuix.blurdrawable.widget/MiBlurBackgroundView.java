package miuix.blurdrawable.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class MiBlurBackgroundView extends FrameLayout {
  private BlurBackgroundView mBackgroundView;

  public MiBlurBackgroundView(Context var1) {
    this(var1, (AttributeSet)null);
  }

  public MiBlurBackgroundView(Context var1, AttributeSet var2) {
    super(var1, var2);
    this.addBlurView(var1);
  }

  private void addBlurView(Context var1) {
    this.mBackgroundView = new BlurBackgroundView(var1);
    LayoutParams var2 = new LayoutParams(-1, -1);
    this.mBackgroundView.setLayoutParams(var2);
    this.addView(this.mBackgroundView, 0);
    this.setBlurBackground(false);
  }

  public boolean setBlurBackground(boolean var1) {
    return this.mBackgroundView.setBlurBackground(var1);
  }
}
