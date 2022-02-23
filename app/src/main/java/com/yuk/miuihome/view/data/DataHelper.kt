package com.yuk.miuihome.view.data

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import com.yuk.miuihome.BuildConfig
import com.yuk.miuihome.R
import com.yuk.miuihome.XposedInit
import com.yuk.miuihome.module.BuildWithEverything
import com.yuk.miuihome.module.ModifyBlurLevel
import com.yuk.miuihome.view.SettingsDialog
import com.yuk.miuihome.view.base.*
import com.yuk.miuihome.utils.Config
import com.yuk.miuihome.utils.LogUtil
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.view.HookSettingsActivity
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
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.DockFeature), switchV = SwitchV("dockSettings")
            ))))
            if (Config.AndroidSDK == 30)
                add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.EnableDockBlur), switchV = SwitchV("searchBarBlur")))))
            if (!XposedInit.hasHookPackageResources)
                add(Item(list = arrayListOf(TextV(resId = R.string.DockWarn, textColor = Color.parseColor("#ff0c0c"), textSize = 16f))))
            else
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

    private fun loadItems(): ArrayList<Item> {
        val itemList = arrayListOf<Item>()
        itemList.apply {
            add(Item(list = arrayListOf(TextV(resId = R.string.app_name, typeface = Typeface.create(null, 300, false), textSize = 30f))))

            if (OwnSP.ownSP.getBoolean("simpleAnimation", false)) {
                add(Item(list = arrayListOf(TextV(resId = R.string.SimpleWarn, textColor = Color.parseColor("#ff0c0c")))))
            } else {
                add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.SmoothAnimation), switchV = SwitchV("smoothAnimation")))))
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
                    if (ModifyBlurLevel.checked)
                        add(Item(list = arrayListOf(TextWithSpinnerV(TextV(resId = R.string.TaskViewBlurLevel), select = dict0[OwnSP.ownSP.getString("blurLevel", "SimpleBlur")], array = blurLevel, callBacks = { OwnSP.set("blurLevel", dict0[it].toString()) }))))
                    else
                        add(Item(list = arrayListOf(TextWithSpinnerV(TextV(resId = R.string.TaskViewBlurLevel), select = dict0[OwnSP.ownSP.getString("blurLevel", "SimpleBlur")], array = blurLevel0, callBacks = { OwnSP.set("blurLevel", dict0[it].toString()) }))))
                }
            }
            add(Item(list = arrayListOf(TextV(resId = R.string.AnimationLevel, onClickListener = { showAnimationLevelDialog() }))))
            add(Item(list = arrayListOf(LineV())))

            add(Item(list = arrayListOf(SubtitleV(resId = R.string.AdvancedFeature))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId =R.string.UnlockGrids), switchV = SwitchV("unlockGrids")))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.ShowDockIconTitles), switchV = SwitchV("showDockIconTitles")))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.HideTaskViewAppIcon), switchV = SwitchV("recentIcon")))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.HideTaskViewCleanUpIcon), switchV = SwitchV("cleanUp")))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.HideStatusBar), switchV = SwitchV("hideStatusBar")))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.MamlDownload), switchV = SwitchV("mamlDownload")))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.UnlockIcons), switchV = SwitchV("unlockIcons")))))
            if (!OwnSP.ownSP.getBoolean("simpleAnimation", false)) {
                add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.WallpaperDarken), switchV = SwitchV("wallpaperDarken")))))
            }
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.HideTaskViewSmallWindowIcon), switchV = SwitchV("smallWindow")))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.CategoryHideAll), switchV = SwitchV("categoryHideAll")))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.CategoryPagingHideEdit), switchV = SwitchV("CategoryPagingHideEdit")))))
            add(Item(list = arrayListOf(TextV(resId = R.string.CustomTitleColor, onClickListener = { showCustomTitleColorDialog() }))))
            add(Item(list = arrayListOf(TextV(resId = R.string.IconTitleFontSize, onClickListener = { showIconTitleFontSizeDialog() }))))
            add(Item(list = arrayListOf(TextV(resId = R.string.TaskViewAppCardTextSize, onClickListener = { showTaskViewAppCardTextSizeDialog() }))))
            add(Item(list = arrayListOf(TextV(resId = R.string.RoundCorner, onClickListener = { showRoundCornerDialog() }))))
            add(Item(list = arrayListOf(TextV(resId = R.string.AppTextSize, onClickListener = { showAppTextSizeDialog() }))))
            add(Item(list = arrayListOf(TextV(resId = R.string.CustomRecentText, onClickListener = { showCustomRecentTextDialog() }))))
            add(Item(list = arrayListOf(TextV(resId = R.string.VerticalTaskViewOfAppCardSize, onClickListener = { showVerticalTaskViewOfAppCardSizeDialog() }))))
            add(Item(list = arrayListOf(TextWithArrowV(TextWithSummaryV(titleResId = R.string.HorizontalTaskViewOfAppCardSize)) {
                setItems(
                    horizontal
                )
            })))
            add(Item(list = arrayListOf(LineV())))

            add(Item(list = arrayListOf(SubtitleV(resId = R.string.Folder))))
            if (!OwnSP.ownSP.getBoolean("simpleAnimation", false) && !OwnSP.ownSP.getBoolean("alwaysBlur", false)) {
                add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.BlurWhenOpenFolder), switchV = SwitchV("blurWhenOpenFolder")))))
            }
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.CloseFolder), switchV = SwitchV("closeFolder")))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.FolderWidth), switchV = SwitchV("folderWidth")))))
            add(Item(list = arrayListOf(TextV(resId = R.string.FolderColumnsCount, onClickListener = { showFolderColumnsCountDialog() }))))
            add(Item(list = arrayListOf(LineV())))

            if (XposedInit().checkWidgetLauncher()) {
                add(Item(list = arrayListOf(SubtitleV(resId = R.string.Widget))))
                add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.HideWidgetTitles), switchV = SwitchV("hideWidgetTitles")))))
                add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.WidgetToMinus), switchV = SwitchV("widgetToMinus")))))
                add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.AlwaysShowMIUIWidget), switchV = SwitchV("alwaysShowMIUIWidget")))))
                add(Item(list = arrayListOf(LineV())))
            }

            add(Item(list = arrayListOf(SubtitleV(resId = R.string.TestFeature))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.SimpleAnimation), switchV = SwitchV("simpleAnimation", customOnCheckedChangeListener =  { _, _ -> currentActivity.recreate() })))))
            if (!OwnSP.ownSP.getBoolean("alwaysBlur", false))
                add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.AppReturnAmin), switchV = SwitchV("appReturnAmin", customOnCheckedChangeListener =  { _, _ -> currentActivity.recreate() })))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.InfiniteScroll), switchV = SwitchV("infiniteScroll")))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.RecommendServer), switchV = SwitchV("recommendServer")))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.HideSeekPoints), switchV = SwitchV("hideSeekPoints")))))
            if (!OwnSP.ownSP.getBoolean("appReturnAmin", false) && !OwnSP.ownSP.getBoolean("simpleAnimation", false))
                add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.AlwaysBlur), switchV = SwitchV("alwaysBlur", customOnCheckedChangeListener =  { _, _ -> currentActivity.recreate() })))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.SmallWindow), switchV = SwitchV("supportSmallWindow")))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.LowEndAnim), switchV = SwitchV("lowEndAnim")))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.LowEndDeviceUseMIUIWidgets), switchV = SwitchV("useMIUIWidgets")))))
            if (!OwnSP.ownSP.getBoolean("appReturnAmin", false) && !OwnSP.ownSP.getBoolean("simpleAnimation", false))
                add(Item(list = arrayListOf(TextV(resId = R.string.BlurRadius, onClickListener = { showBlurRadiusDialog() }))))
            add(Item(list = arrayListOf(LineV())))

            add(Item(list = arrayListOf(SubtitleV(resId = R.string.OtherFeature))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.AlwaysShowStatusBarClock), switchV = SwitchV("clockGadget")))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.DoubleTap), switchV = SwitchV("doubleTap")))))
            if (!OwnSP.ownSP.getBoolean("dockSettings", false) && Config.AndroidSDK == 30)
                add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.SearchBarBlur), switchV = SwitchV("searchBarBlur")))))
            add(Item(list = arrayListOf(TextWithArrowV(TextWithSummaryV(titleResId = R.string.DockSettings)) {
                setItems(
                    dock
                )
            })))
            add(Item(list = arrayListOf(TextV(resId = R.string.EveryThingBuild, onClickListener = { BuildWithEverything().init() }))))
            add(Item(list = arrayListOf(LineV())))

            add(Item(list = arrayListOf(SubtitleV(resId = R.string.BrokenFeature))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.RealTaskViewHorizontal), switchV = SwitchV("horizontal")))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.EnableIconShadow), switchV = SwitchV("isEnableIconShadow")))))
            add(Item(list = arrayListOf(LineV())))

            add(Item(list = arrayListOf(SubtitleV(resId = R.string.ModuleFeature))))
            add(Item(list = arrayListOf(TextV(resId = R.string.Reboot, onClickListener = { showRestartDialog() }))))
            add(Item(list = arrayListOf(TextWithArrowV(TextWithSummaryV(titleResId = R.string.BackupModuleSettings)) {
                (currentActivity as HookSettingsActivity).spBackup.also {
                    it.requestWriteToFile(
                        "config_${System.currentTimeMillis()}.json",
                        it.getWriteJson()
                    )
                }
            })))
            add(Item(list = arrayListOf(TextWithArrowV(TextWithSummaryV(titleResId = R.string.RestoreModuleSettings)) { showRestoreModuleSettingsDialog() })))
            add(Item(list = arrayListOf(TextV(resId = R.string.CleanModuleSettings, onClickListener = { showCleanModuleSettingsDialog() }))))
            add(Item(list = arrayListOf(TextWithSwitchV(TextWithSummaryV(titleResId = R.string.AppCenter, summaryResId = R.string.AppCenterTip), SwitchV("appCenter")))))

            add(Item(list = arrayListOf(TextWithArrowV(TextWithSummaryV(titleResId = R.string.Github, summaryResId = R.string.OpenGithub)) {
                val uri = Uri.parse("https://github.com/qqlittleice/MiuiHome");
                val intent = Intent(Intent.ACTION_VIEW, uri); currentActivity.startActivity(intent)
            })))
            add(Item(list = arrayListOf(TextWithArrowV(TextWithSummaryV(titleResId = R.string.About, summary = "${BuildConfig.VERSION_NAME}(${BuildConfig.BUILD_TYPE})")) {
                setItems(
                    menu
                )
            })))

            //add(Item(list = arrayListOf(LineV())))
            //add(Item(list = arrayListOf(TitleTextV(text = "Subtitle"))))
            //add(Item(list = arrayListOf(TextV(text = "Title", onClickListener = { Toast.makeText(currentActivity, "Test Toast", Toast.LENGTH_SHORT).show() }))))
            //add(Item(list = arrayListOf(TextRV(title = "Title"), onClickListener = { Toast.makeText(currentActivity, "Test Toast", Toast.LENGTH_SHORT).show() }))))
            //add(Item(list = arrayListOf(TextRV(title = "Title", summary = "Summary", onClickListener = { Toast.makeText(currentActivity, "Test Toast", Toast.LENGTH_SHORT).show() }))))
            //add(Item(list = arrayListOf(TextRV(title = "Title", summary = "Summary"), onClickListener = { Toast.makeText(currentActivity, "Test Toast", Toast.LENGTH_SHORT).show() }))))
            //add(Item(list = arrayListOf(TextWithSwitchV(TextV("Switch"), switchV = SwitchV("testSwitch")))))
            //add(Item(list = arrayListOf(TextWithSeekBarV(TextV("Seekbar"), key = "testSeekBar", min = 0, max = 100, divide = 1, defaultProgress = 0))))

        }
        return itemList
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

    private fun showRestoreModuleSettingsDialog() {
        SettingsDialog(currentActivity).apply {
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
        SettingsDialog(currentActivity).apply {
            setTitle(XposedInit.moduleRes.getString(R.string.Reset2))
            setMessage(XposedInit.moduleRes.getString(R.string.Tips3))
            setRButton(XposedInit.moduleRes.getString(R.string.Yes)) {
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
            setMessage("${XposedInit.moduleRes.getString(R.string.Defaults)}: -1, ${XposedInit.moduleRes.getString(R.string.Recommend)}：-1-30, ${XposedInit.moduleRes.getString(R.string.setDefaults)}")
            setEditText("", "${XposedInit.moduleRes.getString(R.string.current)}: ${OwnSP.ownSP.getFloat("recentTextSize", -1f).toString()}")
            setRButton(XposedInit.moduleRes.getString(R.string.Yes)) {
                if (getEditText() == "") editor.putFloat("recentTextSize", -1f)
                else editor.putFloat("recentTextSize", getEditText().toFloat())
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
