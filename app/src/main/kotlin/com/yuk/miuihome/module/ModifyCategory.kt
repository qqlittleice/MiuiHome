package com.yuk.miuihome.module

import android.view.View
import com.github.kyuubiran.ezxhelper.utils.*
import com.yuk.miuihome.XposedInit
import com.yuk.miuihome.utils.OwnSP

class ModifyCategory {

    fun init() {
        if (OwnSP.ownSP.getBoolean("categoryHideAll", false)) {
            val cla = if (XposedInit().checkVersionCode() >= 427004483L) "com.miui.home.launcher.allapps.category.BaseAllAppsCategoryListContainer" else "com.miui.home.launcher.allapps.category.AllAppsCategoryListContainer"
            findMethod(loadClass(cla)) {
                name == "buildSortCategoryList"
            }.hookAfter {
                val list = it.result as ArrayList<*>
                if (list.size > 1) {
                    list.removeAt(0)
                    it.result = list
                }
            }
        }
        if (OwnSP.ownSP.getBoolean("CategoryPagingHideEdit", false)) {
            findMethod("com.miui.home.launcher.allapps.AllAppsGridAdapter") {
                name == "onBindViewHolder" && parameterTypes[0] == loadClass("com.miui.home.launcher.allapps.AllAppsGridAdapter.ViewHolder") && parameterTypes[1] == Int::class.javaPrimitiveType
            }.hookAfter {
                if ((it.args[0].invokeMethodAuto("getItemViewType") as Int) == 64)
                    (it.args[0].getObject("itemView") as View).visibility = View.INVISIBLE
            }
        }
    }
}