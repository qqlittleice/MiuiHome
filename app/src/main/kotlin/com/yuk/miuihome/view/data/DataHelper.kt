package com.yuk.miuihome.view.data

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import com.github.kyuubiran.ezxhelper.init.InitFields.moduleRes
import com.github.kyuubiran.ezxhelper.utils.Log
import com.yuk.miuihome.BuildConfig
import com.yuk.miuihome.R
import com.yuk.miuihome.XposedInit
import com.yuk.miuihome.module.BuildWithEverything
import com.yuk.miuihome.module.ModifyBlurLevel
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.view.CustomDialog
import com.yuk.miuihome.view.HookSettingsActivity
import com.yuk.miuihome.view.base.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread
import kotlin.system.exitProcess

@SuppressLint("StaticFieldLeak")
object DataHelper {
    var thisItems = "Main"
    const val main = "Main"
    const val dock = "Dock"
    const val horizontal = "Horizontal"
    private const val menu = "Menu"
    lateinit var currentActivity: Activity
    private val editor by lazy { OwnSP.ownSP.edit() }

    fun setItems(string: String) {
        thisItems = string
        val intent = Intent(XposedInit.context, HookSettingsActivity::class.java)
        this.currentActivity.finish()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        XposedInit.context?.startActivity(intent)
    }

    fun getItems(): ArrayList<Item> = when (thisItems) {
        menu -> loadMenuItems()
        dock -> loadDockItems()
        horizontal -> loadHorizontalItems()
        else -> loadItems()
    }

