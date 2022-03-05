package com.yuk.miuihome.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yuk.miuihome.R
import com.yuk.miuihome.utils.ViewBuilder
import com.yuk.miuihome.view.base.BaseView
import com.yuk.miuihome.view.data.Item

class ItemAdapter(private val itemList: List<Item>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val settingsBaseLinearLayout: LinearLayout = view.findViewById(R.id.settings_linear_base)
        val settingsText: TextView = view.findViewById(R.id.settings_text)
        val settingsCustomView: LinearLayout = view.findViewById(R.id.settings_custom_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    @SuppressLint("RtlHardcoded", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        val customItems = item.customItems
        val testItems = item.list
        val context = holder.settingsText.context

        for (view: View in customItems) holder.settingsCustomView.addView(view)
        for (view: BaseView in testItems) {
            if (view.outside) {
                ViewBuilder.build(context, view)
                    ?.let { holder.settingsBaseLinearLayout.addView(it) }
            } else {
                ViewBuilder.build(context, view)?.let { holder.settingsCustomView.addView(it) }
            }
        }
    }

    override fun getItemCount(): Int = itemList.size

    private fun getSystemColor(context: Context): Int {
        val typedValue = TypedValue()
        val contextThemeWrapper = ContextThemeWrapper(context, android.R.style.Theme_DeviceDefault)
        contextThemeWrapper.theme.resolveAttribute(android.R.attr.colorAccent, typedValue, true)
        return typedValue.data
    }

}