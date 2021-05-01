package hk.qqlittleice.hook.miuihome.view

import android.app.AlertDialog
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.SeekBar
import android.widget.TextView
import hk.qqlittleice.hook.miuihome.HomeContext
import hk.qqlittleice.hook.miuihome.utils.OwnSP
import hk.qqlittleice.hook.miuihome.utils.dp2px

class SettingSeekBarDialog(private val mText: String, private val mKey: String, private val minValue: Int, private val maxValue: Int, private val minText: String, private val maxText: String) {

    private val sharedPreferences = OwnSP.ownSP
    private val editor by lazy { sharedPreferences.edit() }

    fun build(): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(HomeContext.activity)
        var nowValue: Float = sharedPreferences.getFloat(mKey, 0f)
        lateinit var valueTextView: TextView
        fun saveValue(value: Float) {
            nowValue = value
            editor.putFloat(mKey, value)
            editor.apply()
        }
        dialogBuilder.setView(ScrollView(HomeContext.activity).apply {
            overScrollMode = 2
            addView(LinearLayout(HomeContext.activity).apply {
                orientation = LinearLayout.VERTICAL
                addView(SettingTextView.FastBuilder(mText = mText, mSize = SettingTextView.text2Size).build())
                addView(SeekBar(HomeContext.context).apply {
                    min = minValue
                    max = maxValue
                    progress = (nowValue * 100).toInt()
                    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                            saveValue(progress.toFloat() / 100)
                            valueTextView.text = "${nowValue}f"
                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                    })
                })
                addView(LinearLayout(HomeContext.context).apply {
                    addView(TextView(HomeContext.context).apply {
                        text = minText
                        layoutParams = LinearLayout.LayoutParams(70, LinearLayout.LayoutParams.MATCH_PARENT)
                    })
                    addView(TextView(HomeContext.context).apply {
                        text = "${nowValue}f"
                        weightSum = 1f
                        textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                        valueTextView = this
                        layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
                    })
                    addView(TextView(HomeContext.context).apply {
                        text = maxText
                        textAlignment = TextView.TEXT_ALIGNMENT_TEXT_END
                        layoutParams = LinearLayout.LayoutParams(70, LinearLayout.LayoutParams.MATCH_PARENT)
                    })
                    layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    gravity = Gravity.CENTER_VERTICAL
                    (this.layoutParams as LinearLayout.LayoutParams).apply {
                        topMargin = dp2px(HomeContext.context, 5f)
                        setPaddingRelative(dp2px(HomeContext.context, 12f), dp2px(HomeContext.context, 6f), dp2px(HomeContext.context, 12f), dp2px(HomeContext.context, 5f))
                    }
                })
            })
        })
        return dialogBuilder.show()
    }
}
