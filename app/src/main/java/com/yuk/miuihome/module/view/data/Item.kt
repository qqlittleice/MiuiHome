package com.yuk.miuihome.module.view.data

import android.view.View
import android.widget.CompoundButton
import com.yuk.miuihome.module.view.emptyCheckChangedListener
import com.yuk.miuihome.module.view.emptyTextClickListener

data class Item(
    val text: String,
    val switchKey: String = "",
    val isTitle: Boolean = false,
    val onSwitchCheckedChangeListener: CompoundButton.OnCheckedChangeListener = emptyCheckChangedListener,
    val onTextClickedListener: View.OnClickListener = emptyTextClickListener
)
