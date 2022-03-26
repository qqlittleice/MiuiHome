package com.yuk.miuihome.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.FrameLayout
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.getMethodByClassOrObject
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.yuk.miuihome.utils.ktx.callMethod

class BlurFrameLayout(context: Context): FrameLayout(context) {
    private var mBackground: Drawable? = null
    private var mColor: Int? = null
    private var mBlurRadius: Int? = null
    private var mCornerRadius: ArrayList<Float?>? = null

    init {
        findMethod(this.javaClass, true)
        { name == "dispatchAttachedToWindow" }.hookAfter {
            val viewRootImplMethod = this.getMethodByClassOrObject("getViewRootImpl")
            val viewRootImpl = viewRootImplMethod.invoke(this)
            val drawable = viewRootImpl?.callMethod("createBackgroundBlurDrawable")
            mBackground = drawable as? Drawable
            mColor?.let { mBackground?.callMethod("setColor", it) }
            mBlurRadius?.let { mBackground?.callMethod("setBlurRadius", it) }
            mCornerRadius?.let {
                if (it.size == 1) {
                    mBackground?.callMethod("setCornerRadius", it[0])
                } else {
                    mBackground?.callMethod("setCornerRadius", it[0], it[1], it[2], it[3])
                }
            }
            background = mBackground
        }
    }

    override fun getBackground(): Drawable? {
        return mBackground
    }

    fun setColor(i: Int?) {
        mBackground?.callMethod("setColor", i).isNull { mColor = i }
    }

    fun setBlurRadius(i: Int?) {
        mBackground?.callMethod("setBlurRadius", i).isNull { mBlurRadius = i }
    }

    fun setCornerRadius(f: Float?) {
        mBackground?.callMethod("setCornerRadius", f).isNull { f?.let { mCornerRadius = arrayListOf(f) }.isNull { mCornerRadius = null } }
    }

    fun setCornerRadius(f: Float?, f1: Float?, f2: Float?, f3: Float?) {
        mBackground?.callMethod("setCornerRadius", f, f1, f2, f3).isNull { f?.let { mCornerRadius = arrayListOf(f, f1, f2, f3) }.isNull { mCornerRadius = null } }
    }

    private fun Any?.isNull(block: () -> Unit) {
        if (this == null) block()
    }
}