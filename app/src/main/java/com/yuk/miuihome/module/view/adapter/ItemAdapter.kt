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
        val settingsTitle: TextView = view.findViewById(R.id.settings_text)
        val settingSwitch: SettingsSwitch = view.findViewById(R.id.settings_switch)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.settingsTitle.text = item.text
        if (item.switchKey.isNotEmpty()) {
            holder.settingSwitch.setCustomCheckedChangeListener(item.onSwitchCheckedChangeListener)
            holder.settingSwitch.key = item.switchKey
            holder.settingSwitch.visibility = View.VISIBLE
        }
        if (item.isTitle) {
            holder.settingsTitle.setTextColor(Color.parseColor("#ffb8c6"))
            holder.settingsTitle.textSize = sp2px(holder.settingsTitle.context, 5f)
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