    private fun loadMenuItems(): ArrayList<Item> {
        val itemList = arrayListOf<Item>()
        itemList.apply {
            add(Item(list = arrayListOf(SubtitleV(resId = R.string.MiuiVersion))))
            add(Item(list = arrayListOf(TextV( "Android " + XposedInit().checkAndroidVersion()+ " / MIUI " + XposedInit().checkMiuiVersion()))))
            add(Item(list = arrayListOf(LineV())))
            add(Item(list = arrayListOf(SubtitleV(resId = R.string.LauncherVersion))))
            add(Item(list = arrayListOf(TextV(XposedInit().checkVersionName()))))
            add(Item(list = arrayListOf(LineV())))
            add(Item(list = arrayListOf(SubtitleV(resId = R.string.ModuleVersion))))
            add(Item(list = arrayListOf(TextV("${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})-${BuildConfig.BUILD_TYPE}"))))
        }
        return itemList
    }
    private fun loadDockItems(): ArrayList<Item> {
        val itemList = arrayListOf<Item>()
        itemList.apply {
            add(Item(list = arrayListOf(SubtitleV(resId = R.string.DockSettings))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.DockFeature), "dockSettings"))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.EnableDockBlur), "searchBarBlur"))))
            add(Item(list = arrayListOf(TextWithSeekBarV(TextV(resId = R.string.DockRoundedCorners), key = "dockRadius", min = 0, max = 50, divide = 10, defaultProgress = 25))))
            add(Item(list = arrayListOf(TextWithSeekBarV(TextV(resId = R.string.DockHeight), key = "dockHeight", min = 30, max = 150, divide = 10, defaultProgress = 79))))
            add(Item(list = arrayListOf(TextWithSeekBarV(TextV(resId = R.string.DockSide), key = "dockSide", min = 0, max = 100, divide = 10, defaultProgress = 30))))
            add(Item(list = arrayListOf(TextWithSeekBarV(TextV(resId = R.string.DockBottom), key = "dockBottom", min = 0, max = 150, divide = 10, defaultProgress = 23))))
            add(Item(list = arrayListOf(TextWithSeekBarV(TextV(resId = R.string.DockIconBottom), key = "dockIconBottom", min = 0, max = 150, divide = 10, defaultProgress = 35))))
            add(Item(list = arrayListOf(TextWithSeekBarV(TextV(resId = R.string.DockMarginTop), key = "dockMarginTop", min = 0, max = 100, divide = 10, defaultProgress = 6))))
            add(Item(list = arrayListOf(TextWithSeekBarV(TextV(resId = R.string.DockMarginBottom), key = "dockMarginBottom", min = 0, max = 200, divide = 10, defaultProgress = 110))))
            add(Item(list = arrayListOf(TextV(resId = R.string.Reset, onClickListener = { showResetDockDialog() }))))
        }
        return itemList
    }

    private fun loadHorizontalItems(): ArrayList<Item> {
        val itemList = arrayListOf<Item>()
        itemList.apply {
            add(Item(list = arrayListOf(SubtitleV(resId = R.string.HorizontalTaskViewOfAppCardSize))))
            add(Item(list = arrayListOf(TextWithSeekBarV(TextV(resId = R.string.VerticalScreen), key = "task_horizontal1", min = 500, max = 1500, divide = 1000, defaultProgress = 1000))))
            add(Item(list = arrayListOf(TextWithSeekBarV(TextV(resId = R.string.HorizontalScreen), key = "task_horizontal2", min = 500, max = 1500, divide = 1000, defaultProgress = 1000))))
            add(Item(list = arrayListOf(TextV(resId = R.string.Reset2, onClickListener = { showResetHorizontalDialog() }))))
        }
        return itemList
    }

    @SuppressLint("SimpleDateFormat")
    private fun loadItems(): ArrayList<Item> {
        val itemList = arrayListOf<Item>()
        itemList.apply {
            add(Item(list = arrayListOf(TextV(resId = R.string.app_name, typeface = Typeface.create(null, 300, false), textSize = 30f))))

            if (OwnSP.ownSP.getBoolean("simpleAnimation", false)) {
                add(Item(list = arrayListOf(TextV(resId = R.string.SimpleWarn, textColor = Color.parseColor("#ff0c0c")))))
            } else {
                if (XposedInit().checkVersionCode() < 427004733L) add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.SmoothAnimation), "smoothAnimation"))))
                val blurLevel = arrayListOf(currentActivity.getString(R.string.CompleteBlur), currentActivity.getString(R.string.TestBlur), currentActivity.getString(R.string.BasicBlur), currentActivity.getString(R.string.SimpleBlur), currentActivity.getString(R.string.NoneBlur))
                val blurLevel0 = arrayListOf(currentActivity.getString(R.string.CompleteBlur), currentActivity.getString(R.string.TestBlur), currentActivity.getString(R.string.SimpleBlur), currentActivity.getString(R.string.NoneBlur))
                val dict0: HashMap<String, String> = hashMapOf()
                dict0["CompleteBlur"] = currentActivity.getString(R.string.CompleteBlur)
                dict0["TestBlur"] = currentActivity.getString(R.string.TestBlur)
                dict0["BasicBlur"] = currentActivity.getString(R.string.BasicBlur)
                dict0["SimpleBlur"] = currentActivity.getString(R.string.SimpleBlur)
                dict0["NoneBlur"] = currentActivity.getString(R.string.NoneBlur)
                dict0[currentActivity.getString(R.string.CompleteBlur)] = "CompleteBlur"
                dict0[currentActivity.getString(R.string.TestBlur)] = "TestBlur"
                dict0[currentActivity.getString(R.string.BasicBlur)] = "BasicBlur"
                dict0[currentActivity.getString(R.string.SimpleBlur)] = "SimpleBlur"
                dict0[currentActivity.getString(R.string.NoneBlur)] = "NoneBlur"
                if (!OwnSP.ownSP.getBoolean("alwaysBlur", false)) {
                    if (ModifyBlurLevel.checked) add(Item(list = arrayListOf(TextWithSpinnerV(TextV(resId = R.string.TaskViewBlurLevel), select = dict0[OwnSP.ownSP.getString("blurLevel", "SimpleBlur")], array = blurLevel, callBacks = { OwnSP.set("blurLevel", dict0[it].toString()) }))))
                    else add(Item(list = arrayListOf(TextWithSpinnerV(TextV(resId = R.string.TaskViewBlurLevel), select = dict0[OwnSP.ownSP.getString("blurLevel", "SimpleBlur")], array = blurLevel0, callBacks = { OwnSP.set("blurLevel", dict0[it].toString()) }))))
                }
            }
            add(Item(list = arrayListOf(TextV(resId = R.string.AnimationLevel, onClickListener = { showAnimationLevelDialog() }))))
            add(Item(list = arrayListOf(LineV())))

            add(Item(list = arrayListOf(SubtitleV(resId = R.string.AdvancedFeature))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.UnlockGrids), "unlockGrids", customOnCheckedChangeListener =  { _, _ -> currentActivity.recreate() }))))
            if (XposedInit().checkVersionCode() == 427004546L && OwnSP.ownSP.getBoolean("unlockGrids", false)) add(Item(list = arrayListOf(TextV(resId = R.string.DrawerModeCount, onClickListener = { showDrawerModeCountDialog() }))))
            if (!XposedInit().checkIsPadDevice()) add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.ShowDockIconTitles), "showDockIconTitles"))))
            if (!XposedInit().checkIsPadDevice()) add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.HideTaskViewAppIcon), "recentIcon"))))
            if (!XposedInit().checkIsPadDevice()) add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.HideTaskViewCleanUpIcon), "cleanUp"))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.HideStatusBar), "hideStatusBar"))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.MamlDownload), "mamlDownload"))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.UnlockIcons), "unlockIcons"))))
            if (!OwnSP.ownSP.getBoolean("simpleAnimation", false)) add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.WallpaperDarken), "wallpaperDarken"))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.HideTaskViewSmallWindowIcon), "smallWindow"))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.CategoryHideAll), "categoryHideAll"))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.CategoryPagingHideEdit), "CategoryPagingHideEdit"))))
            add(Item(list = arrayListOf(TextV(resId = R.string.CustomTitleColor, onClickListener = { showCustomTitleColorDialog() }))))
            add(Item(list = arrayListOf(TextV(resId = R.string.IconTitleTopMargin, onClickListener = { showModifyIconTitleTopMarginDialog() }))))
            add(Item(list = arrayListOf(TextV(resId = R.string.IconTitleFontSize, onClickListener = { showIconTitleFontSizeDialog() }))))
            add(Item(list = arrayListOf(TextV(resId = R.string.TaskViewAppCardTextSize, onClickListener = { showTaskViewAppCardTextSizeDialog() }))))
            add(Item(list = arrayListOf(TextV(resId = R.string.RoundCorner, onClickListener = { showRoundCornerDialog() }))))
            add(Item(list = arrayListOf(TextV(resId = R.string.AppTextSize, onClickListener = { showAppTextSizeDialog() }))))
            add(Item(list = arrayListOf(TextV(resId = R.string.CustomRecentText, onClickListener = { showCustomRecentTextDialog() }))))
            add(Item(list = arrayListOf(TextV(resId = R.string.TaskViewAppCardBgColor, onClickListener = { showCustomAppCardBgColorDialog() }))))
            add(Item(list = arrayListOf(TextV(resId = R.string.VerticalTaskViewOfAppCardSize, onClickListener = { showVerticalTaskViewOfAppCardSizeDialog() }))))
            add(Item(list = arrayListOf(TextWithArrowV(TextWithSummaryV(titleResId = R.string.HorizontalTaskViewOfAppCardSize)) { setItems(horizontal) })))
            add(Item(list = arrayListOf(LineV())))

            add(Item(list = arrayListOf(SubtitleV(resId = R.string.Folder))))
            if (Build.VERSION.SDK_INT >= 31) add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.smallFolderBlur, summaryResId = R.string.smallFolderSummy), "folderBlur", customOnCheckedChangeListener =  { _, _ -> currentActivity.recreate() }))))
            if (Build.VERSION.SDK_INT >= 31 && OwnSP.ownSP.getBoolean("folderBlur", false)) add(Item(list = arrayListOf(TextWithSeekBarV(TextV(resId = R.string.smallFolderBlurCorner), key = "folderBlurCorner", min = 0, max = 100, divide = 1, defaultProgress = 30))))
            if (Build.VERSION.SDK_INT >= 31 && OwnSP.ownSP.getBoolean("folderBlur", false)) add(Item(list = arrayListOf(TextWithSeekBarV(TextV(resId = R.string.smallFolderBlurSide), key = "folderBlurSide", min = 50, max = 300, divide = 100, defaultProgress = 120))))
            if (!OwnSP.ownSP.getBoolean("simpleAnimation", false) && !OwnSP.ownSP.getBoolean("alwaysBlur", false)) add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.BlurWhenOpenFolder), "blurWhenOpenFolder"))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.CloseFolder), "closeFolder"))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.FolderWidth), "folderWidth"))))
            add(Item(list = arrayListOf(TextV(resId = R.string.FolderColumnsCount, onClickListener = { showFolderColumnsCountDialog() }))))
            add(Item(list = arrayListOf(LineV())))

            if (XposedInit().checkWidgetLauncher()) {
                add(Item(list = arrayListOf(SubtitleV(resId = R.string.Widget))))
                add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.HideWidgetTitles), "hideWidgetTitles"))))
                add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.WidgetToMinus), "widgetToMinus"))))
                add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.AlwaysShowMIUIWidget), "alwaysShowMIUIWidget"))))
                add(Item(list = arrayListOf(LineV())))
            }

            add(Item(list = arrayListOf(SubtitleV(resId = R.string.TestFeature))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.SimpleAnimation), "simpleAnimation", customOnCheckedChangeListener =  { _, _ -> currentActivity.recreate() }))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.AllAppsBlur), "allAppsBlur"))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.InfiniteScroll), "infiniteScroll"))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.RecommendServer), "recommendServer"))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.HideSeekPoints), "hideSeekPoints"))))
            if (!OwnSP.ownSP.getBoolean("simpleAnimation", false)) add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.AlwaysBlur), "alwaysBlur", customOnCheckedChangeListener =  { _, _ -> currentActivity.recreate() }))))
            if (!OwnSP.ownSP.getBoolean("alwaysBlur", false)) add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.AppReturnAmin), "appReturnAmin", customOnCheckedChangeListener =  { _, _ -> currentActivity.recreate() }))))
            if (OwnSP.ownSP.getBoolean("appReturnAmin", false)) add(Item(list = arrayListOf(TextWithSeekBarV(TextV(resId = R.string.appReturnAminSpend), key = "appReturnAminSpend", min = 50, max = 300, divide = 100, defaultProgress = 200))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.SmallWindow), "supportSmallWindow"))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.LowEndAnim), "lowEndAnim"))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.LowEndDeviceUseMIUIWidgets), "useMIUIWidgets"))))
            if (!OwnSP.ownSP.getBoolean("simpleAnimation", false)) add(Item(list = arrayListOf(TextV(resId = R.string.BlurRadius, onClickListener = { showBlurRadiusDialog() }))))
            add(Item(list = arrayListOf(LineV())))

            add(Item(list = arrayListOf(SubtitleV(resId = R.string.OtherFeature))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.AlwaysShowStatusBarClock), "clockGadget"))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.DoubleTap), "doubleTap"))))
            if (Build.VERSION.SDK_INT >= 31 && XposedInit().checkIsPadDevice()) add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.PadDockBlur), "PadDockBlur"))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.ShortcutCount), "unlockShortcutCount", customOnCheckedChangeListener =  { _, _ -> currentActivity.recreate() }))))
            if (OwnSP.ownSP.getBoolean("unlockShortcutCount", false)) add(Item(list = arrayListOf(TextV(resId = R.string.MaxShortcutCount, onClickListener = { showMaxShortcutCountDialog() }))))
            if (Build.VERSION.SDK_INT >= 31 && !XposedInit().checkIsPadDevice()) add(Item(list = arrayListOf(TextWithArrowV(TextWithSummaryV(titleResId = R.string.DockSettings)) { setItems(dock) })))
            add(Item(list = arrayListOf(TextV(resId = R.string.EveryThingBuild, onClickListener = { BuildWithEverything().init() }))))
            add(Item(list = arrayListOf(LineV())))

            add(Item(list = arrayListOf(SubtitleV(resId = R.string.BrokenFeature))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.RealTaskViewHorizontal), "horizontal"))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.EnableIconShadow), "isEnableIconShadow"))))
            add(Item(list = arrayListOf(LineV())))

            add(Item(list = arrayListOf(SubtitleV(resId = R.string.ModuleFeature))))
            add(Item(list = arrayListOf(TextV(resId = R.string.Reboot, onClickListener = { showRestartDialog() }))))
            add(Item(list = arrayListOf(TextWithArrowV(TextWithSummaryV(titleResId = R.string.BackupModuleSettings)) { (currentActivity as HookSettingsActivity).spBackup.also { it.requestWriteToFile("MiuiHome-Config-${SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Calendar.getInstance().time)}") } })))
            add(Item(list = arrayListOf(TextWithArrowV(TextWithSummaryV(titleResId = R.string.RestoreModuleSettings)) { showRestoreModuleSettingsDialog() })))
            add(Item(list = arrayListOf(TextV(resId = R.string.CleanModuleSettings, onClickListener = { showCleanModuleSettingsDialog() }))))
            add(Item(list = arrayListOf(TextWithArrowV(TextWithSummaryV(titleResId = R.string.Github, summaryResId = R.string.OpenGithub)) { val uri = Uri.parse("https://github.com/qqlittleice/MiuiHome"); val intent = Intent(Intent.ACTION_VIEW, uri); currentActivity.startActivity(intent) })))
            add(Item(list = arrayListOf(TextWithArrowV(TextWithSummaryV(titleResId = R.string.About, summary = "${BuildConfig.VERSION_NAME}(${BuildConfig.BUILD_TYPE})")) { setItems(menu) })))
        }
        return itemList
    }

    private fun showRestartDialog() {
        CustomDialog(currentActivity).apply {
            setTitle(R.string.Reboot)
            setMessage(R.string.Reboot1)
            setRButton(R.string.Yes) {
                dismiss()
                thread {
                    Log.toast(currentActivity.getString(R.string.Reboot2))
                    Thread.sleep(1000)
                    exitProcess(0)
                }
            }
            setLButton(R.string.Cancel) { dismiss() }
            show()
        }
    }

    private fun showResetDockDialog() {
        CustomDialog(currentActivity).apply {
            setTitle(R.string.Reset)
            setMessage(R.string.Tips1)
            setRButton(R.string.Yes) {
                OwnSP.set("dockRadius", 2.5f)
                OwnSP.set("dockHeight", 7.9f)
                OwnSP.set("dockSide", 3.0f)
                OwnSP.set("dockBottom", 2.3f)
                OwnSP.set("dockIconBottom", 3.5f)
                OwnSP.set("dockMarginTop", 0.6f)
                OwnSP.set("dockMarginBottom", 11.0f)
                dismiss()
                thread {
                    Log.toast(currentActivity.getString(R.string.Reboot2))
                    Thread.sleep(1000)
                    exitProcess(0)
                }
            }
            setLButton(R.string.Cancel) { dismiss() }
            show()
        }
    }

    private fun showCleanModuleSettingsDialog() {
        CustomDialog(currentActivity).apply {
            setTitle(R.string.CleanModuleSettings)
            setMessage(R.string.Tips2)
            setRButton(R.string.Yes) {
                OwnSP.clear()
                dismiss()
                thread {
                    Log.toast(currentActivity.getString(R.string.Reboot2))
                    Thread.sleep(1000)
                    exitProcess(0)
                }
            }
            setLButton(R.string.Cancel) { dismiss() }
            show()
        }
    }

    private fun showRestoreModuleSettingsDialog() {
        CustomDialog(currentActivity).apply {
            setTitle(R.string.RestoreModuleSettings)
            setMessage(R.string.RestoreModuleSettingsTips)
            setRButton(R.string.Yes) {
                dismiss()
                (currentActivity as HookSettingsActivity).spBackup.requestReadFromFile()
            }
            setLButton(R.string.Cancel) { dismiss() }
            show()
        }
    }

    private fun showResetHorizontalDialog() {
        CustomDialog(currentActivity).apply {
            setTitle(R.string.Reset2)
            setMessage(R.string.Tips3)
            setRButton(R.string.Yes) {
                OwnSP.set("task_horizontal1", 1.0f)
                OwnSP.set("task_horizontal2", 1.0f)
                dismiss()
                thread {
                    Log.toast(currentActivity.getString(R.string.Reboot2))
                    Thread.sleep(1000)
                    exitProcess(0)
                }
            }
            setLButton(R.string.Cancel) { dismiss() }
            show()
        }
    }

    private fun showCustomTitleColorDialog() {
        CustomDialog(currentActivity).apply {
            setTitle(R.string.CustomTitleColor)
            setMessage("${moduleRes.getString(R.string.Tips4)}, ${moduleRes.getString(R.string.setDefaults)}")
            setEditText("", "${moduleRes.getString(R.string.current)}: ${OwnSP.ownSP.getString("iconTitleFontColor", "").toString()}")
            setRButton(R.string.Yes) {
                editor.putString("iconTitleFontColor", getEditText())
                editor.apply()
                dismiss()
            }
            setLButton(R.string.Cancel) { dismiss() }
            show()
        }
    }


    private fun showCustomAppCardBgColorDialog() {
        CustomDialog(currentActivity).apply {
            setTitle(R.string.TaskViewAppCardBgColor)
            setMessage("${moduleRes.getString(R.string.Tips4)}, ${moduleRes.getString(R.string.setDefaults)}")
            setEditText("", "${moduleRes.getString(R.string.current)}: ${OwnSP.ownSP.getString("appCardBgColor", "").toString()}")
            setRButton(R.string.Yes) {
                editor.putString("appCardBgColor", getEditText())
                editor.apply()
                dismiss()
            }
            setLButton(R.string.Cancel) { dismiss() }
            show()
        }
    }


    private fun showCustomRecentTextDialog() {
        CustomDialog(currentActivity).apply {
            setTitle(R.string.CustomRecentText)
            setMessage(R.string.setRecentString)
            setEditText("", "${moduleRes.getString(R.string.current)}: ${OwnSP.ownSP.getString("recentText", "").toString()}")
            setRButton(R.string.Yes) {
                editor.putString("recentText", getEditText())
                editor.apply()
                dismiss()
            }
            setLButton(R.string.Cancel) { dismiss() }
            show()
        }
    }

    private fun showTaskViewAppCardTextSizeDialog() {
        CustomDialog(currentActivity).apply {
            setTitle(R.string.TaskViewAppCardTextSize)
            setMessage("${moduleRes.getString(R.string.Defaults)}: -1, ${moduleRes.getString(R.string.Recommend)}：-1-30, ${moduleRes.getString(R.string.setDefaults)}")
            setEditText("", "${moduleRes.getString(R.string.current)}: ${OwnSP.ownSP.getFloat("recentTextSize", -1f)}")
            setRButton(R.string.Yes) {
                if (getEditText() == "") editor.putFloat("recentTextSize", -1f)
                else editor.putFloat("recentTextSize", getEditText().toFloat())
                editor.apply()
                dismiss()
            }
            setLButton(R.string.Cancel) { dismiss() }
            show()
        }
    }

    private fun showModifyIconTitleTopMarginDialog() {
        CustomDialog(currentActivity).apply {
            setTitle(R.string.IconTitleTopMargin)
            setMessage("${moduleRes.getString(R.string.Defaults)}: -1, ${moduleRes.getString(R.string.Recommend)}：0-50, ${moduleRes.getString(R.string.setDefaults)}")
            setEditText("", "${moduleRes.getString(R.string.current)}: ${OwnSP.ownSP.getInt("titleTopMargin", -1)}")
            setRButton(R.string.Yes) {
                if (getEditText() == "") editor.putInt("titleTopMargin", -1)
                else editor.putInt("titleTopMargin", getEditText().toInt())
                editor.apply()
                dismiss()
            }
            setLButton(R.string.Cancel) { dismiss() }
            show()
        }
    }

    private fun showAnimationLevelDialog() {
        CustomDialog(currentActivity).apply {
            setTitle(R.string.AnimationLevel)
            setMessage("${moduleRes.getString(R.string.Defaults)}: 1.0, ${moduleRes.getString(R.string.Recommend)}: 0.1-5.0, ${moduleRes.getString(R.string.setDefaults)}")
            setEditText("", "${moduleRes.getString(R.string.current)}: ${OwnSP.ownSP.getFloat("animationLevel", 1f)}")
            setRButton(R.string.Yes) {
                if (getEditText() == "") editor.putFloat("animationLevel", 1f)
                else editor.putFloat("animationLevel", getEditText().toFloat())
                editor.apply()
                dismiss()
            }
            setLButton(R.string.Cancel) { dismiss() }
            show()
        }
    }

    private fun showRoundCornerDialog() {
        CustomDialog(currentActivity).apply {
            setTitle(R.string.RoundCorner)
            setMessage("${moduleRes.getString(R.string.Defaults)}: 20.0, ${moduleRes.getString(R.string.Recommend)}: 0.0-50.0, ${moduleRes.getString(R.string.setDefaults)}")
            setEditText("", "${moduleRes.getString(R.string.current)}: ${OwnSP.ownSP.getFloat("recents_task_view_rounded_corners_radius", 20f)}")
            setRButton(R.string.Yes) {
                if (getEditText() == "") editor.putFloat("recents_task_view_rounded_corners_radius", 20f)
                else editor.putFloat("recents_task_view_rounded_corners_radius", getEditText().toFloat())
                editor.apply()
                dismiss()
            }
            setLButton(R.string.Cancel) { dismiss() }
            show()
        }
    }

    private fun showAppTextSizeDialog() {
        CustomDialog(currentActivity).apply {
            setTitle(R.string.AppTextSize)
            setMessage("${moduleRes.getString(R.string.Defaults)}: 40.0, ${moduleRes.getString(R.string.Recommend)}: 0.0-100.0, ${moduleRes.getString(R.string.setDefaults)}")
            setEditText("", "${moduleRes.getString(R.string.current)}: ${OwnSP.ownSP.getFloat("recents_task_view_header_height", 40f)}")
            setRButton(R.string.Yes) {
                if (getEditText() == "") editor.putFloat("recents_task_view_header_height", 40f)
                else editor.putFloat("recents_task_view_header_height", getEditText().toFloat())
                editor.apply()
                dismiss()
            }
            setLButton(R.string.Cancel) { dismiss() }
            show()
        }
    }

    private fun showVerticalTaskViewOfAppCardSizeDialog() {
        CustomDialog(currentActivity).apply {
            setTitle(R.string.VerticalTaskViewOfAppCardSize)
            setMessage("${moduleRes.getString(R.string.Defaults)}: 1000.0, ${moduleRes.getString(R.string.Recommend)}: 500.0-1500.0, ${moduleRes.getString(R.string.setDefaults)}")
            setEditText("", "${moduleRes.getString(R.string.current)}: ${OwnSP.ownSP.getFloat("task_vertical", 1000f)}")
            setRButton(R.string.Yes) {
                if (getEditText() == "") editor.putFloat("task_vertical", 1000f)
                else editor.putFloat("task_vertical", getEditText().toFloat())
                editor.apply()
                dismiss()
            }
            setLButton(R.string.Cancel) { dismiss() }
            show()
        }
    }

    private fun showIconTitleFontSizeDialog() {
        CustomDialog(currentActivity).apply {
            setTitle(R.string.IconTitleFontSize)
            setMessage("${moduleRes.getString(R.string.Defaults)}: 12.0, ${moduleRes.getString(R.string.Recommend)}: 0.0-50.0, ${moduleRes.getString(R.string.setDefaults)}")
            setEditText("", "${moduleRes.getString(R.string.current)}: ${OwnSP.ownSP.getFloat("iconTitleFontSize", 12f)}")
            setRButton(R.string.Yes) {
                if (getEditText() == "") editor.putFloat("iconTitleFontSize", 12f)
                else editor.putFloat("iconTitleFontSize", getEditText().toFloat())
                editor.apply()
                dismiss()
            }
            setLButton(R.string.Cancel) { dismiss() }
            show()
        }
    }

    private fun showFolderColumnsCountDialog() {
        CustomDialog(currentActivity).apply {
            setTitle(R.string.FolderColumnsCount)
            setMessage("${moduleRes.getString(R.string.Defaults)}: 3, ${moduleRes.getString(R.string.Recommend)}: 1-6, ${moduleRes.getString(R.string.setDefaults)}")
            setEditText("", "${moduleRes.getString(R.string.current)}: ${OwnSP.ownSP.getInt("folderColumns", 3)}")
            setRButton(R.string.Yes) {
                if (getEditText() == "") editor.putInt("folderColumns", 3)
                else editor.putInt("folderColumns", getEditText().toInt())
                editor.apply()
                dismiss()
            }
            setLButton(R.string.Cancel) { dismiss() }
            show()
        }
    }

    private fun showMaxShortcutCountDialog() {
        CustomDialog(currentActivity).apply {
            setTitle(R.string.MaxShortcutCount)
            setMessage("${moduleRes.getString(R.string.Defaults)}: 6, ${moduleRes.getString(R.string.Recommend)}: 3-8, ${moduleRes.getString(R.string.setDefaults)}")
            setEditText("", "${moduleRes.getString(R.string.current)}: ${OwnSP.ownSP.getInt("shortcutCount", 6)}")
            setRButton(R.string.Yes) {
                if (getEditText() == "") editor.putInt("shortcutCount", 6)
                else editor.putInt("shortcutCount", getEditText().toInt())
                editor.apply()
                dismiss()
            }
            setLButton(R.string.Cancel) { dismiss() }
            show()
        }
    }

    private fun showDrawerModeCountDialog() {
        CustomDialog(currentActivity).apply {
            setTitle(R.string.DrawerModeCount)
            setMessage("${moduleRes.getString(R.string.Defaults)}: 4, ${moduleRes.getString(R.string.Recommend)}: 2-10, ${moduleRes.getString(R.string.setDefaults)}")
            setEditText("", "${moduleRes.getString(R.string.current)}: ${OwnSP.ownSP.getInt("config_cell_count_x_drawer_mode", 3)}")
            setRButton(R.string.Yes) {
                if (getEditText() == "") editor.putInt("config_cell_count_x_drawer_mode", 3)
                else editor.putInt("config_cell_count_x_drawer_mode", getEditText().toInt())
                editor.apply()
                dismiss()
            }
            setLButton(R.string.Cancel) { dismiss() }
            show()
        }
    }

    private fun showBlurRadiusDialog() {
        CustomDialog(currentActivity).apply {
            setTitle(R.string.BlurRadius)
            setMessage("${moduleRes.getString(R.string.Defaults)}: 1.0, ${moduleRes.getString(R.string.Recommend)}: 0.0-2.0, ${moduleRes.getString(R.string.setDefaults)}")
            setEditText("", "${moduleRes.getString(R.string.current)}: ${OwnSP.ownSP.getFloat("blurRadius", 1f)}")
            setRButton(R.string.Yes) {
                if (getEditText() == "") editor.putFloat("blurRadius", 1f)
                else editor.putFloat("blurRadius", getEditText().toFloat())
                editor.apply()
                dismiss()
            }
            setLButton(R.string.Cancel) { dismiss() }
            show()
        }
    }
}
