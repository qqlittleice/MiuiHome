package com.yuk.miuihome.module.view.adapter

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yuk.miuihome.R
import com.yuk.miuihome.module.view.SettingsSwitch
import com.yuk.miuihome.module.view.data.Item

class ItemAdapter(private val itemList: List<Item>): RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val settingsText: TextView = view.findViewById(R.id.settings_text)
        val settingSwitch: SettingsSwitch = view.findViewById(R.id.settings_switch)
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
        val context = holder.settingsText.context

        textInfo?.let {
            textInfo.text?.let { holder.settingsText.text = it }
            textInfo.resId?.let { holder.settingsText.setText(it) }
            textInfo.textSize?.let { holder.settingsText.textSize = sp2px(context, it) }
            textInfo.textColor?.let { holder.settingsText.setTextColor(it) }
            textInfo.onClickListener?.let { holder.settingsText.setOnClickListener(it) }
            if (textInfo.isTitle) {
                holder.settingsText.textSize = sp2px(context, 5f)
                holder.settingsText.setTextColor(Color.parseColor("#9399b3"))
            }
        }

        switchInfo?.let {
            switchInfo.onCheckedChangeListener?.let { holder.settingSwitch.customCheckedChangeListener = it }
            if (! switchInfo.key.isNullOrEmpty()) {
                holder.settingSwitch.key = switchInfo.key
                holder.settingSwitch.visibility = View.VISIBLE
            }
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