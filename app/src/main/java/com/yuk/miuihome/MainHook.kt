package com.yuk.miuihome

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import com.yuk.miuihome.Config.AndroidSDK
import com.yuk.miuihome.XposedInit.Companion.moduleRes
import com.yuk.miuihome.module.*
import com.yuk.miuihome.utils.*
import com.yuk.miuihome.utils.OwnSP.ownSP
import com.yuk.miuihome.utils.ktx.getObjectField
import com.yuk.miuihome.utils.ktx.hookAfterMethod
import com.yuk.miuihome.utils.ktx.setObjectField
import com.yuk.miuihome.view.*
import kotlin.concurrent.thread
import kotlin.system.exitProcess

class MainHook {

    private val editor by lazy { ownSP.edit() }

    fun doHook() {
        "com.miui.home.settings.MiuiHomeSettingActivity".hookAfterMethod("onCreate", Bundle::class.java) { HomeContext.activity = it.thisObject as Activity }
        "com.miui.home.settings.MiuiHomeSettings".hookAfterMethod("onCreatePreferences", Bundle::class.java, String::class.java) {
            (it.thisObject.getObjectField("mDefaultHomeSetting")).apply {
                setObjectField("mTitle", moduleRes.getString(R.string.ModuleSettings))
                setObjectField("mClickListener", object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        showSettingDialog()
                    }
                })
            }
        }

        // 修改设备分级
        SetDeviceLevel().init()
        // 修改模糊等级
        ModifyBlurLevel().init()
        // 开启平滑动画
        EnableSmoothAnimation().init()
        // 开启文件夹模糊
        EnableBlurWhenOpenFolder().init()
        // 开启水波纹
        EnableMamlDownload().init()
        // 开启时钟常显
        EnableClockGadget().init()
        // 动画速度调节
        ModifyAnimDurationRatio().init()
        // 后台卡片圆角大小调节
        ModifyRoundedCorners().init()
        // 后台卡片图标文字间距调节
        ModifyHeaderHeight().init()
        // 进入后台是否隐藏状态栏
        EnableHideStatusBarWhenEnterRecents().init()
        // 禁用Log
        DisableLog().init()
        // 桌面搜索框模糊
        EnableSearchBarBlur().init()
        // 允许最近任务横屏
        EnableRecentsViewHorizontal().init()
        // 取消最近任务壁纸压暗
        DisableRecentsViewWallpaperDarken().init()
        // 隐藏桌面小部件标题
        ModifyHideWidgetTitles().init()
        // 允许桌面安卓小部件移到负一屏
        AllowWidgetToMinus().init()
        // 允许在安卓小部件显示MIUI组件
        AlwaysShowMIUIWidget().init()
        // 纵向后台卡片大小
        ModifyTaskVertical().init()
        // 横向后台卡片大小
        ModifyTaskHorizontal().init()
        // 开启简单动画
        EnableSimpleAnimation().init()
        // 屏幕无限滚动
        ModifyInfiniteScroll().init()
        // 解锁桌面布局限制
        ModifyUnlockGrids().init()
        // 打开应用时关闭文件夹
        ModifyCloseFolderOnLaunch().init()
        // 显示底栏应用标题
        ModifyShowDockIconTitles().init()
        // 显示底栏应用阴影
        EnableDockIconShadow().init()
        // 允许所有应用使用小窗口
        AllowAllAppsToUseSmallWindow().init()
        // 允许低端机使用MIUI小组件
        EnableLowEndDeviceUseMIUIWidgets().init()
        // 禁用今日推荐
        DisableRecommendServer().init()
        // 移除非抽屉模式下桌面指示器
        ModifyHideSeekPoints().init()
        // 移除应用分组中"全部"选项卡
        ModifyCategoryHideAll().init()
        // 文件夹列数
        ModifyFolderColumnsCount().init()
        // 桌面标题字体大小
        ModifyIconTitleFontSize().init()
        // Prop
        HookSystemProperties().init()
        // Dock
        ModifyDockHook().init()
        // 双击锁屏
        ModifyDoubleTapToSleep().init()
        //
        ModifyUnlockHotseatIcon().init()
        // CustomHook
        //CustomHook.init()
        // ResHook
        ResourcesHook().init()
    }

    private fun showSettingDialog() {
        if (ownSP.getBoolean("isFirstUse", true)) {
            firstUseDialog()
            return
        }
        lateinit var dialog: AlertDialog
        val dialogBuilder = SettingBaseDialog().get()
        dialogBuilder.setView(ScrollView(HomeContext.activity).apply {
            overScrollMode = 2
            addView(LinearLayout(HomeContext.activity).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dip2px(10), dip2px(6), dip2px(10), dip2px(6))
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.app_name), mSize = SettingTextView.titleSize).build())
                if (XposedInit.hasHookPackageResources) {
                    addView(SettingTextView.FastBuilder(mText = "${BuildConfig.VERSION_NAME} - " + "${BuildConfig.VERSION_CODE}(${BuildConfig.BUILD_TYPE}) - " + moduleRes.getString(R.string.ResHook), mColor = "#01b17b").build())
                } else {
                    addView(SettingTextView.FastBuilder(mText = "${BuildConfig.VERSION_NAME} - ${BuildConfig.VERSION_CODE}(${BuildConfig.BUILD_TYPE})", mColor = "#01b17b").build())
                }
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.Warn), mColor = "#ff0c0c").build())
                if (ownSP.getBoolean("simpleAnimation", false)) {
                    addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.SimpleWarn), mColor = "#ff0c0c").build())
                }
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.BaseFeature), mColor = "#0C84FF", mSize = SettingTextView.text2Size).build())
                if (!ownSP.getBoolean("simpleAnimation", false)) {
                    addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.SmoothAnimation), mKey = "smoothAnimation").build())
                    addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.TaskViewBlurLevel)) { showModifyBlurLevel() }.build())
                }
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.AnimationLevel)) { showModifyAnimationLevel() }.build())
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.AdvancedFeature), mColor = "#0C84FF", mSize = SettingTextView.text2Size).build())
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.UnlockGrids), mKey = "unlockGrids").build())
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.ShowDockIconTitles), mKey = "showDockIconTitles").build())
                addView(
                        SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.HideStatusBar), mKey = "hideStatusBar").build())
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.MamlDownload), mKey = "mamlDownload").build())
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.UnlockIcons), mKey = "unlockIcons").build())
                if (!ownSP.getBoolean("simpleAnimation", false)) {
                    addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.WallpaperDarken), mKey = "wallpaperDarken").build())
                }
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.CategoryHideAll), mKey = "categoryHideAll").build())
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.CategoryPagingHideEdit), mKey = "CategoryPagingHideEdit").build())
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.IconTitleFontSize)) { showModifyIconTitleFontSize() }.build())
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.RoundCorner)) { showModifyRoundCorner() }.build())
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.AppTextSize)) { showModifyTextSize() }.build())
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.VerticalTaskViewOfAppCardSize)) { showModifyVertical() }.build())
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.HorizontalTaskViewOfAppCardSize)) { showModifyHorizontal() }.build())
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.Folder), mColor = "#0C84FF", mSize = SettingTextView.text2Size).build())
                if (!ownSP.getBoolean("simpleAnimation", false)) {
                    addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.BlurWhenOpenFolder), mKey = "blurWhenOpenFolder", show = EnableBlurWhenOpenFolder.checked).build())
                }
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.CloseFolder), mKey = "closeFolder").build())
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.FolderWidth), mKey = "folderWidth").build())
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.FolderColumnsCount)) { showModifyFolderColumnsCount() }.build())
                if (HomeContext.isWidgetLauncher) {
                    addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.Widget), mColor = "#0C84FF", mSize = SettingTextView.text2Size).build())
                    addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.HideWidgetTitles), mKey = "hideWidgetTitles").build())
                    addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.WidgetToMinus), mKey = "widgetToMinus").build())
                    addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.AlwaysShowMIUIWidget), mKey = "alwaysShowMIUIWidget").build())
                }
                if (XposedInit.hasHookPackageResources) {
                    addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.ResourceHooks), mColor = "#0C84FF", mSize = SettingTextView.text2Size).build())
                    addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.HideTaskViewAppIcon), mKey = "buttonPadding").build())
                    addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.HideTaskViewCleanUpIcon), mKey = "cleanUp").build())
                    addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.HideTaskViewSmallWindowIcon), mKey = "smallWindow").build())
                    addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.TaskViewAppCardTextSize)) { showModifyBackgroundTextSize() }.build())
                    addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.CustomRecentText)) { showModifyRecentText() }.build())
                }
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.TestFeature), mColor = "#0C84FF", mSize = SettingTextView.text2Size).build())
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.SimpleAnimation), mKey = "simpleAnimation") {
                    dialog.cancel()
                    showSettingDialog()
                }.build())
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.InfiniteScroll), mKey = "infiniteScroll").build())
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.RecommendServer), mKey = "recommendServer").build())
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.HideSeekPoints), mKey = "hideSeekPoints").build())
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.SmallWindow), mKey = "supportSmallWindow").build())
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.LowEndAnim), mKey = "lowEndAnim").build())
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.LowEndDeviceUseMIUIWidgets), mKey = "useMIUIWidgets").build())
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.OtherFeature), mColor = "#0C84FF", mSize = SettingTextView.text2Size).build())
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.AlwaysShowStatusBarClock), mKey = "clockGadget").build())
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.DoubleTap), mKey = "doubleTap").build())
                if (!ownSP.getBoolean("dockSettings", false) && (AndroidSDK == 30)) {
                    addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.SearchBarBlur), mKey = "searchBarBlur").build())
                }
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.DockSettings)) { showDockDialog() }.build())
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.EveryThingBuild)) { BuildWithEverything().init() }.build())
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.BrokenFeature), mColor = "#0C84FF", mSize = SettingTextView.text2Size).build())
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.RealTaskViewHorizontal), mKey = "horizontal").build())
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.EnableIconShadow), mKey = "isEnableIconShadow").build())
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.ModuleFeature), mColor = "#0C84FF", mSize = SettingTextView.text2Size).build())
//              if (BuildConfig.DEBUG) {
//                  addView(SettingTextView.FastBuilder(mText = "自定义Hook") { customHookDialog() }.build())
//              }
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.CleanModuleSettings)) { showCleanModuleSettingsDialog() }.build())
            })
        })
        dialogBuilder.setNeutralButton(moduleRes.getString(R.string.Close), null)
        dialogBuilder.setPositiveButton(moduleRes.getString(R.string.Reboot)) { _, _ -> exitProcess(0) }
        dialog = dialogBuilder.show()
    }

    private fun customHookDialog() {
        val argsEditText = arrayListOf<EditText>()
        val argsLinearLayout = LinearLayout(HomeContext.activity).also { it.orientation = LinearLayout.VERTICAL }

        fun createArgsEditText(): View {
            val linearView = LinearLayout(HomeContext.activity).also { layout ->
                lateinit var editText: EditText
                layout.orientation = LinearLayout.HORIZONTAL
                layout.addView(EditText(HomeContext.activity).apply {
                    argsEditText.add(this)
                    layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                    editText = this
                })
                layout.addView(Button(HomeContext.activity).apply {
                    text = "X"
                    setOnClickListener {
                        argsEditText.remove(editText)
                        argsLinearLayout.removeView(layout)
                    }
                    layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 5f)
                })
            }
            return linearView
        }

        val dialogBuilder = SettingBaseDialog().get()
        dialogBuilder.apply {
            setTitle("自定义Hook")
            setView(ScrollView(HomeContext.activity).apply {
                overScrollMode = 2
                addView(LinearLayout(HomeContext.activity).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(dip2px(10), dip2px(6), dip2px(10), dip2px(6))
                    addView(SettingTextView.FastBuilder(mText = "Class:").build())
                    val classEditText = EditText(HomeContext.activity)
                    addView(classEditText)
                    addView(SettingTextView.FastBuilder(mText = "Method name:").build())
                    val methodEditText = EditText(HomeContext.activity)
                    addView(methodEditText)
                    addView(SettingTextView.FastBuilder(mText = "arg(s):").build())
                    addView(argsLinearLayout)
                    addView(Button(HomeContext.activity).apply {
                        text = "Add arg"
                        setOnClickListener { argsLinearLayout.addView(createArgsEditText()) }
                    })
                    addView(SettingTextView.FastBuilder(mText = "result(null直接不输入即可):").build())
                    val resultEditText = EditText(HomeContext.activity)
                    addView(resultEditText)
                    addView(SettingTextView.FastBuilder(mText = "result type(null直接不输入即可):").build())
                    val resultTypeEditText = EditText(HomeContext.activity)
                    addView(resultTypeEditText)
                })
            })
        }.show()
    }

    private fun showDockDialog() {
        val dialogBuilder = SettingBaseDialog().get()
        lateinit var dialog: AlertDialog
        dialogBuilder.apply {
            setView(ScrollView(HomeContext.activity).apply {
                overScrollMode = 2
                addView(LinearLayout(HomeContext.activity).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(dip2px(10), dip2px(6), dip2px(10), dip2px(6))
                    addView(SettingTextView.FastBuilder(mText = "「" + moduleRes.getString(R.string.DockSettings) + "」", mColor = "#0C84FF", mSize = SettingTextView.text2Size).build())
                    addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.DockFeature), mKey = "dockSettings") {
                        dialog.cancel()
                        showDockDialog()
                    }.build())
                    if (ownSP.getBoolean("dockSettings", false)) {
                        if (AndroidSDK == 30) {
                            addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.EnableDockBlur), mKey = "searchBarBlur").build())
                        }
                        if (!XposedInit.hasHookPackageResources) {
                            addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.DockWarn), mColor = "#ff0c0c", mSize = SettingTextView.textSize).build())
                        } else {
                            addView(SettingSeekBar.FastBuilder(mText = moduleRes.getString(R.string.DockRoundedCorners), mKey = "dockRadius", minValue = 0, maxValue = 50, defValue = 25).build())
                        }
                        addView(SettingSeekBar.FastBuilder(mText = moduleRes.getString(R.string.DockHeight), mKey = "dockHeight", minValue = 50, maxValue = 150, defValue = 79).build())
                        addView(SettingSeekBar.FastBuilder(mText = moduleRes.getString(R.string.DockSide), mKey = "dockSide", minValue = 0, maxValue = 100, defValue = 30).build())
                        addView(SettingSeekBar.FastBuilder(mText = moduleRes.getString(R.string.DockBottom), mKey = "dockBottom", minValue = 0, maxValue = 150, defValue = 23).build())
                        addView(SettingSeekBar.FastBuilder(mText = moduleRes.getString(R.string.DockIconBottom), mKey = "dockIconBottom", minValue = 0, maxValue = 150, defValue = 35).build())
                        addView(SettingSeekBar.FastBuilder(mText = moduleRes.getString(R.string.DockIconTop), mKey = "dockIconTop", minValue = 0, maxValue = 80, defValue = 6).build())
                    }
                })
            })
            if (ownSP.getBoolean("dockSettings", false)) {
                setPositiveButton(moduleRes.getString(R.string.Save), null)
                setNeutralButton(moduleRes.getString(R.string.Reset1)) { _, _ -> showModifyReset1() }
            }
        }
        dialog = dialogBuilder.show()
    }

    private fun showModifyRoundCorner() {
        SettingUserInputNumber(moduleRes.getString(R.string.RoundCorner), "recents_task_view_rounded_corners_radius", 0, 100, 20, 1).build()
    }

    private fun showModifyTextSize() {
        SettingUserInputNumber(moduleRes.getString(R.string.AppTextSize), "recents_task_view_header_height", 0, 200, 40, 1).build()
    }

    private fun showModifyAnimationLevel() {
        SettingSeekBarDialog(moduleRes.getString(R.string.AnimationLevel), "animationLevel", 5, 500, canUserInput = true, defValue = 100).build()
    }

    private fun showModifyBackgroundTextSize() {
        SettingUserInputNumber(moduleRes.getString(R.string.TaskViewAppCardTextSize), "backgroundTextSize", 0, 100, 13, 1).build()
    }

    private fun showModifyVertical() {
        SettingUserInputNumber(moduleRes.getString(R.string.VerticalTaskViewOfAppCardSize), "task_vertical", 50, 150, 100, 100).build()
    }

    private fun showModifyHorizontal() {
        val dialogBuilder = SettingBaseDialog().get()
        dialogBuilder.apply {
            setView(ScrollView(HomeContext.activity).apply {
                overScrollMode = 2
                addView(LinearLayout(HomeContext.activity).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(dip2px(10), dip2px(6), dip2px(10), dip2px(6))
                    addView(SettingTextView.FastBuilder(mText = "「" + moduleRes.getString(R.string.HorizontalTaskViewOfAppCardSize) + "」", mColor = "#0C84FF", mSize = SettingTextView.text2Size).build())
                    addView(SettingSeekBar.FastBuilder(mText = moduleRes.getString(R.string.VerticalScreen), mKey = "task_horizontal1", minValue = 10, maxValue = 1500, divide = 1000, defValue = 1000).build())
                    addView(SettingSeekBar.FastBuilder(mText = moduleRes.getString(R.string.HorizontalScreen), mKey = "task_horizontal2", minValue = 10, maxValue = 1500, divide = 1000, defValue = 1000).build())
                })
            })
            setPositiveButton(moduleRes.getString(R.string.Save), null)
            setNeutralButton(moduleRes.getString(R.string.Reset1)) { _, _ -> showModifyReset2() }
        }
        dialogBuilder.show()
    }

    private fun showModifyFolderColumnsCount() {
        SettingUserInputNumber(moduleRes.getString(R.string.FolderColumnsCount), "folderColumns", 1, 6, 3, 1).build()
    }

    private fun showModifyIconTitleFontSize() {
        SettingUserInputNumber(moduleRes.getString(R.string.IconTitleFontSize), "iconTitleFontSize", 0, 30, 12, 1).build()
    }

    private fun showModifyRecentText() {
        SettingUserInputText(moduleRes.getString(R.string.CustomRecentText), "recentText").build()
    }

    private fun showModifyBlurLevel() {
        val dialogBuilder = SettingBaseDialog().get()
        val mKey = "blurLevel"
        lateinit var dialog: AlertDialog
        lateinit var onClick: View
        fun saveValue(value: String) {
            editor.putString(mKey, value)
            editor.apply()
        }
        dialogBuilder.setView(ScrollView(HomeContext.activity).apply {
            overScrollMode = 2
            addView(LinearLayout(HomeContext.activity).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dip2px(10), dip2px(6), dip2px(10), dip2px(6))
                addView(SettingTextView.FastBuilder(mText = "「" + moduleRes.getString(R.string.TaskViewBlurLevel) + "」", mSize = SettingTextView.text2Size, mColor = "#0C84FF").build())
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.CompleteBlur)) {
                    saveValue("COMPLETE")
                    onClick = it
                    dialog.dismiss()
                }.build())
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.TestBlur)) {
                    saveValue("TEST")
                    onClick = it
                    dialog.dismiss()
                }.build())
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.SimpleBlur)) {
                    saveValue("SIMPLE")
                    onClick = it
                    dialog.dismiss()
                }.build())
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.NoneBlur)) {
                    saveValue("NONE")
                    onClick = it
                    dialog.dismiss()
                }.build())
            })
        })
        dialog = dialogBuilder.show().apply {
            setOnDismissListener {
                try {
                    LogUtil.toast(moduleRes.getString(R.string.TaskViewBlurSetTo) + " : ${(onClick as TextView).text}")
                } catch (ignore: Exception) {
                }
            }
        }
    }

    private fun showModifyReset1() {
        val dialogBuilder = SettingBaseDialog().get()
        dialogBuilder.apply {
            setView(ScrollView(HomeContext.activity).apply {
                overScrollMode = 2
                addView(LinearLayout(HomeContext.activity).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(dip2px(10), dip2px(6), dip2px(10), dip2px(6))
                    addView(SettingTextView.FastBuilder(mText = "「" + moduleRes.getString(R.string.Reset2) + "」", mColor = "#0C84FF", mSize = SettingTextView.text2Size).build())
                    addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.Tips3)).build())
                })
            })
            setNeutralButton(moduleRes.getString(R.string.Yes)) { _, _ ->
                OwnSP.set("searchBarBlur", true)
                OwnSP.set("dockRadius", 2.5f)
                OwnSP.set("dockHeight", 7.9f)
                OwnSP.set("dockSide", 3f)
                OwnSP.set("dockBottom", 1.6f)
                OwnSP.set("dockIconBottom", 2.5f)
                OwnSP.set("dockIconTop", 0.6f)
                thread {
                    LogUtil.toast(moduleRes.getString(R.string.Reboot2))
                    Thread.sleep(1000)
                    exitProcess(0)
                }
            }
            setPositiveButton(moduleRes.getString(R.string.Cancel), null)
        }
        dialogBuilder.show()
    }

    private fun showModifyReset2() {
        val dialogBuilder = SettingBaseDialog().get()
        dialogBuilder.apply {
            setView(ScrollView(HomeContext.activity).apply {
                overScrollMode = 2
                addView(LinearLayout(HomeContext.activity).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(dip2px(10), dip2px(6), dip2px(10), dip2px(6))
                    addView(SettingTextView.FastBuilder(mText = "「" + moduleRes.getString(R.string.Reset1) + "」", mColor = "#0C84FF", mSize = SettingTextView.text2Size).build())
                    addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.Tips2)).build())
                })
            })
            setNeutralButton(moduleRes.getString(R.string.Yes)) { _, _ ->
                OwnSP.set("task_horizontal1", 1.0f)
                OwnSP.set("task_horizontal2", 1.0f)
                thread {
                    LogUtil.toast(moduleRes.getString(R.string.Reboot2))
                    Thread.sleep(1000)
                    exitProcess(0)
                }
            }
            setPositiveButton(moduleRes.getString(R.string.Cancel), null)
        }
        dialogBuilder.show()
    }


    private fun showCleanModuleSettingsDialog() {
        val dialogBuilder = SettingBaseDialog().get()
        dialogBuilder.apply {
            setView(ScrollView(HomeContext.activity).apply {
                overScrollMode = 2
                addView(LinearLayout(HomeContext.activity).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(dip2px(10), dip2px(6), dip2px(10), dip2px(6))
                    addView(SettingTextView.FastBuilder(mText = "「" + moduleRes.getString(R.string.CleanModuleSettings) + "」", mColor = "#0C84FF", mSize = SettingTextView.text2Size).build())
                    addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.Tips2)).build())
                })
            })
            setNeutralButton(moduleRes.getString(R.string.Yes)) { _, _ ->
                editor.clear()
                editor.commit()
                OwnSP.set("isFirstUse", false)
                OwnSP.set("animationLevel", 1.25f)
                OwnSP.set("dockRadius", 2.5f)
                OwnSP.set("dockHeight", 7.9f)
                OwnSP.set("dockSide", 3.0f)
                OwnSP.set("dockBottom", 2.3f)
                OwnSP.set("dockIconBottom", 3.5f)
                OwnSP.set("dockIconTop", 0.6f)
                OwnSP.set("folderColumns", 3f)
                OwnSP.set("task_horizontal1", 1.0f)
                OwnSP.set("task_horizontal2", 1.0f)
                thread {
                    LogUtil.toast(moduleRes.getString(R.string.Reboot2))
                    Thread.sleep(1000)
                    exitProcess(0)
                }
            }
            setPositiveButton(moduleRes.getString(R.string.Cancel), null)
        }
        dialogBuilder.show()
    }

    private fun firstUseDialog() {
        val dialogBuilder = SettingBaseDialog().get()
        dialogBuilder.apply {
            setView(ScrollView(HomeContext.activity).apply {
                overScrollMode = 2
                addView(LinearLayout(HomeContext.activity).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(dip2px(10), dip2px(6), dip2px(10), dip2px(6))
                    addView(SettingTextView.FastBuilder(mText = "「" + moduleRes.getString(R.string.Welcome) + "」", mColor = "#0C84FF", mSize = SettingTextView.text2Size).build())
                    addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.Tips)).build())
                })
            })
            setOnDismissListener {
                OwnSP.set("isFirstUse", false)
                OwnSP.set("animationLevel", 1.25f)
                OwnSP.set("dockRadius", 2.5f)
                OwnSP.set("dockHeight", 7.9f)
                OwnSP.set("dockSide", 3.0f)
                OwnSP.set("dockBottom", 2.3f)
                OwnSP.set("dockIconBottom", 3.5f)
                OwnSP.set("dockIconTop", 0.6f)
                OwnSP.set("folderColumns", 3f)
                OwnSP.set("task_horizontal1", 1.0f)
                OwnSP.set("task_horizontal2", 1.0f)
                thread {
                    LogUtil.toast(moduleRes.getString(R.string.Reboot2))
                    Thread.sleep(1000)
                    exitProcess(0)
                }
            }
            setPositiveButton(moduleRes.getString(R.string.Yes), null)
            setCancelable(false)
        }
        dialogBuilder.show()
    }
}