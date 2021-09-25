package com.yuk.miuihome.custom.hook

import android.widget.EditText

class CustomHookDataManager {
    private fun setClassName(editText: EditText) {}
    private fun setMethodName(editText: EditText) {}
    private fun setArgs(array: Array<EditText>) {}
    private fun setResult(type: EditText, editText: EditText) {}
    private fun saveAndApply() {}
}

data class CustomHookData(
    var className: String?,
    var methodName: String?,
    var args: Array<Any>,
    var result: Any?
)
