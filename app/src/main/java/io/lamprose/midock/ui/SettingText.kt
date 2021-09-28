package io.lamprose.midock.ui

import android.content.Context
import android.view.View
import android.widget.TextView
import com.github.kyuubiran.ezxhelper.init.InitFields
import io.lamprose.midock.Utils

class SettingText(context: Context) : TextView(context) {
    var size: Float
        get() = textSize
        set(value) {
            textSize = value
        }

    init {
        setPadding(Utils.dip2px(5), Utils.dip2px(5), Utils.dip2px(5), Utils.dip2px(5))
    }


    class Builder(private val mContext: Context = InitFields.appContext, private val mText: String, private val mSize: Float? = null, private val mOnClickListener: ((View) -> Unit)? = null) {
        fun build() = SettingText(mContext).apply {
            text = mText
            mSize?.let { size = it }
            mOnClickListener?.let { setOnClickListener(it) }
        }
    }

    companion object {
        val titleSize = Utils.sp2px(5f)
    }
}