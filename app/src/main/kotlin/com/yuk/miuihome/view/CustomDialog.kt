package com.yuk.miuihome.view

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.yuk.miuihome.R
import com.yuk.miuihome.XposedInit
import com.yuk.miuihome.utils.ktx.dp2px

class CustomDialog(context: Context) : Dialog(context, if (XposedInit().checkIsPadDevice()) R.style.CustomPadDialog else R.style.CustomDialog) {
    var view: View

    init {
        if (XposedInit().checkIsPadDevice()) {
            view = createView(context, R.layout.dialog_pad_layout)
            window!!.attributes.width = dp2px(380f)
            window!!.setGravity(Gravity.CENTER)
        } else {
            view = createView(context, R.layout.dialog_layout)
            window!!.attributes.width = WindowManager.LayoutParams.MATCH_PARENT
            window!!.setGravity(Gravity.BOTTOM)
        }
    }

    private fun createView(context: Context, dialog_layout: Int): View {
        val inflate: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflate.inflate(dialog_layout, null)
        setContentView(view)
        return view
    }

    fun addView(mView: View) {
        view.findViewById<LinearLayout>(R.id.View).addView(mView)
    }

    fun addView(mView: View, index: Int) {
        view.findViewById<LinearLayout>(R.id.View).addView(mView, index)
    }
    fun addView(mView: View, width: Int, height: Int) {
        view.findViewById<LinearLayout>(R.id.View).addView(mView, width, height)
    }
    fun addView(mView: View, params: ViewGroup.LayoutParams) {
        view.findViewById<LinearLayout>(R.id.View).addView(mView, params)
    }
    fun addView(mView: View, index: Int, params: ViewGroup.LayoutParams) {
        view.findViewById<LinearLayout>(R.id.View).addView(mView, index, params)
    }

    override fun setTitle(title: CharSequence?) {
        view.findViewById<TextView>(R.id.Title).text = title
    }

    override fun setTitle(titleId: Int) {
        view.findViewById<TextView>(R.id.Title).setText(titleId)
    }

    fun setRButton(text: CharSequence?, callBacks: (it: View) -> Unit) {
        view.findViewById<Button>(R.id.RButton).apply {
            setText(text)
            visibility = View.VISIBLE
            setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) it.performHapticFeedback(HapticFeedbackConstants.CONFIRM, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
                else it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
                callBacks(it)
            }
        }
    }

    fun setRButton(textId: Int, callBacks: () -> Unit) {
        view.findViewById<Button>(R.id.RButton).apply {
            setText(textId)
            visibility = View.VISIBLE
            setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) it.performHapticFeedback(HapticFeedbackConstants.CONFIRM, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
                else it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
                callBacks()
            }
        }
    }

    fun setCButton(text: CharSequence?, callBacks: (it: View) -> Unit) {
        view.findViewById<Button>(R.id.CButton).apply {
            setText(text)
            visibility = View.VISIBLE
            setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) it.performHapticFeedback(HapticFeedbackConstants.CONFIRM, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
                else it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
                callBacks(it)
            }
        }
    }

    fun setCButton(textId: Int, callBacks: () -> Unit) {
        view.findViewById<Button>(R.id.CButton).apply {
            setText(textId)
            visibility = View.VISIBLE
            setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) it.performHapticFeedback(HapticFeedbackConstants.CONFIRM, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
                else it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
                callBacks()
            }
        }
    }

    fun setLButton(text: CharSequence?, callBacks: (it: View) -> Unit) {
        view.findViewById<Button>(R.id.LButton).apply {
            setText(text)
            visibility = View.VISIBLE
            setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) it.performHapticFeedback(HapticFeedbackConstants.REJECT, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
                else it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
                callBacks(it)
            }
            visibility = View.VISIBLE
        }
    }

    fun setLButton(textId: Int, callBacks: () -> Unit) {
        view.findViewById<Button>(R.id.LButton).apply {
            setText(textId)
            visibility = View.VISIBLE
            setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) it.performHapticFeedback(HapticFeedbackConstants.REJECT, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
                else it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
                callBacks()
            }
            visibility = View.VISIBLE
        }
    }

    fun setMessage(textId: Int) {
        view.findViewById<TextView>(R.id.Message).apply {
            setText(textId)
            textAlignment = View.TEXT_ALIGNMENT_VIEW_START
            visibility = View.VISIBLE
        }
    }

    fun setMessage(text: CharSequence?) {
        view.findViewById<TextView>(R.id.Message).apply {
            this.text = text
            textAlignment = View.TEXT_ALIGNMENT_VIEW_START
            visibility = View.VISIBLE
        }
    }

    fun setEditText(text: String, hint: String) {
        view.findViewById<EditText>(R.id.EditText).apply {
            setText(text.toCharArray(), 0, text.length)
            this.hint = hint
            textAlignment = View.TEXT_ALIGNMENT_VIEW_START
            visibility = View.VISIBLE
        }
    }

    fun getEditText(): String = view.findViewById<EditText>(R.id.EditText).text.toString()

    override fun show() {
        super.show()
        val lp = window!!.attributes
        if (Build.VERSION.SDK_INT >= 31) {
            window!!.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
            lp.blurBehindRadius = 45
            lp.dimAmount = 0.18f
            window!!.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        } else {
            lp.dimAmount = 0.3f
            window!!.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
    }

}