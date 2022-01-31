package com.yuk.miuihome.module.view.base

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import com.yuk.miuihome.R
import com.yuk.miuihome.module.view.data.LayoutPair
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.dp2px
import com.yuk.miuihome.utils.ktx.sp2px

class TextWithSeekBarV(private val textV: TextV, val key: String, private val min: Int, private val max: Int, val divide: Int = 1, private val defaultProgress: Int, val callBacks: ((Int, TextView) -> Unit)? = null): BaseView() {

    override var outside = true

    override fun getType(): BaseView = this

    override fun create(context: Context): View {
        val minText = TextV(min.toString(), textSize = sp2px(context, 4.5f)).create(context)
        val maxText = TextV(max.toString(), textSize = sp2px(context, 4.5f)).create(context)
        val mutableText = TextV("", textSize = sp2px(context, 4.5f)).create(context)
        val seekBar = SeekBar(context).also { view ->
            view.thumb = null
            view.maxHeight = dp2px(context, 30f)
            view.minHeight = dp2px(context, 30f)
            view.isIndeterminate = false
            view.progressDrawable = context.getDrawable(R.drawable.seekbar_progress_drawable)
            view.indeterminateDrawable = context.getDrawable(R.color.colorAccent)
            view.min = min
            view.max = max
            OwnSP.ownSP.getFloat(key, -2333f).let {
                if (it != -2333f) {
                    view.progress = (it * divide).toInt()
                    (mutableText as TextView).text = view.progress.toString()
                } else {
                    view.progress = defaultProgress
                    (mutableText as TextView).text = defaultProgress.toString()
                }
            }
            view.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    OwnSP.ownSP.edit().run {
                        (mutableText as TextView).text = p1.toString()
                        putFloat(key, p1.toFloat() / divide)
                        apply()
                    }
                }
                override fun onStartTrackingTouch(p0: SeekBar?) {}
                override fun onStopTrackingTouch(p0: SeekBar?) {}
            })
        }
        return LinearContainerV(
            LinearContainerV.VERTICAL,
            arrayOf(
                LayoutPair(textV.create(context).also { it.setPadding(dp2px(context, 25f), 0, dp2px(context, 25f), dp2px(context, 10f)) }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)),
                LayoutPair(seekBar.also { it.setPadding(dp2px(context, 25f), 0, dp2px(context, 25f), 0) }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)),
                LayoutPair(LinearContainerV(LinearContainerV.HORIZONTAL, arrayOf(LayoutPair(minText, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)), LayoutPair(mutableText.also { it.textAlignment = TextView.TEXT_ALIGNMENT_CENTER }, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)), LayoutPair(maxText.also { it.textAlignment = TextView.TEXT_ALIGNMENT_VIEW_END }, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)))).create(context).also { it.setPadding(dp2px(context, 25f), dp2px(context, 5f), dp2px(context, 25f), dp2px(context, 5f)) }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
            )
        ).create(context)
    }
}