package com.yuk.miuihome.utils

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import org.json.JSONObject

class SPBackup(private val activity: Activity, private val sp: SharedPreferences) {

    fun getWriteJson(): String {
        val json = JSONObject()
        val map = mutableMapOf<String, String>()
        sp.all.forEach { (key: String, any: Any?) ->
            when (any) {
                is Int -> { map[key] = "int;$any" }
                is Float -> { map[key] = "float;$any" }
                is String -> { map[key] = "string;$any" }
                is Boolean -> { map[key] = "boolean;$any" }
                is Long -> { map[key] = "long;$any" }
                is Double -> { map[key] = "double;$any" }
            }
        }
        val config = JSONObject(map.toMap())
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
                val rawValue = config[it] as String
                val type = rawValue.split(";")[0]
                val value = rawValue.split(";")[1]
                when (type) {
                    "int" -> { editor.putInt(it, value.toInt()) }
                    "float" -> { editor.putFloat(it, value.toFloat()) }
                    "string" -> { editor.putString(it, value) }
                    "boolean" -> { editor.putBoolean(it, value.toBoolean()) }
                    "long" -> { editor.putLong(it, value.toLong()) }
                    "double" -> { editor.putFloat(it, value.toFloat()) }
                    else -> { throw RuntimeException("key: $it value: $value type: ${value::class.java.typeName}") }
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