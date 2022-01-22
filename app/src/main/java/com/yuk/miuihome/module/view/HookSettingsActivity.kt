package com.yuk.miuihome.module.view

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuk.miuihome.R
import com.yuk.miuihome.module.view.adapter.ItemAdapter
import com.yuk.miuihome.module.view.data.Item

class HookSettingsActivity: TransferActivity() {

    private val itemList = arrayListOf<Item>()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        recyclerView = findViewById(R.id.settings_recycler)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val adapter = ItemAdapter(itemList)
        recyclerView.adapter = adapter

    }

    init {
        itemList.add(Item("标题", isTitle = true))
        itemList.add(Item("测试功能", "testView"))
        itemList.add(Item("测试功能2", "testView2"))
        itemList.add(Item("测试功能3", "testView3"))

        itemList.add(Item("标题2", isTitle = true))
        itemList.add(Item("测试功能4", "testView4"))
        itemList.add(Item("测试功能5", "testView5"))
        itemList.add(Item("测试功能6", "testView6"))
        itemList.add(Item("测试功能7", "testView7"))

        itemList.add(Item("标题3", isTitle = true))
        itemList.add(Item("测试功能8", "testView8"))
        itemList.add(Item("测试功能9", "testView9"))

        itemList.add(Item("标题4", isTitle = true))
        itemList.add(Item("只有Text"))
        itemList.add(Item("只有Text2"))
        itemList.add(Item("只有Text3"))
        itemList.add(Item("只有Text4"))

    }

}