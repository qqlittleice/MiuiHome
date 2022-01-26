package com.yuk.miuihome.module.view.data

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.widget.EditText
import com.yuk.miuihome.BuildConfig
import com.yuk.miuihome.R
import com.yuk.miuihome.XposedInit
import com.yuk.miuihome.module.BuildWithEverything
import com.yuk.miuihome.module.view.SettingsDialog
import com.yuk.miuihome.utils.Config
import com.yuk.miuihome.utils.LogUtil
import com.yuk.miuihome.utils.OwnSP
import kotlin.concurrent.thread
import kotlin.system.exitProcess

@SuppressLint("StaticFieldLeak")
object DataHelper {
    var isMenu = false
    lateinit var currentActivity: Activity

    fun getItems(): ArrayList<Item> = if (isMenu) loadMenuItems() else loadItems()

    private fun loadMenuItems(): ArrayList<Item> {
        val itemList = arrayListOf<Item>()
        itemList.apply {
            add(Item(Text("Launcher Version", isTitle = true), null, line = true))
            add(Item(Text(XposedInit().checkVersionName()), null))
            add(Item(Text("Module Version", isTitle = true), null, line = true))
            add(Item(Text(showMiuiVersion()+ "/"+"${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})-${BuildConfig.BUILD_TYPE}"), null))
        }
        return itemList
    }

    private fun loadItems(): ArrayList<Item> {
        val itemList = arrayListOf<Item>()
        itemList.apply {
            if (OwnSP.ownSP.getBoolean("simpleAnimation", false)) {
                add(Item(Text(resId = R.string.SimpleWarn, textColor = Color.parseColor("#ff0c0c"), textSize = 4.5f), null))
            }
            if (!OwnSP.ownSP.getBoolean("simpleAnimation", false)) {
                add(Item(Text(resId = R.string.SmoothAnimation), Switch("smoothAnimation")))
                add(Item(Text(resId = R.string.TaskViewBlurLevel, onClickListener = {}), null))
            }
            add(Item(Text(resId = R.string.AnimationLevel, onClickListener = {}), null, line = true)) // TODO Fix Dialog

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
            add(Item(Text(resId = R.string.IconTitleFontSize), null, seekBar = SeekBar(0, 30, 1, OwnSP.ownSP.getFloat("iconTitleFontSize", -1f).toInt(), "iconTitleFontSize")))
            add(Item(Text(resId = R.string.CustomTitleColor, onClickListener = {}), null)) // TODO Fix Dialog
            add(Item(Text(resId = R.string.RoundCorner, onClickListener = {}), null)) // TODO Fix Dialog
            add(Item(Text(resId = R.string.AppTextSize, onClickListener = {}), null)) // TODO Fix Dialog
            add(Item(Text(resId = R.string.VerticalTaskViewOfAppCardSize, onClickListener = {}), null)) // TODO Fix Dialog
            add(Item(Text(resId = R.string.HorizontalTaskViewOfAppCardSize, onClickListener = {}), null, line = true)) // TODO Fix Dialog

            add(Item(Text(resId = R.string.Folder, isTitle = true), null))
            if (!OwnSP.ownSP.getBoolean("simpleAnimation", false)) {
                add(Item(Text(resId = R.string.BlurWhenOpenFolder), Switch("blurWhenOpenFolder")))
            }
            add(Item(Text(resId = R.string.CloseFolder), Switch("closeFolder")))
            add(Item(Text(resId = R.string.FolderWidth), Switch("folderWidth")))
            add(Item(Text(resId = R.string.FolderColumnsCount, onClickListener = {}), null, line = true)) // TODO Fix Dialog

            if (XposedInit().checkWidgetLauncher()) {
                add(Item(Text(resId = R.string.Widget, isTitle = true), null))
                add(Item(Text(resId = R.string.HideWidgetTitles), Switch("hideWidgetTitles")))
                add(Item(Text(resId = R.string.WidgetToMinus), Switch("widgetToMinus")))
                add(Item(Text(resId = R.string.AlwaysShowMIUIWidget), Switch("alwaysShowMIUIWidget"), line = true))
            }

            if (XposedInit.hasHookPackageResources) {
                add(Item(Text(resId = R.string.ResourceHooks, isTitle = true), null))
                add(Item(Text(resId = R.string.HideTaskViewAppIcon), Switch("buttonPadding")))
                add(Item(Text(resId = R.string.HideTaskViewCleanUpIcon), Switch("cleanUp")))
                add(Item(Text(resId = R.string.HideTaskViewSmallWindowIcon), Switch("smallWindow")))
                add(Item(Text(resId = R.string.TaskViewAppCardTextSize, onClickListener = {}), null)) // TODO Fix Dialog
                add(Item(Text(resId = R.string.CustomRecentText, onClickListener = {}), null, line = true)) // TODO Fix Dialog
            }

            add(Item(Text(resId = R.string.TestFeature, isTitle = true), null))
            add(Item(Text(resId = R.string.SimpleAnimation), Switch("simpleAnimation", onCheckedChangeListener = { _, _ -> currentActivity.recreate() })))
            add(Item(Text(resId = R.string.AppReturnAmin), Switch("appReturnAmin", onCheckedChangeListener = { _, _ -> currentActivity.recreate() })))
            add(Item(Text(resId = R.string.InfiniteScroll), Switch("infiniteScroll")))
            add(Item(Text(resId = R.string.RecommendServer), Switch("recommendServer")))
            add(Item(Text(resId = R.string.HideSeekPoints), Switch("hideSeekPoints")))
            add(Item(Text(resId = R.string.SmallWindow), Switch("supportSmallWindow")))
            add(Item(Text(resId = R.string.LowEndAnim), Switch("lowEndAnim")))
            add(Item(Text(resId = R.string.LowEndDeviceUseMIUIWidgets), Switch("useMIUIWidgets")))
            if (!OwnSP.ownSP.getBoolean("appReturnAmin", false)) {
                add(Item(Text(resId = R.string.BlurRadius, onClickListener = {}), null)) // TODO Fix Dialog
            }
            add(Item(line = true))

            add(Item(Text(resId = R.string.OtherFeature, isTitle = true), null))
            add(Item(Text(resId = R.string.AlwaysShowStatusBarClock), Switch("clockGadget")))
            add(Item(Text(resId = R.string.DoubleTap), Switch("doubleTap")))
            if (!OwnSP.ownSP.getBoolean("dockSettings", false) && (Config.AndroidSDK == 30)) {
                add(Item(Text(resId = R.string.SearchBarBlur), Switch("searchBarBlur")))
            }
            add(Item(Text(resId = R.string.DockSettings, onClickListener = {}), null)) // TODO Fix Dialog
            add(Item(Text(resId = R.string.EveryThingBuild, onClickListener = { BuildWithEverything().init() }), null, line = true))

            add(Item(Text(resId = R.string.BrokenFeature, isTitle = true), null))
            add(Item(Text(resId = R.string.RealTaskViewHorizontal), Switch("horizontal")))
            add(Item(Text(resId = R.string.EnableIconShadow), Switch("isEnableIconShadow"), line = true))

            add(Item(Text(resId = R.string.ModuleFeature, isTitle = true), null))
            add(Item(Text(resId = R.string.CleanModuleSettings, onClickListener = {}), null)) // TODO Fix Dialog
            add(Item(Text(resId = R.string.Reboot, onClickListener = { showRestartDialog() }), null))
        }
        return itemList
    }

