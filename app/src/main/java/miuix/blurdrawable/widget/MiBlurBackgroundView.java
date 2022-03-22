package miuix.blurdrawable.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class MiBlurBackgroundView extends FrameLayout {

  private BlurBackgroundView mBackgroundView;

  public MiBlurBackgroundView(Context context) {
    this(context, (AttributeSet) null);
  }

  public MiBlurBackgroundView(Context context, AttributeSet attributeSet) {
    super(context, attributeSet);
    this.addBlurView(context);
  }

  private void addBlurView(Context context) {
    this.mBackgroundView = new BlurBackgroundView(context);
    LayoutParams lp = new LayoutParams(-1, -1);
    this.mBackgroundView.setLayoutParams(lp);
    this.addView(this.mBackgroundView, 0);
    this.setBlurBackground(false);
  }

  public boolean setBlurBackground(boolean b) {
    return this.mBackgroundView.setBlurBackground(b);
  }
}
