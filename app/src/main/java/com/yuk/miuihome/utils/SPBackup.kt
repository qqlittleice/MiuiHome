package com.yuk.miuihome.utils

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import org.json.JSONObject

class SPBackup(private val activity: Activity, private val sp: SharedPreferences) {

    fun getWriteJson(): String {
        val json = JSONObject()
        val config = JSONObject(sp.all)
        json.put("config", config)
        return json.toString()
    }

    fun convertJsonToSP(text: String): Boolean {
        try {
            val json = JSONObject(text)
            val config = json.getJSONObject("config")
            val editor = sp.edit()
            editor.clear()
            config.keys().forEach {
                when (val any = config[it]) {
                    is Int -> editor.putInt(it, any)
                    is Float -> editor.putFloat(it, any)
                    is String -> editor.putString(it, any)
                    is Boolean -> editor.putBoolean(it, any)
                    is Long -> editor.putLong(it, any)
                    is Double -> editor.putFloat(it, any.toFloat())
                    else -> { throw RuntimeException("key: $it value: $any type: ${any::class.java.typeName}") }
                }
            }
            editor.apply()
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun requestWriteToFile(fileName: String) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/plain"
            putExtra(Intent.EXTRA_TITLE, fileName)
        }
        activity.startActivityForResult(intent, createCode)
    }

    fun requestReadFromFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/plain"
        }
        activity.startActivityForResult(intent, readCode)
    }
    companion object {
        const val createCode = 2333
        const val readCode = 114514
    }
}