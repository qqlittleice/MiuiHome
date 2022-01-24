package com.yuk.miuihome.module.view

import android.app.Dialog
import android.content.Context
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.yuk.miuihome.R

class SettingsDialog(context: Context) : Dialog(context, R.style.CustomDialog) {
    var view: View

    init {
        window?.setGravity(Gravity.BOTTOM)
        view = createView(context, R.layout.dialog_layout)
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

    override fun setTitle(title: CharSequence?) {
        view.findViewById<TextView>(R.id.Title).text = title
    }

    override fun setTitle(titleId: Int) {
        view.findViewById<TextView>(R.id.Title).setText(titleId)
    }

    fun setLBText(text: CharSequence?) {
        view.findViewById<Button>(R.id.LButton).text = text
    }

    fun setLBText(textId: Int) {
        view.findViewById<Button>(R.id.LButton).setText(textId)
    }

    fun setRBText(text: CharSequence?) {
        view.findViewById<Button>(R.id.RButton).text = text
    }

    fun setRBText(textId: Int) {
        view.findViewById<Button>(R.id.RButton).setText(textId)
    }

    override fun show() {
        super.show()
        val layoutParams = window!!.attributes
        layoutParams.dimAmount = 0.3f
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        window!!.attributes = layoutParams
    }
}