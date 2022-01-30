package com.yuk.miuihome.module.view.data

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.widget.Toast
import com.yuk.miuihome.BuildConfig
import com.yuk.miuihome.R
import com.yuk.miuihome.SettingDialog
import com.yuk.miuihome.XposedInit
import com.yuk.miuihome.module.BuildWithEverything
import com.yuk.miuihome.module.ModifyBlurLevel
import com.yuk.miuihome.module.view.SettingsDialog
import com.yuk.miuihome.module.view.base.*
import com.yuk.miuihome.utils.Config
import com.yuk.miuihome.utils.LogUtil
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.sp2px
import kotlin.concurrent.thread
import kotlin.system.exitProcess

@SuppressLint("StaticFieldLeak")
object DataHelper {
    var thisItems = "Main"
    var main = "Main"
    const val menu = "Menu"
    const val dock = "Dock"
    const val horizontal = "Horizontal"
    lateinit var currentActivity: Activity

    private val editor by lazy { OwnSP.ownSP.edit() }

    fun setItems(string: String, goto: Boolean) {
        thisItems = string
        val intent = currentActivity.intent
        currentActivity.finish()
        if (goto) currentActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left)
        else currentActivity.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right)
        currentActivity.startActivity(intent)
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
            add(Item(test = arrayListOf(TitleTextV("Launcher Version"))))
            add(Item(test = arrayListOf(LineV())))
            add(Item(test = arrayListOf(TextV(XposedInit().checkVersionName()))))
            add(Item(test = arrayListOf(TitleTextV("Module Version"))))
            add(Item(test = arrayListOf(LineV())))
            add(Item(test = arrayListOf(TextV(showMiuiVersion()+ "/"+"${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})-${BuildConfig.BUILD_TYPE}"))))
        }
        return itemList
    }

    private fun loadDockItems(): ArrayList<Item> {
        val itemList = arrayListOf<Item>()
        itemList.apply {
            add(Item(test = arrayListOf(TitleTextV(resId = R.string.DockSettings))))
            add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.DockFeature), SwitchV("dockSettings")))))
            if (Config.AndroidSDK == 30)
                add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.EnableDockBlur), SwitchV("searchBarBlur")))))
            if (!XposedInit.hasHookPackageResources)
                add(Item(test = arrayListOf(TextV(resId = R.string.DockWarn, textColor = Color.parseColor("#ff0c0c"), textSize = sp2px(6f)))))
            else
                add(Item(test = arrayListOf(TextWithSeekBarV(resId = R.string.DockRoundedCorners, key = "dockRadius", min = 0, max = 50, divide = 10, defaultProgress = 25))))
            add(Item(test = arrayListOf(TextWithSeekBarV(resId = R.string.DockHeight, key = "dockHeight", min = 30, max = 150, divide = 10, defaultProgress = 79))))
            add(Item(test = arrayListOf(TextWithSeekBarV(resId = R.string.DockSide, key = "dockSide", min = 0, max = 100, divide = 10, defaultProgress = 30))))
            add(Item(test = arrayListOf(TextWithSeekBarV(resId = R.string.DockBottom, key = "dockBottom", min = 0, max = 150, divide = 10, defaultProgress = 23))))
            add(Item(test = arrayListOf(TextWithSeekBarV(resId = R.string.DockIconBottom, key = "dockIconBottom", min = 0, max = 150, divide = 10, defaultProgress = 35))))
            add(Item(test = arrayListOf(TextWithSeekBarV(resId = R.string.DockMarginTop, key = "dockMarginTop", min = 0, max = 100, divide = 10, defaultProgress = 6))))
            add(Item(test = arrayListOf(TextWithSeekBarV(resId = R.string.DockMarginBottom, key = "dockMarginBottom", min = 0, max = 200, divide = 10, defaultProgress = 110))))
        }
        return itemList
    }

    private fun loadHorizontalItems(): ArrayList<Item> {
        val itemList = arrayListOf<Item>()
        itemList.apply {
            add(Item(test = arrayListOf(TitleTextV(resId = R.string.HorizontalTaskViewOfAppCardSize))))
            add(Item(test = arrayListOf(TextWithSeekBarV(resId = R.string.VerticalScreen, key = "task_horizontal1", min = 10, max = 1500, divide = 1000, defaultProgress = 1000))))
            add(Item(test = arrayListOf(TextWithSeekBarV(resId = R.string.HorizontalScreen, key = "task_horizontal2", min = 10, max = 1500, divide = 1000, defaultProgress = 1000))))
        }
        return itemList
    }

    private fun loadItems(): ArrayList<Item> {
        val itemList = arrayListOf<Item>()
        itemList.apply {
            if (OwnSP.ownSP.getBoolean("simpleAnimation", false)) {
                add(Item(test = arrayListOf(TextV(resId = R.string.SimpleWarn, textColor = Color.parseColor("#ff0c0c")))))
            } else {
                add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.SmoothAnimation), SwitchV("smoothAnimation")))))
                //val blurLevel = arrayListOf(currentActivity.getString(R.string.CompleteBlur), currentActivity.getString(R.string.TestBlur), currentActivity.getString(R.string.BasicBlur), currentActivity.getString(R.string.SimpleBlur), currentActivity.getString(R.string.NoneBlur))
                //val blurLevel0 = arrayListOf(currentActivity.getString(R.string.CompleteBlur), currentActivity.getString(R.string.TestBlur), currentActivity.getString(R.string.SimpleBlur), currentActivity.getString(R.string.NoneBlur))
                //val dict0: HashMap<String, String> = hashMapOf()
                //dict0["CompleteBlur"] = currentActivity.getString(R.string.CompleteBlur)
                //dict0["TestBlur"] = currentActivity.getString(R.string.TestBlur)
                //dict0["BasicBlur"] = currentActivity.getString(R.string.BasicBlur)
                //dict0["SimpleBlur"] = currentActivity.getString(R.string.SimpleBlur)
                //dict0["NoneBlur"] = currentActivity.getString(R.string.NoneBlur)
                //dict0[currentActivity.getString(R.string.CompleteBlur)] = "CompleteBlur"
                //dict0[currentActivity.getString(R.string.TestBlur)] = "TestBlur"
                //dict0[currentActivity.getString(R.string.BasicBlur)] = "BasicBlur"
                //dict0[currentActivity.getString(R.string.SimpleBlur)] = "SimpleBlur"
                //dict0[currentActivity.getString(R.string.NoneBlur)] = "NoneBlur"
                if (ModifyBlurLevel.checked)
                    add(Item(test = arrayListOf(TextV(resId = R.string.TaskViewBlurLevel, onClickListener = { SettingDialog().showModifyBlurLevel() })))) //TODO Fix
                //TODO
                    //spinner = Spinner(array = blurLevel, select = dict0[OwnSP.ownSP.getString("blurLevel", "SimpleBlur")], context = currentActivity, callBacks = { editor.putString("blurLevel", dict0[it]) })))
                else
                    add(Item(test = arrayListOf(TextV(resId = R.string.TaskViewBlurLevel, onClickListener = { SettingDialog().showModifyBlurLevel() })))) //TODO Fix
                //TODO
                    //spinner = Spinner(array = blurLevel0, select = dict0[OwnSP.ownSP.getString("blurLevel", "SimpleBlur")], context = currentActivity, callBacks = { editor.putString("blurLevel", dict0[it]) })))
            }
            add(Item(test = arrayListOf(TextV(resId = R.string.AnimationLevel, onClickListener = { showAnimationLevelDialog() }))))
            add(Item(test = arrayListOf(LineV())))

            add(Item(test = arrayListOf(TitleTextV(resId = R.string.AdvancedFeature))))
            add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.UnlockGrids), SwitchV("unlockGrids")))))
            add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.ShowDockIconTitles), SwitchV("showDockIconTitles")))))
            add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.HideStatusBar), SwitchV("hideStatusBar")))))
            add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.MamlDownload), SwitchV("mamlDownload")))))
            add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.UnlockIcons), SwitchV("unlockIcons")))))
            if (!OwnSP.ownSP.getBoolean("simpleAnimation", false)) {
                add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.WallpaperDarken), SwitchV("wallpaperDarken")))))
            }
            add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.CategoryHideAll), SwitchV("categoryHideAll")))))
            add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.CategoryPagingHideEdit), SwitchV("CategoryPagingHideEdit")))))
            add(Item(test = arrayListOf(TextV(resId = R.string.IconTitleFontSize, onClickListener = { showIconTitleFontSizeDialog() }))))
            add(Item(test = arrayListOf(TextV(resId = R.string.CustomTitleColor, onClickListener = { showCustomTitleColorDialog() }))))
            add(Item(test = arrayListOf(TextV(resId = R.string.RoundCorner, onClickListener = { showRoundCornerDialog() }))))
            add(Item(test = arrayListOf(TextV(resId = R.string.AppTextSize, onClickListener = { showAppTextSizeDialog() }))))
            add(Item(test = arrayListOf(TextV(resId = R.string.VerticalTaskViewOfAppCardSize, onClickListener = { showVerticalTaskViewOfAppCardSizeDialog() }))))
            add(Item(test = arrayListOf(TextV(resId = R.string.HorizontalTaskViewOfAppCardSize, onClickListener = { setItems(horizontal,true) }))))  // SettingDialog().showModifyHorizontal()
            add(Item(test = arrayListOf(LineV())))

            add(Item(test = arrayListOf(TitleTextV(resId = R.string.Folder))))
            if (!OwnSP.ownSP.getBoolean("simpleAnimation", false)) {
                add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.BlurWhenOpenFolder), SwitchV("blurWhenOpenFolder")))))
            }
            add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.CloseFolder), SwitchV("closeFolder")))))
            add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.FolderWidth), SwitchV("folderWidth")))))
            add(Item(test = arrayListOf(TextV(resId = R.string.FolderColumnsCount, onClickListener = { showFolderColumnsCountDialog() }))))
            add(Item(test = arrayListOf(LineV())))

            if (XposedInit().checkWidgetLauncher()) {
                add(Item(test = arrayListOf(TitleTextV(resId = R.string.Widget))))
                add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.HideWidgetTitles), SwitchV("hideWidgetTitles")))))
                add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.WidgetToMinus), SwitchV("widgetToMinus")))))
                add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.AlwaysShowMIUIWidget), SwitchV("alwaysShowMIUIWidget")))))
                add(Item(test = arrayListOf(LineV())))
            }

            if (XposedInit.hasHookPackageResources) {
                add(Item(test = arrayListOf(TitleTextV(resId = R.string.ResourceHooks))))
                add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.HideTaskViewAppIcon), SwitchV("buttonPadding")))))
                add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.HideTaskViewCleanUpIcon), SwitchV("cleanUp")))))
                add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.HideTaskViewSmallWindowIcon), SwitchV("smallWindow")))))
                add(Item(test = arrayListOf(TextV(resId = R.string.TaskViewAppCardTextSize, onClickListener = { showTaskViewAppCardTextSizeDialog() }))))
                add(Item(test = arrayListOf(TextV(resId = R.string.CustomRecentText, onClickListener = { showCustomRecentTextDialog() }))))
                add(Item(test = arrayListOf(LineV())))
            }

            add(Item(test = arrayListOf(TitleTextV(resId = R.string.TestFeature))))
            add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.SimpleAnimation), SwitchV("simpleAnimation", customOnCheckedChangeListener =  { _, _ -> currentActivity.recreate() }))))) //To Do Fix
            add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.AppReturnAmin), SwitchV("appReturnAmin", customOnCheckedChangeListener =  { _, _ -> currentActivity.recreate() })))))
            add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.InfiniteScroll), SwitchV("infiniteScroll")))))
            add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.RecommendServer), SwitchV("recommendServer")))))
            add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.HideSeekPoints), SwitchV("hideSeekPoints")))))
            add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.SmallWindow), SwitchV("supportSmallWindow")))))
            add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.LowEndAnim), SwitchV("lowEndAnim")))))
            add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.LowEndDeviceUseMIUIWidgets), SwitchV("useMIUIWidgets")))))
            if (!OwnSP.ownSP.getBoolean("appReturnAmin", false))
                add(Item(test = arrayListOf(TextV(resId = R.string.BlurRadius, onClickListener = { showBlurRadiusDialog() }))))
            add(Item(test = arrayListOf(LineV())))

            add(Item(test = arrayListOf(TitleTextV(resId = R.string.OtherFeature))))
            add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.AlwaysShowStatusBarClock), SwitchV("clockGadget")))))
            add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.DoubleTap), SwitchV("doubleTap")))))
            if (!OwnSP.ownSP.getBoolean("dockSettings", false) && (Config.AndroidSDK == 30))
                add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.SearchBarBlur), SwitchV("searchBarBlur")))))
            add(Item(test = arrayListOf(TextV(resId = R.string.DockSettings, onClickListener = { setItems(dock,true) }))))  // SettingDialog().showDockDialog()
            add(Item(test = arrayListOf(TextV(resId = R.string.EveryThingBuild, onClickListener = { BuildWithEverything().init() }))))
            add(Item(test = arrayListOf(LineV())))

            add(Item(test = arrayListOf(TitleTextV(resId = R.string.BrokenFeature))))
            add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.RealTaskViewHorizontal), SwitchV("horizontal")))))
            add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.EnableIconShadow), SwitchV("isEnableIconShadow")))))
            add(Item(test = arrayListOf(LineV())))

            add(Item(test = arrayListOf(TitleTextV(resId = R.string.ModuleFeature))))
            add(Item(test = arrayListOf(TextV(resId = R.string.CleanModuleSettings, onClickListener = { showCleanModuleSettingsDialog() }))))
            add(Item(test = arrayListOf(TextV(resId = R.string.Reboot, onClickListener = { showRestartDialog() }))))
            add(Item(test = arrayListOf(TextV(resId = R.string.About, onClickListener = { setItems(menu, true) }))))

            add(Item(test = arrayListOf(LineV())))
            add(Item(test = arrayListOf(TitleTextV("Test Title"))))
            add(Item(test = arrayListOf(TextV("Test Function", onClickListener = { Toast.makeText(currentActivity, "Test Toast", Toast.LENGTH_SHORT).show() }))))
            add(Item(test = arrayListOf(TextWithSeekBarV("Test Seekbar", key = "testSeekBar", min = 0, max = 100, divide = 1, defaultProgress = 0))))
            add(Item(test = arrayListOf(TextWithSwitchV(TextV("Test Switch"), SwitchV("testSwitch")))))

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
                OwnSP.set("isFirstUse",false)
                OwnSP.set("animationLevel", 1.0f)
                OwnSP.set("dockRadius", 2.5f)
                OwnSP.set("dockHeight", 7.9f)
                OwnSP.set("dockSide", 3.0f)
                OwnSP.set("dockBottom", 2.3f)
                OwnSP.set("dockIconBottom", 3.5f)
                OwnSP.set("dockMarginTop", 0.6f)
                OwnSP.set("dockMarginBottom", 11.0f)
                OwnSP.set("task_horizontal1", 1.0f)
                OwnSP.set("task_horizontal2", 1.0f)
                OwnSP.set("folderColumns", 3)
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
                OwnSP.set("isFirstUse",false)
                OwnSP.set("animationLevel", 1.0f)
                OwnSP.set("dockRadius", 2.5f)
                OwnSP.set("dockHeight", 7.9f)
                OwnSP.set("dockSide", 3.0f)
                OwnSP.set("dockBottom", 2.3f)
                OwnSP.set("dockIconBottom", 3.5f)
                OwnSP.set("dockMarginTop", 0.6f)
                OwnSP.set("dockMarginBottom", 11.0f)
                OwnSP.set("task_horizontal1", 1.0f)
                OwnSP.set("task_horizontal2", 1.0f)
                OwnSP.set("folderColumns", 3)
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
            setEditText("", "当前为: ${OwnSP.ownSP.getString("iconTitleFontColor", "").toString()}")
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
            setEditText("", "当前为: ${OwnSP.ownSP.getString("recentText", "").toString()}")
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
            setEditText("", "当前值: ${OwnSP.ownSP.getInt("backgroundTextSize", 13).toString()}")
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
            setEditText("", "当前值: ${OwnSP.ownSP.getFloat("animationLevel", 1f).toString()}")
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
            setEditText("", "当前值: ${OwnSP.ownSP.getFloat("recents_task_view_rounded_corners_radius", 20f).toString()}")
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
            setEditText("", "当前值: ${OwnSP.ownSP.getFloat("recents_task_view_header_height", 40f).toString()}")
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
            setEditText("", "当前值: ${OwnSP.ownSP.getFloat("task_vertical", 100f).toString()}")
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
            setEditText("", "当前值: ${OwnSP.ownSP.getFloat("iconTitleFontSize", 1f).toString()}")
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
            setEditText("", "当前值: ${OwnSP.ownSP.getInt("folderColumns", 3).toString()}")
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
            setEditText("", "当前值: ${OwnSP.ownSP.getFloat("blurRadius", 1f).toString()}")
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