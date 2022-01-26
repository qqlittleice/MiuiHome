package com.yuk.miuihome.module.view.data

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import com.yuk.miuihome.BuildConfig
import com.yuk.miuihome.R
import com.yuk.miuihome.SettingDialog
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
    private val editor by lazy { OwnSP.ownSP.edit() }
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
                add(Item(Text(resId = R.string.TaskViewBlurLevel, onClickListener = { SettingDialog().showModifyBlurLevel() }), null)) // TODO Fix Dialog
            }
            add(Item(Text(resId = R.string.AnimationLevel, onClickListener = { showAnimationLevelDialog() }), null, line = true))

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
            add(Item(Text(resId = R.string.IconTitleFontSize, onClickListener = { showIconTitleFontSizeDialog() }), null))
            add(Item(Text(resId = R.string.CustomTitleColor, onClickListener = { showCustomTitleColorDialog() }), null))
            add(Item(Text(resId = R.string.RoundCorner, onClickListener = { showRoundCornerDialog() }), null))
            add(Item(Text(resId = R.string.AppTextSize, onClickListener = { showAppTextSizeDialog() }), null))
            add(Item(Text(resId = R.string.VerticalTaskViewOfAppCardSize, onClickListener = { showVerticalTaskViewOfAppCardSizeDialog() }), null))
            add(Item(Text(resId = R.string.HorizontalTaskViewOfAppCardSize, onClickListener = { SettingDialog().showModifyHorizontal() }), null, line = true)) // TODO Fix Dialog

            add(Item(Text(resId = R.string.Folder, isTitle = true), null))
            if (!OwnSP.ownSP.getBoolean("simpleAnimation", false)) {
                add(Item(Text(resId = R.string.BlurWhenOpenFolder), Switch("blurWhenOpenFolder")))
            }
            add(Item(Text(resId = R.string.CloseFolder), Switch("closeFolder")))
            add(Item(Text(resId = R.string.FolderWidth), Switch("folderWidth")))
            add(Item(Text(resId = R.string.FolderColumnsCount, onClickListener = { showFolderColumnsCountDialog() }), null, line = true))

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
                add(Item(Text(resId = R.string.TaskViewAppCardTextSize, onClickListener = { showTaskViewAppCardTextSizeDialog() }), null))
                add(Item(Text(resId = R.string.CustomRecentText, onClickListener = { showCustomRecentTextDialog() }), null, line = true))
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
                add(Item(Text(resId = R.string.BlurRadius, onClickListener = { showBlurRadiusDialog() }), null))
            }
            add(Item(line = true))

            add(Item(Text(resId = R.string.OtherFeature, isTitle = true), null))
            add(Item(Text(resId = R.string.AlwaysShowStatusBarClock), Switch("clockGadget")))
            add(Item(Text(resId = R.string.DoubleTap), Switch("doubleTap")))
            if (!OwnSP.ownSP.getBoolean("dockSettings", false) && (Config.AndroidSDK == 30)) {
                add(Item(Text(resId = R.string.SearchBarBlur), Switch("searchBarBlur")))
            }
            add(Item(Text(resId = R.string.DockSettings, onClickListener = { SettingDialog().showDockDialog() }), null)) // TODO Fix Dialog
            add(Item(Text(resId = R.string.EveryThingBuild, onClickListener = { BuildWithEverything().init() }), null, line = true))

            add(Item(Text(resId = R.string.BrokenFeature, isTitle = true), null))
            add(Item(Text(resId = R.string.RealTaskViewHorizontal), Switch("horizontal")))
            add(Item(Text(resId = R.string.EnableIconShadow), Switch("isEnableIconShadow"), line = true))

            add(Item(Text(resId = R.string.ModuleFeature, isTitle = true), null))
            add(Item(Text(resId = R.string.CleanModuleSettings, onClickListener = { showCleanModuleSettingsDialog() }), null))
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
                dismiss()
                thread {
                    LogUtil.toast(XposedInit.moduleRes.getString(R.string.Reboot2))
                    Thread.sleep(1000)
                    exitProcess(0)
                }
            }
            setLButton(XposedInit.moduleRes.getString(R.string.Cancel)) { dismiss() }
            show()
        }
    }

    private fun showCleanModuleSettingsDialog() {
        SettingsDialog(currentActivity).apply {
            setTitle(XposedInit.moduleRes.getString(R.string.CleanModuleSettings))
            setMessage(XposedInit.moduleRes.getString(R.string.Tips2))
            setRButton(XposedInit.moduleRes.getString(R.string.Yes)) {
                OwnSP.clear()
                OwnSP.set("animationLevel", 1.25f)
                OwnSP.set("dockRadius", 2.5f)
                OwnSP.set("dockHeight", 7.9f)
                OwnSP.set("dockSide", 3.0f)
                OwnSP.set("dockBottom", 2.3f)
                OwnSP.set("dockIconBottom", 3.5f)
                OwnSP.set("dockMarginTop", 0.6f)
                OwnSP.set("dockMarginBottom", 11.0f)
                OwnSP.set("folderColumns", 3f)
                OwnSP.set("task_horizontal1", 1.0f)
                OwnSP.set("task_horizontal2", 1.0f)
                dismiss()
                thread {
                    LogUtil.toast(XposedInit.moduleRes.getString(R.string.Reboot2))
                    Thread.sleep(1000)
                    exitProcess(0)
                }
            }
            setLButton(XposedInit.moduleRes.getString(R.string.Cancel)) { dismiss() }
            show()
        }
    }

    private fun showFirstUseDialog() {
        SettingsDialog(currentActivity).apply {
            setTitle(XposedInit.moduleRes.getString(R.string.Welcome))
            setMessage(XposedInit.moduleRes.getString(R.string.Tips))
            setRButton(XposedInit.moduleRes.getString(R.string.Yes)) {
                OwnSP.clear()
                OwnSP.set("animationLevel", 1.25f)
                OwnSP.set("dockRadius", 2.5f)
                OwnSP.set("dockHeight", 7.9f)
                OwnSP.set("dockSide", 3.0f)
                OwnSP.set("dockBottom", 2.3f)
                OwnSP.set("dockIconBottom", 3.5f)
                OwnSP.set("dockMarginTop", 0.6f)
                OwnSP.set("dockMarginBottom", 11.0f)
                OwnSP.set("folderColumns", 3f)
                OwnSP.set("task_horizontal1", 1.0f)
                OwnSP.set("task_horizontal2", 1.0f)
                dismiss()
                thread {
                    LogUtil.toast(XposedInit.moduleRes.getString(R.string.Reboot2))
                    Thread.sleep(1000)
                    exitProcess(0)
                }
            }
            setLButton(XposedInit.moduleRes.getString(R.string.Cancel)) { dismiss() }
            show()
        }
    }

    private fun showCustomTitleColorDialog() {
        SettingsDialog(currentActivity).apply {
            setTitle(XposedInit.moduleRes.getString(R.string.CustomTitleColor))
            setMessage(XposedInit.moduleRes.getString(R.string.Tips4)+ ", 留空为恢复默认")
            setEditText("", "  当前为: ${OwnSP.ownSP.getString("iconTitleFontColor", "").toString()}")
            setRButton(XposedInit.moduleRes.getString(R.string.Yes)) {
                editor.putString("iconTitleFontColor", getEditText())
                editor.apply()
                dismiss()
            }
            setLButton(XposedInit.moduleRes.getString(R.string.Cancel)) { dismiss() }
            show()
        }
    }


    private fun showCustomRecentTextDialog() {
        SettingsDialog(currentActivity).apply {
            setTitle(XposedInit.moduleRes.getString(R.string.CustomRecentText))
            setMessage("留空为恢复默认, 键入空格表示移除文本")
            setEditText("", "  当前为: ${OwnSP.ownSP.getString("recentText", "").toString()}")
            setRButton(XposedInit.moduleRes.getString(R.string.Yes)) {
                editor.putString("recentText", getEditText())
                editor.apply()
                dismiss()
            }
            setLButton(XposedInit.moduleRes.getString(R.string.Cancel)) { dismiss() }
            show()
        }
    }

    private fun showTaskViewAppCardTextSizeDialog() {
        SettingsDialog(currentActivity).apply {
            setTitle(XposedInit.moduleRes.getString(R.string.TaskViewAppCardTextSize))
            setMessage("默认值: 13, 推荐值：0-30, 留空为恢复默认")
            setEditText("", "  当前值: ${OwnSP.ownSP.getInt("backgroundTextSize", 13).toString()}")
            setRButton(XposedInit.moduleRes.getString(R.string.Yes)) {
                if (getEditText() == "") editor.putInt("backgroundTextSize", 13)
                else editor.putInt("backgroundTextSize", getEditText().toInt())
                editor.apply()
                dismiss()
            }
            setLButton(XposedInit.moduleRes.getString(R.string.Cancel)) { dismiss() }
            show()
        }
    }


    private fun showAnimationLevelDialog() {
        SettingsDialog(currentActivity).apply {
            setTitle(XposedInit.moduleRes.getString(R.string.AnimationLevel))
            setMessage("默认值: 1.0, 推荐值: 0.1-5.0, 留空为恢复默认")
            setEditText("", "  当前值: ${OwnSP.ownSP.getFloat("animationLevel", 1f).toString()}")
            setRButton(XposedInit.moduleRes.getString(R.string.Yes)) {
                if (getEditText() == "") editor.putFloat("animationLevel", 1f)
                else editor.putFloat("animationLevel", getEditText().toFloat())
                editor.apply()
                dismiss()
            }
            setLButton(XposedInit.moduleRes.getString(R.string.Cancel)) { dismiss() }
            show()
        }
    }

    private fun showRoundCornerDialog() {
        SettingsDialog(currentActivity).apply {
            setTitle(XposedInit.moduleRes.getString(R.string.RoundCorner))
            setMessage("默认值: 20.0, 推荐值: 0.0-50.0, 留空为恢复默认")
            setEditText("", "  当前值: ${OwnSP.ownSP.getFloat("recents_task_view_rounded_corners_radius", 20f).toString()}")
            setRButton(XposedInit.moduleRes.getString(R.string.Yes)) {
                if (getEditText() == "") editor.putFloat("recents_task_view_rounded_corners_radius", 20f)
                else editor.putFloat("recents_task_view_rounded_corners_radius", getEditText().toFloat())
                editor.apply()
                dismiss()
            }
            setLButton(XposedInit.moduleRes.getString(R.string.Cancel)) { dismiss() }
            show()
        }
    }

    private fun showAppTextSizeDialog() {
        SettingsDialog(currentActivity).apply {
            setTitle(XposedInit.moduleRes.getString(R.string.AppTextSize))
            setMessage("默认值: 40.0, 推荐值: 0.0-100.0, 留空为恢复默认")
            setEditText("", "  当前值: ${OwnSP.ownSP.getFloat("recents_task_view_header_height", 40f).toString()}")
            setRButton(XposedInit.moduleRes.getString(R.string.Yes)) {
                if (getEditText() == "") editor.putFloat("recents_task_view_header_height", 40f)
                else editor.putFloat("recents_task_view_header_height", getEditText().toFloat())
                editor.apply()
                dismiss()
            }
            setLButton(XposedInit.moduleRes.getString(R.string.Cancel)) { dismiss() }
            show()
        }
    }

    private fun showVerticalTaskViewOfAppCardSizeDialog() {
        SettingsDialog(currentActivity).apply {
            setTitle(XposedInit.moduleRes.getString(R.string.VerticalTaskViewOfAppCardSize))
            setMessage("默认值: 100.0, 推荐值: 50.0-150.0, 留空为恢复默认")
            setEditText("", "  当前值: ${OwnSP.ownSP.getFloat("task_vertical", 100f).toString()}")
            setRButton(XposedInit.moduleRes.getString(R.string.Yes)) {
                if (getEditText() == "") editor.putFloat("task_vertical", 100f)
                else editor.putFloat("task_vertical", getEditText().toFloat())
                editor.apply()
                dismiss()
            }
            setLButton(XposedInit.moduleRes.getString(R.string.Cancel)) { dismiss() }
            show()
        }
    }

    private fun showIconTitleFontSizeDialog() {
        SettingsDialog(currentActivity).apply {
            setTitle(XposedInit.moduleRes.getString(R.string.IconTitleFontSize))
            setMessage("默认值: 1.0, 推荐值: 0.0-10.0, 留空为恢复默认")
            setEditText("", "  当前值: ${OwnSP.ownSP.getFloat("iconTitleFontSize", 1f).toString()}")
            setRButton(XposedInit.moduleRes.getString(R.string.Yes)) {
                if (getEditText() == "") editor.putFloat("iconTitleFontSize", 1f)
                else editor.putFloat("iconTitleFontSize", getEditText().toFloat())
                editor.apply()
                dismiss()
            }
            setLButton(XposedInit.moduleRes.getString(R.string.Cancel)) { dismiss() }
            show()
        }
    }

    private fun showFolderColumnsCountDialog() {
        SettingsDialog(currentActivity).apply {
            setTitle(XposedInit.moduleRes.getString(R.string.FolderColumnsCount))
            setMessage("默认值: 3, 推荐值: 1-6, 留空为恢复默认")
            setEditText("", "  当前值: ${OwnSP.ownSP.getInt("folderColumns", 3).toString()}")
            setRButton(XposedInit.moduleRes.getString(R.string.Yes)) {
                if (getEditText() == "") editor.putInt("folderColumns", 3)
                else editor.putInt("folderColumns", getEditText().toInt())
                editor.apply()
                dismiss()
            }
            setLButton(XposedInit.moduleRes.getString(R.string.Cancel)) { dismiss() }
            show()
        }
    }

    private fun showBlurRadiusDialog() {
        SettingsDialog(currentActivity).apply {
            setTitle(XposedInit.moduleRes.getString(R.string.BlurRadius))
            setMessage("默认值: 1.0, 推荐值: 0.0-2.0, 留空为恢复默认")
            setEditText("", "  当前值: ${OwnSP.ownSP.getFloat("blurRadius", 1f).toString()}")
            setRButton(XposedInit.moduleRes.getString(R.string.Yes)) {
                if (getEditText() == "") editor.putFloat("blurRadius", 1f)
                else editor.putFloat("blurRadius", getEditText().toFloat())
                editor.apply()
                dismiss()
            }
            setLButton(XposedInit.moduleRes.getString(R.string.Cancel)) { dismiss() }
            show()
        }
    }
}