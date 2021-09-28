package com.yuk.miuihome.view

import android.content.Context
import android.view.View
import android.widget.TextView
import com.github.kyuubiran.ezxhelper.init.InitFields
import com.yuk.miuihome.utils.dip2px
import com.yuk.miuihome.utils.sp2px

class SettingText(context: Context) : TextView(context) {
    var size: Float
        get() = textSize
        set(value) {
            textSize = value
        }

    init {
        setPadding(dip2px(5), dip2px(5), dip2px(5), dip2px(5))
    }


    class Builder(private val mContext: Context = InitFields.appContext, private val mText: String, private val mSize: Float? = null, private val mOnClickListener: ((View) -> Unit)? = null) {
        fun build() = SettingText(mContext).apply {
            text = mText
            mSize?.let { size = it }
            mOnClickListener?.let { setOnClickListener(it) }
        }
    }

    companion object {
        val titleSize = sp2px(5f)
    }
}