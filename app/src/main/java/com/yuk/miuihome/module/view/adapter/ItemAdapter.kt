package com.yuk.miuihome.module.view.adapter

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yuk.miuihome.R
import com.yuk.miuihome.module.view.SettingsSwitch
import com.yuk.miuihome.module.view.data.Item
import com.yuk.miuihome.utils.OwnSP

class ItemAdapter(private val itemList: List<Item>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private val editor by lazy { OwnSP.ownSP.edit() }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val settingsText: TextView = view.findViewById(R.id.settings_text)
        val settingSwitch: SettingsSwitch = view.findViewById(R.id.settings_switch)
        val settingsCustomView: LinearLayout = view.findViewById(R.id.settings_custom_view)
        val settingLine: View = view.findViewById(R.id.settings_line)
        val settingSeekBar: SeekBar = view.findViewById(R.id.settings_seekbar)
        val rightArrow: ImageView = view.findViewById(R.id.RightArrow)
        val layout: LinearLayout = view.findViewById(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        val textInfo = item.text
        val switchInfo = item.switch
        val seekBarInfo = item.seekBar
        val customItems = item.customItems
        val context = holder.settingsText.context

        textInfo?.let {
            textInfo.text?.let { holder.settingsText.text = it }
            textInfo.resId?.let { holder.settingsText.setText(it) }
            textInfo.textSize?.let { holder.settingsText.textSize = sp2px(context, it) }
            textInfo.textColor?.let { holder.settingsText.setTextColor(it) }
            textInfo.onClickListener?.let { holder.layout.setOnClickListener(it) }
            if (textInfo.showArrow && switchInfo == null && seekBarInfo == null) {
                holder.rightArrow.visibility = View.VISIBLE
            }
            if (textInfo.isTitle) {
                holder.settingsText.textSize = sp2px(context, 4.5f)
                holder.settingsText.setTextColor(Color.parseColor("#9399b3"))
            }
            holder.settingsText.visibility = View.VISIBLE
        }

        switchInfo?.let {
            switchInfo.onCheckedChangeListener?.let { holder.settingSwitch.customCheckedChangeListener = it }
            if (!switchInfo.key.isNullOrEmpty()) {
                holder.settingSwitch.key = switchInfo.key
                holder.settingSwitch.visibility = View.VISIBLE
            }
        }

        for (view: View in customItems) {
            holder.settingsCustomView.addView(view)
        }

        seekBarInfo?.let {
            seekBarInfo.callBacks?.let {
                holder.settingSeekBar.setOnSeekBarChangeListener(
                    object : SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                            it(progress, holder.settingsText)
                            editor.putFloat(seekBarInfo.key, progress.toFloat() / seekBarInfo.divide)
                            editor.apply()
                        }
                        override fun onStartTrackingTouch(p0: SeekBar?) { }
                        override fun onStopTrackingTouch(p0: SeekBar?) { }
                    })
            }
            seekBarInfo.min?.let { holder.settingSeekBar.min = it }
            seekBarInfo.max?.let { holder.settingSeekBar.max = it }
            seekBarInfo.progress?.let {  holder.settingSeekBar.progress = it }
            holder.settingSeekBar.visibility = View.VISIBLE
        }

        if (item.line) {
            holder.settingLine.visibility = View.VISIBLE
        }

    }

    override fun getItemCount(): Int = itemList.size

    private fun sp2px(context: Context, spValue: Float): Float = (context.resources.displayMetrics.scaledDensity * spValue + 0.5f)

    private fun getSystemColor(context: Context): Int {
        val typedValue = TypedValue()
        val contextThemeWrapper = ContextThemeWrapper(context, android.R.style.Theme_DeviceDefault)
        contextThemeWrapper.theme.resolveAttribute(android.R.attr.colorAccent, typedValue, true)
        return typedValue.data
    }

}