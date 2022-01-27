package com.yuk.miuihome.module.view.base

import android.content.Context
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import com.yuk.miuihome.R
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.dp2px

class SeekBarV(val key: String = "", val min: Int, val max: Int, val divide: Int = 1, val defaultProgress: Int, val callBacks: ((Int, TextView) -> Unit)? = null): BaseView() {

    override var outside = true

    override fun getType(): BaseView = this

    override fun create(context: Context): View {
        return SeekBar(context).also { view ->
            view.thumb = null
            view.maxHeight = dp2px(context, 35f)
            view.minHeight = dp2px(context, 35f)
            view.isIndeterminate = false
            view.progressDrawable = context.getDrawable(R.drawable.seekbar_progress_drawable)
            view.indeterminateDrawable = context.getDrawable(R.color.colorAccent)
            view.min = min
            view.max = max
            OwnSP.ownSP.getFloat(key, -2333f).let {
                if (it != -2333f) view.progress = it.toInt() else view.progress = defaultProgress
            }
            view.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    OwnSP.ownSP.edit().run {
                        putFloat(key, p1.toFloat() / divide)
                        apply()
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {}
                override fun onStopTrackingTouch(p0: SeekBar?) {}
            })
        }
    }
}