    private fun showMiuiVersion():String {
        lateinit var value: String
        when (XposedInit().checkMiuiVersion()) {
            "V130" -> value = "MIUI 13"
            "V125" -> value = "MIUI 12.5"
            "V12" -> value = "MIUI 12"
            "V11" -> value = "MIUI 11"
            "V10" -> value = "MIUI 10"
        }
        return value
    }

    private fun showRestartDialog() {
        SettingsDialog(currentActivity).apply {
            setTitle(XposedInit.moduleRes.getString(R.string.Reboot))
            setMessage(XposedInit.moduleRes.getString(R.string.Reboot1))
            setRButton(XposedInit.moduleRes.getString(R.string.Yes)) {
                thread {
                    LogUtil.toast(XposedInit.moduleRes.getString(R.string.Reboot2))
                    Thread.sleep(1000)
                    exitProcess(0)
                }
                dismiss()
            }
            setLButton(XposedInit.moduleRes.getString(R.string.Cancel)) { dismiss() }
            show()
        }
    }

    private fun showModifyTitleColorDialog() {
        SettingsDialog(currentActivity).apply {
            setTitle(XposedInit.moduleRes.getString(R.string.CustomTitleColor))
            setMessage(XposedInit.moduleRes.getString(R.string.Tips4))
            setEditText(OwnSP.ownSP.getString("iconTitleFontColor", "").toString(), "#FFFFFF")
            setRButton(XposedInit.moduleRes.getString(R.string.Yes)) {
                dismiss()
            }
            setLButton(XposedInit.moduleRes.getString(R.string.Cancel)) { dismiss() }
            show()
        }
    }
}