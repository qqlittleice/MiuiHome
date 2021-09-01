package com.yuk.miuihome

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.Keep
import com.yuk.miuihome.module.*
import com.yuk.miuihome.utils.LogUtil
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.dp2px
import com.yuk.miuihome.utils.ktx.getObjectField
import com.yuk.miuihome.utils.ktx.hookAfterMethod
import com.yuk.miuihome.utils.ktx.setObjectField
import com.yuk.miuihome.view.*
import kotlin.concurrent.thread
import kotlin.system.exitProcess

@Keep
class MainHook {

    private val sharedPreferences = OwnSP.ownSP
    private val editor by lazy { sharedPreferences.edit() }

    fun doHook() {

        "com.miui.home.settings.MiuiHomeSettingActivity".hookAfterMethod("onCreate", Bundle::class.java) {
            HomeContext.activity = it.thisObject as Activity
        }

        "com.miui.home.settings.MiuiHomeSettings".hookAfterMethod("onCreatePreferences", Bundle::class.java, String::class.java) {
            (it.thisObject.getObjectField("mDefaultHomeSetting")).apply {
                setObjectField("mTitle", "模块设置")
                setObjectField("mClickListener", object: View.OnClickListener {
                    override fun onClick(v: View?) {
                        showSettingDialog()
                    }
                })
                if (XposedInit.hasHookPackageResources) {
                    setObjectField("mTitle", "模块设置(资源钩子√)")
                }
            }
        }

        //修改设备分级
        SetDeviceLevel().init()
        //修改模糊等级
        ModifyBlurLevel().init()
        //开启平滑动画
        EnableSmoothAnimation().init()
        //开启文件夹模糊
        EnableBlurWhenOpenFolder().init()
        //开启水波纹
        EnableMamlDownload().init()
        //开启时钟常显
        EnableClockGadget().init()
        //动画速度调节
        ModifyAnimDurationRatio().init()
        //后台卡片圆角大小调节
        ModifyRoundedCorners().init()
        //后台卡片图标文字间距调节
        ModifyHeaderHeight().init()
        //进入后台是否隐藏状态栏
        EnableHideStatusBarWhenEnterRecents().init()
        //禁用Log
        DisableLog().init()
        //桌面搜索框模糊
        EnableSearchBarBlur().init()
        //允许最近任务横屏
        EnableRecentsViewHorizontal().init()
        //取消最近任务壁纸压暗
        DisableRecentsViewWallpaperDarken().init()
        //隐藏桌面小部件标题
        HideWidgetTitle().init()
        //允许桌面安卓小部件移到负一屏
        AllowWidgetToMinus().init()
        //允许在安卓小部件显示MIUI组件
        AlwaysShowMIUIWidget().init()
        //纵向后台卡片大小
        ModifyTaskVertical().init()
        //横向后台卡片大小
        ModifyTaskHorizontal().init()
        //开启简单动画
        EnableSimpleAnimation().init()
    }

