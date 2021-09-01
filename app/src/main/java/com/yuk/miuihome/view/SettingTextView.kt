package com.yuk.miuihome.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.TextView
import com.yuk.miuihome.HomeContext
import com.yuk.miuihome.utils.dp2px
import com.yuk.miuihome.utils.isNightMode
import com.yuk.miuihome.utils.sp2px

class SettingTextView(context: Context) : TextView(context) {
    private var mContext: Context? = null

    var size: Float
        get() = textSize
        set(value) {
            textSize = value
        }
    var color = if (isNightMode(getContext())) "#ffffff" else "#000000"
        set(value) = setTextColor(Color.parseColor(value))
    var url = ""
        set(value) = setOnClickListener { HomeContext.activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(value))) }

    init {
        setPadding(
            dp2px(getContext(), 10f),
            dp2px(getContext(), 10f),
            dp2px(getContext(), 10f),
            dp2px(getContext(), 5f))
        setTextColor(Color.parseColor(color))
    }

    class Builder(private val mContext: Context = HomeContext.context, private val block: SettingTextView.() -> Unit) {
        fun build() = SettingTextView(mContext).apply(block)
    }

    class FastBuilder(private val mContext: Context = HomeContext.context, private val mText: String, private val mSize: Float? = null, private val mColor: String? = null, private val mUrl: String? = null, private val mOnClickListener: ((View) -> Unit)? = null) {
        fun build() = SettingTextView(mContext).apply {
            text = mText
            mSize?.let { size = it }
            mColor?.let { color = it }
            mUrl?.let { url = it }
            mOnClickListener?.let { setOnClickListener(it) }
        }
    }

    companion object {
        val titleSize = sp2px(HomeContext.context, 9f).toFloat()
        val textSize = sp2px(HomeContext.context, 6f).toFloat()
        val text2Size = sp2px(HomeContext.context, 7f).toFloat()
    }
}