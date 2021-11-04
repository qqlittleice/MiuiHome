package com.yuk.miuihome.module

import android.view.View
import com.yuk.miuihome.utils.OwnSP.ownSP
import com.yuk.miuihome.utils.ktx.callMethod
import com.yuk.miuihome.utils.ktx.findClass
import com.yuk.miuihome.utils.ktx.getObjectField
import com.yuk.miuihome.utils.ktx.hookAfterMethod


class ModifyCategoryHideAll {

    fun init() {
        if (ownSP.getBoolean("categoryHideAll", false)) {
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
        if (ownSP.getBoolean("CategoryPagingHideEdit", false)) {
            "com.miui.home.launcher.allapps.AllAppsGridAdapter"
                .hookAfterMethod(
                    "onBindViewHolder",
                    "com.miui.home.launcher.allapps.AllAppsGridAdapter.ViewHolder".findClass(),
                    Int::class.javaPrimitiveType
                ) {
                    if ((it.args[0].callMethod("getItemViewType") as Int) == 64)
                        (it.args[0].getObjectField("itemView") as View).visibility = View.INVISIBLE
                }
        }
    }
}