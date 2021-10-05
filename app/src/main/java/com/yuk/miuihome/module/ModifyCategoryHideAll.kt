package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.hookAfterMethod


class ModifyCategoryHideAll {

    fun init() {
        if (OwnSP.ownSP.getBoolean("categoryHideAll", false)) {
            "com.miui.home.launcher.allapps.category.AllAppsCategoryListContainer".hookAfterMethod(
                "buildSortCategoryList"
            ) {
                val list = it.result as ArrayList<*>
                if (list.size > 1) {
                    list.removeAt(0)
                    it.result = list
                }
            }
        }
    }
}