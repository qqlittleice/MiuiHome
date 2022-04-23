package com.yuk.miuihome.module

import android.view.View
import com.yuk.miuihome.XposedInit
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.callMethod
import com.yuk.miuihome.utils.ktx.findClass
import com.yuk.miuihome.utils.ktx.getObjectField
import com.yuk.miuihome.utils.ktx.hookAfterMethod

class ModifyCategory {

    fun init() {
        if (OwnSP.ownSP.getBoolean("categoryHideAll", false)) {
            val cla = if (XposedInit().checkVersionCode() >= 427004483L) "com.miui.home.launcher.allapps.category.BaseAllAppsCategoryListContainer" else "com.miui.home.launcher.allapps.category.AllAppsCategoryListContainer"
            cla.hookAfterMethod("buildSortCategoryList"
                ) {
                    val list = it.result as ArrayList<*>
                    if (list.size > 1) {
                        list.removeAt(0)
                        it.result = list
                    }
                }
        }
        if (OwnSP.ownSP.getBoolean("CategoryPagingHideEdit", false))
            "com.miui.home.launcher.allapps.AllAppsGridAdapter".hookAfterMethod("onBindViewHolder", "com.miui.home.launcher.allapps.AllAppsGridAdapter.ViewHolder".findClass(), Int::class.javaPrimitiveType
            ) {
                if ((it.args[0].callMethod("getItemViewType") as Int) == 64)
                        (it.args[0].getObjectField("itemView") as View).visibility = View.INVISIBLE
            }
    }
}