    private fun showSettingDialog() {
        if (sharedPreferences.getBoolean("isFirstUse", true)) {
            firstUseDialog()
            return
        }
        val dialogBuilder = SettingBaseDialog().get()
        dialogBuilder.setView(ScrollView(HomeContext.activity).apply {
            overScrollMode = 2
            addView(LinearLayout(HomeContext.activity).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(
                    dp2px(HomeContext.context, 10f),
                    dp2px(HomeContext.context, 10f),
                    dp2px(HomeContext.context, 10f),
                    dp2px(HomeContext.context, 5f)
                )
                addView(SettingTextView.FastBuilder(mText = "MiuiHome", mSize = SettingTextView.titleSize).build())
                addView(SettingTextView.FastBuilder(mText = "注意:部分功能模块只提供开关开启,实际效果由您设备上运行的桌面版本而定.", mColor = "#ff0c0c", mSize = SettingTextView.textSize).build())
                addView(SettingTextView.FastBuilder(mText = "基础设定", mColor = "#0C84FF", mSize = SettingTextView.text2Size).build())
                addView(SettingSwitch.FastBuilder(mText = "平滑动画", mKey = "smoothAnimation").build())
                if (!OwnSP.ownSP.getBoolean("simpleAnimation", false)) {
                    addView(SettingTextView.FastBuilder(mText = "后台模糊级别") { showModifyBlurLevel() }.build())
                }
                addView(SettingTextView.FastBuilder(mText = "手势动画速度") { showModifyAnimationLevel() }.build())
                addView(SettingTextView.FastBuilder(mText = "进阶设定", mColor = "#0C84FF", mSize = SettingTextView.text2Size).build())
                addView(SettingSwitch.FastBuilder(mText = "后台隐藏状态栏", mKey = "hideStatusBar").build())
                if (!OwnSP.ownSP.getBoolean("simpleAnimation", false)) {
                addView(SettingSwitch.FastBuilder(mText = "取消壁纸压暗效果", mKey = "wallpaperDarken").build())
                }
                addView(SettingSwitch.FastBuilder(mText = "水波纹应用下载特效", mKey = "mamlDownload").build())
                if (!OwnSP.ownSP.getBoolean("simpleAnimation", false)) {
                    addView(SettingSwitch.FastBuilder(mText = "文件夹打开背景模糊", mKey = "blurWhenOpenFolder").build())
                }
                addView(SettingTextView.FastBuilder(mText = "应用卡片圆角大小") { showModifyRoundCorner() }.build())
                addView(SettingTextView.FastBuilder(mText = "应用图标与名称间距") { showModifyTextSize() }.build())
                addView(SettingTextView.FastBuilder(mText = "小部件相关", mColor = "#0C84FF" , mSize = SettingTextView.text2Size).build())
                addView(SettingSwitch.FastBuilder(mText = "隐藏小部件名称", mKey = "hideWidgetTitle").build())
                addView(SettingSwitch.FastBuilder(mText = "允许将安卓小部件移到负一屏", mKey = "widgetToMinus").build())
                addView(SettingSwitch.FastBuilder(mText = "允许在安卓小部件中显示MIUI小部件", mKey = "alwaysShowMIUIWidget").build())
                if (XposedInit.hasHookPackageResources) {
                    addView(SettingTextView.FastBuilder(mText = "资源钩子功能", mColor = "#0C84FF" , mSize = SettingTextView.text2Size).build())
                    addView(SettingSwitch.FastBuilder(mText = "隐藏桌面应用名称", mKey = "icons").build())
                    addView(SettingSwitch.FastBuilder(mText = "隐藏后台应用图标", mKey = "buttonPadding").build())
                    addView(SettingSwitch.FastBuilder(mText = "隐藏后台清理图标", mKey = "cleanUp").build())
                    addView(SettingSwitch.FastBuilder(mText = "隐藏后台小窗应用图标", mKey = "smallWindow").build())
                    addView(SettingTextView.FastBuilder(mText = "后台卡片文字大小") { showModifyBackgroundTextSize() }.build())
                }
                if (OwnSP.ownSP.getBoolean("testUser", false)) {
                    addView(SettingTextView.FastBuilder(mText = "实验性功能", mColor = "#0C84FF", mSize = SettingTextView.text2Size).build())
                    addView(SettingSwitch.FastBuilder(mText = "简单动画", mKey = "simpleAnimation").build())
                    addView(SettingSwitch.FastBuilder(mText = "真·横屏后台", mKey = "horizontal").build())
                    addView(SettingTextView.FastBuilder(mText = "纵向卡片大小") { showModifyVertical() }.build())
                    addView(SettingTextView.FastBuilder(mText = "横向卡片大小") { showModifyHorizontal() }.build())
                }
                addView(SettingTextView.FastBuilder(mText = "其他功能", mColor = "#0C84FF" , mSize = SettingTextView.text2Size).build())
                addView(SettingSwitch.FastBuilder(mText = "时钟常显", mKey = "clockGadget").build())
                addView(SettingSwitch.FastBuilder(mText = "搜索框模糊", mKey = "searchBarBlur").build())
                addView(SettingTextView.FastBuilder(mText = "模块相关", mColor = "#0C84FF" , mSize = SettingTextView.text2Size).build())
                addView(SettingSwitch.FastBuilder(mText = "实验性功能", mKey = "testUser").build())
                addView(SettingTextView.FastBuilder(mText = "清除用户配置") { editor.clear(); editor.commit(); exitProcess(0) }.build())
            })
        })
        dialogBuilder.setPositiveButton("关闭", null)
        dialogBuilder.setNeutralButton("重启桌面") { _, _ -> exitProcess(0) }
        dialogBuilder.setCancelable(false)
        dialogBuilder.show()
    }

    private fun showModifyRoundCorner() {
        SettingUserInput("应用卡片圆角大小", "recents_task_view_rounded_corners_radius", 0, 100, 1,20).build()
    }

    private fun showModifyTextSize() {
        SettingUserInput("应用图标与文字间距", "recents_task_view_header_height", 0, 200, 1,40).build()
    }

    private fun showModifyAnimationLevel() {
        SettingSeekBarDialog("手势动画速度", "animationLevel", 10, 500, canUserInput = true, defval = 100).build()
    }

    private fun showModifyBackgroundTextSize() {
        SettingUserInput("应用卡片文字大小", "backgroundTextSize", 0, 100, 1, 13).build()
    }

    private fun showModifyVertical() {
        SettingUserInput("纵向应用卡片大小", "task_vertical", 50, 150, 100,100).build()
    }

    private fun showModifyHorizontal() {
        SettingUserInput("横向应用卡片大小", "task_horizontal", 100, 1000, 1000,544).build()
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
                setPadding(
                    dp2px(HomeContext.context, 10f),
                    dp2px(HomeContext.context, 10f),
                    dp2px(HomeContext.context, 10f),
                    dp2px(HomeContext.context, 5f)
                )
                addView(SettingTextView.FastBuilder(
                    mText = "「后台模糊级别」",
                    mSize = SettingTextView.text2Size,
                    mColor = "#0C84FF")
                    .build()
                )
                addView(SettingTextView.FastBuilder(mText = "完整模糊") {
                    saveValue("COMPLETE")
                    onClick = it
                    dialog.dismiss()
                }.build())
                addView(SettingTextView.FastBuilder(mText = "测试模糊") {
                    saveValue("TEST")
                    onClick = it
                    dialog.dismiss()
                }.build())
                addView(SettingTextView.FastBuilder(mText = "简单模糊") {
                    saveValue("SIMPLE")
                    onClick = it
                    dialog.dismiss()
                }.build())
                addView(SettingTextView.FastBuilder(mText = "无模糊") {
                    saveValue("NONE")
                    onClick = it
                    dialog.dismiss()
                }.build())
            })
        })
        dialog = dialogBuilder.show().apply {
            setOnDismissListener {
                try {
                    LogUtil.toast("后台模糊级别已设为:${(onClick as TextView).text}")
                } catch (ignore: Exception) {}
            }
        }
    }

    private fun firstUseDialog() {
        val dialogBuilder = SettingBaseDialog().get().apply {
            setTitle("「欢迎」")
            setMessage("检测到你是第一次使用本模块，模块会进行默认值设定，并随后重启桌面\n如需进一步设置，请待桌面重启后再次打开桌面设置。")
            setOnDismissListener {
                OwnSP.set("blurLevel", "COMPLETE")
                OwnSP.set("smoothAnimation", true)
                OwnSP.set("animationLevel", 1.0f)
                OwnSP.set("isFirstUse", false)
                thread {
                    LogUtil.toast("桌面将在2秒后重启!")
                    Thread.sleep(2000)
                    exitProcess(0)
                }
            }
            setPositiveButton("确定", null)
            setCancelable(false)
        }
        dialogBuilder.show()
    }

}
