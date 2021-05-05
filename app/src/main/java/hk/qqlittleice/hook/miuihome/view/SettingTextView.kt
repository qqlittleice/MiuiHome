package hk.qqlittleice.hook.miuihome.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.TextView
import hk.qqlittleice.hook.miuihome.HomeContext
import hk.qqlittleice.hook.miuihome.utils.dp2px
import hk.qqlittleice.hook.miuihome.utils.isNightMode
import hk.qqlittleice.hook.miuihome.utils.sp2px

class SettingTextView(context: Context) : TextView(context) {

    var size: Float
        get() = textSize
        set(value) {
            textSize = value
        }
    var color = if (isNightMode(getContext())) "#ffffff" else "#ffffff" // #000000
        set(value) = setTextColor(Color.parseColor(value))
    var url = ""
        set(value) = setOnClickListener { HomeContext.activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(value))) }

    init {
        setPadding(dp2px(getContext(), 10f), dp2px(getContext(), 10f), dp2px(getContext(), 10f), dp2px(getContext(), 10f))
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
        val titleSize = sp2px(HomeContext.context, 10f).toFloat()
        val textSize = sp2px(HomeContext.context, 8.5f).toFloat()
        val text2Size = sp2px(HomeContext.context, 7f).toFloat()
    }
}