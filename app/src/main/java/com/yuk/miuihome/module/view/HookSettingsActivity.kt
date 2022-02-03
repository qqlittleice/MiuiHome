package com.yuk.miuihome.module.view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuk.miuihome.R
import com.yuk.miuihome.module.view.adapter.ItemAdapter
import com.yuk.miuihome.module.view.data.DataHelper
import com.yuk.miuihome.module.view.data.Item

class HookSettingsActivity: TransferActivity() {

    private val itemList = arrayListOf<Item>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter
    private lateinit var back: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        DataHelper.currentActivity = this
        itemList.addAll(DataHelper.getItems())
        initBack()
        recyclerView = findViewById(R.id.settings_recycler)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = ItemAdapter(itemList)
        recyclerView.adapter = adapter
    }

    private fun initBack() {
        back = findViewById(R.id.settings_back)
        back.setOnClickListener {
            if (DataHelper.thisItems == DataHelper.main) DataHelper.currentActivity.finish()
            else DataHelper.setItems(DataHelper.main)
        }
    }

    override fun onBackPressed() {
        if (DataHelper.thisItems != DataHelper.main) DataHelper.setItems(DataHelper.main)
        else super.onBackPressed()
    }

}