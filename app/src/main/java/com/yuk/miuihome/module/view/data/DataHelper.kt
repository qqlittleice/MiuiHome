package com.yuk.miuihome.module.view.data

import android.app.Activity
import android.graphics.Color
import com.yuk.miuihome.R
import com.yuk.miuihome.XposedInit
import com.yuk.miuihome.module.BuildWithEverything
import com.yuk.miuihome.utils.Config
import com.yuk.miuihome.utils.OwnSP
import kotlin.system.exitProcess

object DataHelper {
    var isMenu = false
    var currentActivity: Activity? = null

    fun getItems(): ArrayList<Item> = if (isMenu) loadMenuItems() else loadItems()

    private fun loadMenuItems(): ArrayList<Item> {
        val itemList = arrayListOf<Item>()
        itemList.apply {
            add(Item(Text(resId = R.string.Reboot, onClickListener = { exitProcess(0) }), null))
        }
        return itemList
    }

    private fun loadItems(): ArrayList<Item> {
        val itemList = arrayListOf<Item>()
        itemList.apply {
            if (OwnSP.ownSP.getBoolean("simpleAnimation", false)) {
                add(Item(Text(resId = R.string.SimpleWarn, textColor = Color.parseColor("#ff0c0c"), textSize = 4.5f), null))
            }
            add(Item(Text(resId = R.string.BaseFeature, isTitle = true), null))
            if (!OwnSP.ownSP.getBoolean("simpleAnimation", false)) {
                add(Item(Text(resId = R.string.SmoothAnimation), Switch("smoothAnimation")))
                add(Item(Text(resId = R.string.TaskViewBlurLevel, onClickListener = {}), null)) // TODO Fix Dialog
            }
            add(Item(Text(resId = R.string.AnimationLevel, onClickListener = {}), null)) // TODO Fix Dialog

            add(Item(Text(resId = R.string.AdvancedFeature, isTitle = true), null))
            add(Item(Text(resId = R.string.UnlockGrids), Switch("unlockGrids")))
            add(Item(Text(resId = R.string.ShowDockIconTitles), Switch("showDockIconTitles")))
            add(Item(Text(resId = R.string.HideStatusBar), Switch("hideStatusBar")))
            add(Item(Text(resId = R.string.MamlDownload), Switch("mamlDownload")))
            add(Item(Text(resId = R.string.UnlockIcons), Switch("unlockIcons")))
            if (!OwnSP.ownSP.getBoolean("simpleAnimation", false)) {
                add(Item(Text(resId = R.string.WallpaperDarken), Switch("wallpaperDarken")))
            }
            add(Item(Text(resId = R.string.CategoryHideAll), Switch("categoryHideAll")))
            add(Item(Text(resId = R.string.CategoryPagingHideEdit), Switch("CategoryPagingHideEdit")))
            add(Item(Text(resId = R.string.IconTitleFontSize, onClickListener = {}), null)) // TODO Fix Dialog
            add(Item(Text(resId = R.string.CustomTitleColor, onClickListener = {}), null)) // TODO Fix Dialog
            add(Item(Text(resId = R.string.RoundCorner, onClickListener = {}), null)) // TODO Fix Dialog
            add(Item(Text(resId = R.string.AppTextSize, onClickListener = {}), null)) // TODO Fix Dialog
            add(Item(Text(resId = R.string.VerticalTaskViewOfAppCardSize, onClickListener = {}), null)) // TODO Fix Dialog
            add(Item(Text(resId = R.string.HorizontalTaskViewOfAppCardSize, onClickListener = {}), null)) // TODO Fix Dialog

            add(Item(Text(resId = R.string.Folder, isTitle = true), null))
            if (!OwnSP.ownSP.getBoolean("simpleAnimation", false)) {
                add(Item(Text(resId = R.string.BlurWhenOpenFolder), Switch("blurWhenOpenFolder")))
            }
            add(Item(Text(resId = R.string.CloseFolder), Switch("closeFolder")))
            add(Item(Text(resId = R.string.FolderWidth), Switch("folderWidth")))
            add(Item(Text(resId = R.string.FolderColumnsCount, onClickListener = {}), null)) // TODO Fix Dialog

            if (XposedInit().checkWidgetLauncher()) {
                add(Item(Text(resId = R.string.Widget, isTitle = true), null))
                add(Item(Text(resId = R.string.HideWidgetTitles), Switch("hideWidgetTitles")))
                add(Item(Text(resId = R.string.WidgetToMinus), Switch("widgetToMinus")))
                add(Item(Text(resId = R.string.AlwaysShowMIUIWidget), Switch("alwaysShowMIUIWidget")))
            }

            if (XposedInit.hasHookPackageResources) {
                add(Item(Text(resId = R.string.ResourceHooks, isTitle = true), null))
                add(Item(Text(resId = R.string.HideTaskViewAppIcon), Switch("buttonPadding")))
                add(Item(Text(resId = R.string.HideTaskViewCleanUpIcon), Switch("cleanUp")))
                add(Item(Text(resId = R.string.HideTaskViewSmallWindowIcon), Switch("smallWindow")))
                add(Item(Text(resId = R.string.TaskViewAppCardTextSize, onClickListener = {}), null)) // TODO Fix Dialog
                add(Item(Text(resId = R.string.CustomRecentText, onClickListener = {}), null)) // TODO Fix Dialog
            }

            add(Item(Text(resId = R.string.TestFeature, isTitle = true), null))
            add(Item(Text(resId = R.string.SimpleAnimation), Switch("simpleAnimation", onCheckedChangeListener = { _, _ -> currentActivity?.recreate() })))
            add(Item(Text(resId = R.string.AppReturnAmin), Switch("appReturnAmin", onCheckedChangeListener = { _, _ -> currentActivity?.recreate()})))
            add(Item(Text(resId = R.string.InfiniteScroll), Switch("infiniteScroll")))
            add(Item(Text(resId = R.string.RecommendServer), Switch("recommendServer")))
            add(Item(Text(resId = R.string.HideSeekPoints), Switch("hideSeekPoints")))
            add(Item(Text(resId = R.string.SmallWindow), Switch("supportSmallWindow")))
            add(Item(Text(resId = R.string.LowEndAnim), Switch("lowEndAnim")))
            add(Item(Text(resId = R.string.LowEndDeviceUseMIUIWidgets), Switch("useMIUIWidgets")))
            if (!OwnSP.ownSP.getBoolean("appReturnAmin", false)) {
                add(Item(Text(resId = R.string.BlurRadius, onClickListener = {}), null)) // TODO Fix Dialog
            }

            add(Item(Text(resId = R.string.OtherFeature, isTitle = true), null))
            add(Item(Text(resId = R.string.AlwaysShowStatusBarClock), Switch("clockGadget")))
            add(Item(Text(resId = R.string.DoubleTap), Switch("doubleTap")))
            if (!OwnSP.ownSP.getBoolean("dockSettings", false) && (Config.AndroidSDK == 30)) {
                add(Item(Text(resId = R.string.SearchBarBlur), Switch("searchBarBlur")))
            }
            add(Item(Text(resId = R.string.DockSettings, onClickListener = {}), null)) // TODO Fix Dialog
            add(Item(Text(resId = R.string.EveryThingBuild, onClickListener = { BuildWithEverything().init() }), null))

            add(Item(Text(resId = R.string.BrokenFeature, isTitle = true), null))
            add(Item(Text(resId = R.string.RealTaskViewHorizontal), Switch("horizontal")))
            add(Item(Text(resId = R.string.EnableIconShadow), Switch("isEnableIconShadow")))

            add(Item(Text(resId = R.string.ModuleFeature, isTitle = true), null))
            add(Item(Text(resId = R.string.CleanModuleSettings, onClickListener = {}), null)) // TODO Fix Dialog
        }
        return itemList
    }

}