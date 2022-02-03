package com.yuk.miuihome.module.view.data

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.widget.Toast
import com.yuk.miuihome.BuildConfig
import com.yuk.miuihome.R
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
    private const val menu = "Menu"
    const val dock = "Dock"
    const val horizontal = "Horizontal"
    lateinit var currentActivity: Activity

    private val editor by lazy { OwnSP.ownSP.edit() }

    fun setItems(string: String) {
        thisItems = string
        val intent = currentActivity.intent
        currentActivity.finish()
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
                add(Item(test = arrayListOf(TextWithSeekBarV(TextV(resId = R.string.DockRoundedCorners), key = "dockRadius", min = 0, max = 50, divide = 10, defaultProgress = 25))))
            add(Item(test = arrayListOf(TextWithSeekBarV(TextV(resId = R.string.DockHeight), key = "dockHeight", min = 30, max = 150, divide = 10, defaultProgress = 79))))
            add(Item(test = arrayListOf(TextWithSeekBarV(TextV(resId = R.string.DockSide), key = "dockSide", min = 0, max = 100, divide = 10, defaultProgress = 30))))
            add(Item(test = arrayListOf(TextWithSeekBarV(TextV(resId = R.string.DockBottom), key = "dockBottom", min = 0, max = 150, divide = 10, defaultProgress = 23))))
            add(Item(test = arrayListOf(TextWithSeekBarV(TextV(resId = R.string.DockIconBottom), key = "dockIconBottom", min = 0, max = 150, divide = 10, defaultProgress = 35))))
            add(Item(test = arrayListOf(TextWithSeekBarV(TextV(resId = R.string.DockMarginTop), key = "dockMarginTop", min = 0, max = 100, divide = 10, defaultProgress = 6))))
            add(Item(test = arrayListOf(TextWithSeekBarV(TextV(resId = R.string.DockMarginBottom), key = "dockMarginBottom", min = 0, max = 200, divide = 10, defaultProgress = 110))))
            add(Item(test = arrayListOf(TextV(resId = R.string.Reset, onClickListener = { showResetDockDialog() }))))
        }
        return itemList
    }

    private fun loadHorizontalItems(): ArrayList<Item> {
        val itemList = arrayListOf<Item>()
        itemList.apply {
            add(Item(test = arrayListOf(TitleTextV(resId = R.string.HorizontalTaskViewOfAppCardSize))))
            add(Item(test = arrayListOf(TextWithSeekBarV(TextV(resId = R.string.VerticalScreen), key = "task_horizontal1", min = 500, max = 1500, divide = 1000, defaultProgress = 1000))))
            add(Item(test = arrayListOf(TextWithSeekBarV(TextV(resId = R.string.HorizontalScreen), key = "task_horizontal2", min = 500, max = 1500, divide = 1000, defaultProgress = 1000))))
            add(Item(test = arrayListOf(TextV(resId = R.string.Reset2, onClickListener = { showResetHorizontalDialog() }))))
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
                if (ModifyBlurLevel.checked)
                    add(Item(test = arrayListOf(TextWithSpinnerV(TextV(resId = R.string.TaskViewBlurLevel) ,"blurLevel", select = dict0[OwnSP.ownSP.getString("blurLevel", "SimpleBlur")], array = blurLevel, callBacks = { OwnSP.set("blurLevel", dict0[it].toString()) }))))
                else
                    add(Item(test = arrayListOf(TextWithSpinnerV(TextV(resId = R.string.TaskViewBlurLevel), "blurLevel", select = dict0[OwnSP.ownSP.getString("blurLevel", "SimpleBlur")], array = blurLevel0, callBacks = { OwnSP.set("blurLevel", dict0[it].toString()) }))))
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
            add(Item(test = arrayListOf(TextWithArrowV(TextV(resId = R.string.HorizontalTaskViewOfAppCardSize), onClickListener = { setItems(horizontal) }))))  // SettingDialog().showModifyHorizontal()
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
            add(Item(test = arrayListOf(TextWithArrowV(TextV(resId = R.string.DockSettings), onClickListener = { setItems(dock) }))))
            add(Item(test = arrayListOf(TextV(resId = R.string.EveryThingBuild, onClickListener = { BuildWithEverything().init() }))))
            add(Item(test = arrayListOf(LineV())))

            add(Item(test = arrayListOf(TitleTextV(resId = R.string.BrokenFeature))))
            add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.RealTaskViewHorizontal), SwitchV("horizontal")))))
            add(Item(test = arrayListOf(TextWithSwitchV(TextV(resId = R.string.EnableIconShadow), SwitchV("isEnableIconShadow")))))
            add(Item(test = arrayListOf(LineV())))

            add(Item(test = arrayListOf(TitleTextV(resId = R.string.ModuleFeature))))
            add(Item(test = arrayListOf(TextV(resId = R.string.CleanModuleSettings, onClickListener = { showCleanModuleSettingsDialog() }))))
            add(Item(test = arrayListOf(TextV(resId = R.string.Reboot, onClickListener = { showRestartDialog() }))))
            add(Item(test = arrayListOf(TextWithArrowV(TextV(resId = R.string.About), onClickListener = { setItems(menu) }))))

            //add(Item(test = arrayListOf(LineV())))
            //add(Item(test = arrayListOf(TitleTextV("Test Title"))))
            //add(Item(test = arrayListOf(TextV("Test Function", onClickListener = { Toast.makeText(currentActivity, "Test Toast", Toast.LENGTH_SHORT).show() }))))
            //add(Item(test = arrayListOf(TextWithSeekBarV(TextV("Test Seekbar"), key = "testSeekBar", min = 0, max = 100, divide = 1, defaultProgress = 0))))
            //add(Item(test = arrayListOf(TextWithSwitchV(TextV("Test Switch"), SwitchV("testSwitch")))))

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

    private fun showResetHorizontalDialog() {
        SettingsDialog(currentActivity).apply {
            setTitle(XposedInit.moduleRes.getString(R.string.Reset2))
            setMessage(XposedInit.moduleRes.getString(R.string.Tips3))
            setRButton(XposedInit.moduleRes.getString(R.string.Yes)) {
                OwnSP.set("task_horizontal1", 1.0f)
                OwnSP.set("task_horizontal2", 1.0f)
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

    private fun showResetDockDialog() {
        SettingsDialog(currentActivity).apply {
            setTitle(XposedInit.moduleRes.getString(R.string.Reset))
            setMessage(XposedInit.moduleRes.getString(R.string.Tips1))
            setRButton(XposedInit.moduleRes.getString(R.string.Yes)) {
                OwnSP.set("dockRadius", 2.5f)
                OwnSP.set("dockHeight", 7.9f)
                OwnSP.set("dockSide", 3.0f)
                OwnSP.set("dockBottom", 2.3f)
                OwnSP.set("dockIconBottom", 3.5f)
                OwnSP.set("dockMarginTop", 0.6f)
                OwnSP.set("dockMarginBottom", 11.0f)
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
            setMessage(XposedInit.moduleRes.getString(R.string.Tips4)+ ", ${XposedInit.moduleRes.getString(R.string.setDefaults)}")
            setEditText("", "${XposedInit.moduleRes.getString(R.string.current)}: ${OwnSP.ownSP.getString("iconTitleFontColor", "").toString()}")
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
            setMessage(XposedInit.moduleRes.getString(R.string.setRecentString))
            setEditText("", "${XposedInit.moduleRes.getString(R.string.current)}: ${OwnSP.ownSP.getString("recentText", "").toString()}")
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
            setMessage("${XposedInit.moduleRes.getString(R.string.Defaults)}: 13, ${XposedInit.moduleRes.getString(R.string.Recommend)}：0-30, ${XposedInit.moduleRes.getString(R.string.setDefaults)}")
            setEditText("", "${XposedInit.moduleRes.getString(R.string.current)}: ${OwnSP.ownSP.getInt("backgroundTextSize", 13).toString()}")
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
            setMessage("${XposedInit.moduleRes.getString(R.string.Defaults)}: 1.0, ${XposedInit.moduleRes.getString(R.string.Recommend)}: 0.1-5.0, ${XposedInit.moduleRes.getString(R.string.setDefaults)}")
            setEditText("", "${XposedInit.moduleRes.getString(R.string.current)}: ${OwnSP.ownSP.getFloat("animationLevel", 1f).toString()}")
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
            setMessage("${XposedInit.moduleRes.getString(R.string.Defaults)}: 20.0, ${XposedInit.moduleRes.getString(R.string.Recommend)}: 0.0-50.0, ${XposedInit.moduleRes.getString(R.string.setDefaults)}")
            setEditText("", "${XposedInit.moduleRes.getString(R.string.current)}: ${OwnSP.ownSP.getFloat("recents_task_view_rounded_corners_radius", 20f).toString()}")
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
            setMessage("${XposedInit.moduleRes.getString(R.string.Defaults)}: 40.0, ${XposedInit.moduleRes.getString(R.string.Recommend)}: 0.0-100.0, ${XposedInit.moduleRes.getString(R.string.setDefaults)}")
            setEditText("", "${XposedInit.moduleRes.getString(R.string.current)}: ${OwnSP.ownSP.getFloat("recents_task_view_header_height", 40f).toString()}")
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
            setMessage("${XposedInit.moduleRes.getString(R.string.Defaults)}: 1000.0, ${XposedInit.moduleRes.getString(R.string.Recommend)}: 500.0-1500.0, ${XposedInit.moduleRes.getString(R.string.setDefaults)}")
            setEditText("", "${XposedInit.moduleRes.getString(R.string.current)}: ${OwnSP.ownSP.getFloat("task_vertical", 1000f).toString()}")
            setRButton(XposedInit.moduleRes.getString(R.string.Yes)) {
                if (getEditText() == "") editor.putFloat("task_vertical", 1000f)
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
            setMessage("${XposedInit.moduleRes.getString(R.string.Defaults)}: 12.0, ${XposedInit.moduleRes.getString(R.string.Recommend)}: 0.0-50.0, ${XposedInit.moduleRes.getString(R.string.setDefaults)}")
            setEditText("", "${XposedInit.moduleRes.getString(R.string.current)}: ${OwnSP.ownSP.getFloat("iconTitleFontSize", 12f).toString()}")
            setRButton(XposedInit.moduleRes.getString(R.string.Yes)) {
                if (getEditText() == "") editor.putFloat("iconTitleFontSize", 12f)
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
            setMessage("${XposedInit.moduleRes.getString(R.string.Defaults)}: 3, ${XposedInit.moduleRes.getString(R.string.Recommend)}: 1-6, ${XposedInit.moduleRes.getString(R.string.setDefaults)}")
            setEditText("", "${XposedInit.moduleRes.getString(R.string.current)}: ${OwnSP.ownSP.getInt("folderColumns", 3).toString()}")
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
            setMessage("${XposedInit.moduleRes.getString(R.string.Defaults)}: 1.0, ${XposedInit.moduleRes.getString(R.string.Recommend)}: 0.0-2.0, ${XposedInit.moduleRes.getString(R.string.setDefaults)}")
            setEditText("", "${XposedInit.moduleRes.getString(R.string.current)}: ${OwnSP.ownSP.getFloat("blurRadius", 1f).toString()}")
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