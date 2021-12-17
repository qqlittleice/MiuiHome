package com.yuk.miuihome

import android.app.AlertDialog
import android.view.View
import android.widget.*
import com.yuk.miuihome.utils.Config.AndroidSDK
import com.yuk.miuihome.XposedInit.Companion.moduleRes
import com.yuk.miuihome.module.BuildWithEverything
import com.yuk.miuihome.module.EnableBlurWhenOpenFolder
import com.yuk.miuihome.utils.HomeContext
import com.yuk.miuihome.utils.LogUtil
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.dip2px
import com.yuk.miuihome.view.*
import kotlin.concurrent.thread
import kotlin.system.exitProcess

class SettingDialog {

    fun showSettingDialog() {
        lateinit var dialog: AlertDialog
        val dialogBuilder = SettingBaseDialog().get()
        dialogBuilder.setView(ScrollView(HomeContext.activity).apply {
            overScrollMode = 2
            addView(LinearLayout(HomeContext.activity).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dip2px(10), dip2px(6), dip2px(10), dip2px(6))
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.app_name), mSize = SettingTextView.titleSize).build())
                addView(SettingTextView.FastBuilder(mText = XposedInit().checkVersionName(), mColor = "#01b17b").build())
                if (XposedInit.hasHookPackageResources) {
                    addView(SettingTextView.FastBuilder(mText = "${BuildConfig.VERSION_NAME} - " + "${BuildConfig.VERSION_CODE}(${BuildConfig.BUILD_TYPE}) - " + moduleRes.getString(R.string.ResHook), mColor = "#01b17b").build())
                } else {
                    addView(SettingTextView.FastBuilder(mText = "${BuildConfig.VERSION_NAME} - ${BuildConfig.VERSION_CODE}(${BuildConfig.BUILD_TYPE})", mColor = "#01b17b").build())
                }
                if (OwnSP.ownSP.getBoolean("simpleAnimation", false)) {
                    addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.SimpleWarn), mColor = "#ff0c0c").build())
                }
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.BaseFeature), mColor = "#0C84FF", mSize = SettingTextView.text2Size).build())
                if (!OwnSP.ownSP.getBoolean("simpleAnimation", false)) {
                    addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.SmoothAnimation), mKey = "smoothAnimation").build())
                    addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.TaskViewBlurLevel)) { showModifyBlurLevel() }.build())
                }
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.AnimationLevel)) { showModifyAnimationLevel() }.build())
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.AdvancedFeature), mColor = "#0C84FF", mSize = SettingTextView.text2Size).build())
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.UnlockGrids), mKey = "unlockGrids").build())
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.ShowDockIconTitles), mKey = "showDockIconTitles").build())
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.HideStatusBar), mKey = "hideStatusBar").build())
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.MamlDownload), mKey = "mamlDownload").build())
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.UnlockIcons), mKey = "unlockIcons").build())
                if (!OwnSP.ownSP.getBoolean("simpleAnimation", false)) {
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
                if (!OwnSP.ownSP.getBoolean("simpleAnimation", false)) {
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
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.AppReturnAmin),  mKey = "appReturnAmin") {
                    dialog.cancel()
                    showSettingDialog()
                }.build())
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.InfiniteScroll), mKey = "infiniteScroll").build())
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.RecommendServer), mKey = "recommendServer").build())
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.HideSeekPoints), mKey = "hideSeekPoints").build())
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.SmallWindow), mKey = "supportSmallWindow").build())
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.LowEndAnim), mKey = "lowEndAnim").build())
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.LowEndDeviceUseMIUIWidgets), mKey = "useMIUIWidgets").build())
                if (!OwnSP.ownSP.getBoolean("appReturnAmin", false)) {
                    addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.BlurRadius)) { showBlurRadius() }.build())
                }
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.OtherFeature), mColor = "#0C84FF", mSize = SettingTextView.text2Size).build())
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.AlwaysShowStatusBarClock), mKey = "clockGadget").build())
                addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.DoubleTap), mKey = "doubleTap").build())
                if (!OwnSP.ownSP.getBoolean("dockSettings", false) && (AndroidSDK >= 30)) {
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
                        text = "X"
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
                    if (OwnSP.ownSP.getBoolean("dockSettings", false)) {
                        if (AndroidSDK >= 30) {
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
                        addView(SettingSeekBar.FastBuilder(mText = moduleRes.getString(R.string.DockMarginTop), mKey = "dockMarginTop", minValue = 0, maxValue = 100, defValue = 6).build())
                        addView(SettingSeekBar.FastBuilder(mText = moduleRes.getString(R.string.DockMarginBottom), mKey = "dockMarginBottom", minValue = 0, maxValue = 200, defValue = 110).build())
                    }
                })
            })
            if (OwnSP.ownSP.getBoolean("dockSettings", false)) {
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

    private fun showBlurRadius() {
        SettingUserInputNumber(moduleRes.getString(R.string.BlurRadius), "blurRadius", 0, 200, 100, 100).build()
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
            setNeutralButton(moduleRes.getString(R.string.Reset2)) { _, _ -> showModifyReset2() }
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
        lateinit var dialog: AlertDialog
        lateinit var onClick: View
        dialogBuilder.setView(ScrollView(HomeContext.activity).apply {
            overScrollMode = 2
            addView(LinearLayout(HomeContext.activity).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dip2px(10), dip2px(6), dip2px(10), dip2px(6))
                addView(SettingTextView.FastBuilder(mText = "「" + moduleRes.getString(R.string.TaskViewBlurLevel) + "」", mSize = SettingTextView.text2Size, mColor = "#0C84FF").build())
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.CompleteBlur)) {
                    OwnSP.set("blurLevel", 2f)
                    onClick = it
                    dialog.dismiss()
                }.build())
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.TestBlur)) {
                    OwnSP.set("blurLevel", 3f)
                    onClick = it
                    dialog.dismiss()
                }.build())
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.SimpleBlur)) {
                    OwnSP.set("blurLevel", 1f)
                    onClick = it
                    dialog.dismiss()
                }.build())
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.NoneBlur)) {
                    OwnSP.set("blurLevel", 0f)
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
                    addView(SettingTextView.FastBuilder(mText = "「" + moduleRes.getString(R.string.Reset) + "」", mColor = "#0C84FF", mSize = SettingTextView.text2Size).build())
                    addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.Tips1)).build())
                })
            })
            setNeutralButton(moduleRes.getString(R.string.Yes)) { _, _ ->
                OwnSP.set("searchBarBlur", true)
                OwnSP.set("dockRadius", 2.5f)
                OwnSP.set("dockHeight", 7.9f)
                OwnSP.set("dockSide", 3f)
                OwnSP.set("dockBottom", 1.6f)
                OwnSP.set("dockIconBottom", 3.5f)
                OwnSP.set("dockMarginTop", 0.6f)
                OwnSP.set("dockMarginBottom", 11.0f)
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
                    addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.Tips3)).build())
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
                OwnSP.clear()
                OwnSP.set("isFirstUse", true)
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

    fun firstUseDialog() {
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
                OwnSP.set("dockMarginTop", 0.6f)
                OwnSP.set("dockMarginBottom", 11.0f)
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