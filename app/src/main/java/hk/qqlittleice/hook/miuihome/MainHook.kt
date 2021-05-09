package hk.qqlittleice.hook.miuihome

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import hk.qqlittleice.hook.miuihome.Config.SP_NAME
import hk.qqlittleice.hook.miuihome.Config.hookPackage
import hk.qqlittleice.hook.miuihome.module.*
import hk.qqlittleice.hook.miuihome.utils.LogUtil
import hk.qqlittleice.hook.miuihome.utils.OwnSP
import hk.qqlittleice.hook.miuihome.utils.dp2px
import hk.qqlittleice.hook.miuihome.utils.ktx.getObjectField
import hk.qqlittleice.hook.miuihome.utils.ktx.hookAfterMethod
import hk.qqlittleice.hook.miuihome.utils.ktx.setObjectField
import hk.qqlittleice.hook.miuihome.view.*
import java.io.File
import kotlin.concurrent.thread

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
                    setObjectField("mTitle", "模块设置(资源钩子已生效)")
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
        //开启简单动画
        EnableSimpleAnimation().init()
        //动画速度调节
        ModifyAnimDurationRatio().init()
        //后台卡片圆角大小调节
        ModifyRoundedCorners().init()
        //后台卡片图标文字间距调节
        ModifyHeaderHeight().init()
        //纵向后台卡片大小
        ModifyTaskVertical().init()
        //横向后台卡片大小
        ModifyTaskHorizontal().init()
        //进入后台是否隐藏状态栏
        EnableHideStatusBarWhenEnterRecents().init()

        TestHook().init()
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
                setPadding(dp2px(HomeContext.context, 20f), dp2px(HomeContext.context, 10f), dp2px(HomeContext.context, 20f), dp2px(HomeContext.context, 5f))
                addView(SettingTextView.FastBuilder(mText = "MiuiHome", mSize = SettingTextView.titleSize).build())
                addView(SettingTextView.FastBuilder(mText = "模糊设置", mColor = "#0C84FF",mSize = SettingTextView.text2Size).build())
                addView(SettingTextView.FastBuilder(mText = "后台模糊级别") { showModifyBlurLevel() }.build())
                addView(SettingTextView.FastBuilder(mText = "特效设置", mColor = "#0C84FF",mSize = SettingTextView.text2Size).build())
                addView(SettingSwitch.FastBuilder(mText = "平滑动画", mKey = "smoothAnimation").build())
                addView(SettingSwitch.FastBuilder(mText = "文件夹模糊", mKey = "blurWhenOpenFolder").build())
                addView(SettingSwitch.FastBuilder(mText = "水波纹下载特效", mKey = "mamlDownload").build())
                addView(SettingTextView.FastBuilder(mText = "后台设置", mColor = "#0C84FF",mSize = SettingTextView.text2Size).build())
                addView(SettingTextView.FastBuilder(mText = "动画速度") { showModifyAnimationLevel() }.build())
                addView(SettingSwitch.FastBuilder(mText = "隐藏状态栏", mKey = "hideStatusBar").build())
                addView(SettingTextView.FastBuilder(mText = "卡片圆角大小") { showModifyRoundCorner() }.build())
                addView(SettingTextView.FastBuilder(mText = "纵向(瀑布)卡片大小") { showModifyVertical() }.build())
                addView(SettingTextView.FastBuilder(mText = "横向(平铺)卡片大小") { showModifyHorizontal() }.build())
                addView(SettingTextView.FastBuilder(mText = "应用图标与文字间距") { showModifyTextSize() }.build())
                addView(SettingTextView.FastBuilder(mText = "扩展设置", mColor = "#0C84FF" ,mSize = SettingTextView.text2Size).build())
                addView(SettingSwitch.FastBuilder(mText = "时钟常显", mKey = "clockGadget").build())
                addView(SettingSwitch.FastBuilder(mText = "简单动画", mKey = "simpleAnimation").build())
                addView(SettingTextView.FastBuilder(mText = "模块相关") { showHookSetting() }.build())
            })
        })
        dialogBuilder.setPositiveButton("关闭", null)
        dialogBuilder.setNeutralButton("重启系统桌面") { _, _ -> System.exit(0) }
        if (XposedInit.hasHookPackageResources) {
            dialogBuilder.setNegativeButton("资源钩子设置") { _, _ -> showResHookedDialog() }
        }
        dialogBuilder.setCancelable(false)
        dialogBuilder.show()
    }

    private fun showModifyBackgroundTextSize() {
        SettingUserInput("后台卡片文字大小", "backgroundTextSize", 0, 100, 1, 13).build()
    }

    private fun showResHookedDialog() {
        val dialogBuilder = SettingBaseDialog().get()
        dialogBuilder.setView(ScrollView(HomeContext.activity).apply {
            overScrollMode = 2
            addView(LinearLayout(HomeContext.activity).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dp2px(HomeContext.context, 20f), dp2px(HomeContext.context, 10f), dp2px(HomeContext.context, 20f), dp2px(HomeContext.context, 5f))
                addView(SettingTextView.FastBuilder(mText = "资源钩子", mSize = SettingTextView.titleSize).build())
                addView(SettingSwitch.FastBuilder(mText = "解锁桌面图标布局", mKey = "cellCount").build())
                addView(SettingTextView.FastBuilder(mText = "后台卡片文字大小") { showModifyBackgroundTextSize() }.build())
            })
        })
        dialogBuilder.setPositiveButton("返回") { _, _ -> showSettingDialog()}
        dialogBuilder.show()
    }

    private fun showHookSetting() {
        val dialogBuilder = SettingBaseDialog().get()
        dialogBuilder.setView(ScrollView(HomeContext.activity).apply {
            overScrollMode = 2
            addView(LinearLayout(HomeContext.activity).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dp2px(HomeContext.context, 20f), dp2px(HomeContext.context, 10f), dp2px(HomeContext.context, 20f), dp2px(HomeContext.context, 5f))
                addView(SettingTextView.FastBuilder(mText = "清除模块配置") { File("/data/data/$hookPackage/shared_prefs/${SP_NAME}.xml").delete(); System.exit(0) }.build())
                addView(SettingSwitch.FastBuilder(mText = "测试功能", mKey = "TESTONLY") { SettingInformationDialog("该功能仅供开发测试使用，如果不知道用途请不要开启！", 5, "TESTONLY").build() }.build())
            })
        })
        dialogBuilder.show()
    }

    private fun showModifyRoundCorner() {
        SettingUserInput("卡片圆角大小", "recents_task_view_rounded_corners_radius", 0, 100, 1,20).build()
    }

    private fun showModifyTextSize() {
        SettingUserInput("图标文字间距", "recents_task_view_header_height", 0, 200, 1,40).build()
    }

    private fun showModifyVertical() {
        SettingUserInput("纵向卡片大小", "task_vertical", 50, 150, 100,100).build()
    }

    private fun showModifyHorizontal() {
        SettingUserInput("横向卡片大小", "task_horizontal", 100, 1000, 1000,544).build()
    }

    private fun showModifyAnimationLevel() {
        SettingSeekBarDialog("动画速度", "animationLevel", 10, 500, canUserInput = true, defval = 100).build()
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
                setPadding(dp2px(HomeContext.context, 20f), dp2px(HomeContext.context, 10f), dp2px(HomeContext.context, 20f), dp2px(HomeContext.context, 5f))
                addView(SettingTextView.FastBuilder(mText = "完整模糊", mSize = SettingTextView.textSize) {
                    saveValue("COMPLETE")
                    dialog.dismiss()
                    onClick = it
                }.build())
                addView(SettingTextView.FastBuilder(mText = "测试模糊", mSize = SettingTextView.textSize) {
                    saveValue("TEST")
                    dialog.dismiss()
                    onClick = it
                }.build())
                addView(SettingTextView.FastBuilder(mText = "简单模糊", mSize = SettingTextView.textSize) {
                    saveValue("SIMPLE")
                    dialog.dismiss()
                    onClick = it
                }.build())
                addView(SettingTextView.FastBuilder(mText = "无模糊", mSize = SettingTextView.textSize) {
                    saveValue("NONE")
                    dialog.dismiss()
                    onClick = it
                }.build())
            })
        })
        dialog = dialogBuilder.show().apply {
            setOnDismissListener {
                try {
                    LogUtil.toast("后台模糊等级设置为：${(onClick as TextView).text}")
                } catch (ignore: Exception) {}
            }
        }
    }


    private fun firstUseDialog() {
        val dialogBuilder = SettingBaseDialog().get().apply {
            setTitle("欢迎")
            setMessage("检测到你是第一次使用本模块，模块会进行默认值设定，并随后重启系统桌面\n如需进一步设置，请待桌面重启后再次打开桌面设置")
            setOnDismissListener {
                OwnSP.set("blurLevel", "COMPLETE")
                OwnSP.set("smoothAnimation", true)
                OwnSP.set("animationLevel", 1.0f)
                OwnSP.set("hideStatusBar", true)
                OwnSP.set("isFirstUse", false)
                OwnSP.set("cellCount", false)
                thread {
                    LogUtil.toast("系统桌面将会在3秒后重启!")
                    Thread.sleep(3000)
                    System.exit(0)
                }
            }
            setPositiveButton("确定", null)
            setCancelable(false)
        }
        dialogBuilder.show()
    }

}