package com.yuk.miuihome.module.view

import android.os.Bundle
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
    private lateinit var menu: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemList.addAll(DataHelper.getItems())
        setContentView(R.layout.settings_activity)
        DataHelper.currentActivity = this
        initMenu()
        recyclerView = findViewById(R.id.settings_recycler)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = ItemAdapter(itemList)

        recyclerView.adapter = adapter

    }

    private fun initMenu() {
        menu = findViewById(R.id.settings_menu)
        menu.setOnClickListener {
            DataHelper.isMenu = !DataHelper.isMenu
            recreate()
        }
        menu.setImageResource(if (DataHelper.isMenu) R.drawable.abc_ic_ab_back_material else R.drawable.abc_ic_menu_overflow_material)
    }

}