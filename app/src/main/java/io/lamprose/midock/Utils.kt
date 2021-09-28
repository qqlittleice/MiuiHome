package io.lamprose.midock

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import com.github.kyuubiran.ezxhelper.init.InitFields.appContext
import com.github.kyuubiran.ezxhelper.utils.Log

object Utils {
    private const val DATA_FILE_NAME = "MIDockConfig"

    fun dip2px(dpValue: Int): Int {
        return (dpValue * appContext.resources.displayMetrics.density + 0.5f).toInt()
    }

    fun px2dip(pxValue: Int): Int {
        return (pxValue / appContext.resources.displayMetrics.density + 0.5f).toInt()
    }

    fun sp2px(spValue: Float): Float {
        return spValue * appContext.resources.displayMetrics.scaledDensity + 0.5f
    }

    fun getEditor(): SharedPreferences.Editor? {
        return try {
            appContext.getSharedPreferences(DATA_FILE_NAME, Context.MODE_PRIVATE).edit()
        } catch (e: Exception) {
            null
        }
    }

    fun saveData(key: String, value: Any) {
        try {
            val sharedPreferences =
                appContext.getSharedPreferences(DATA_FILE_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            when (value) {
                is Int -> editor.putInt(key, value)
                is Float -> editor.putFloat(key, value)
                is String -> editor.putString(key, value)
                is Boolean -> editor.putBoolean(key, value)
                is Long -> editor.putLong(key, value)
            }
            editor.apply()
        } catch (e: Exception) {
            // 也许是模块尚未加载
            Log.e(e, "saveData")
        }
    }

    fun getData(key: String, defValue: Int): Int {
        return try {
            val sharedPreferences =
                appContext.getSharedPreferences(DATA_FILE_NAME, Context.MODE_PRIVATE)
            sharedPreferences.getInt(key, defValue)
        } catch (e: Throwable) {
            // 也许是模块尚未加载
            defValue
        }
    }

    fun getVersionName(): String? {
        return try {
            val packageManager: PackageManager = appContext.packageManager
            packageManager.getPackageInfo(appContext.packageName, 0).versionName
        } catch (e: Exception) {
            Log.e(e, "getVersionName")
            null
        }
    }

    fun getVersionCode(): Long {
        return try {
            val packageManager: PackageManager = appContext.packageManager
            packageManager.getPackageInfo(appContext.packageName, 0).longVersionCode
        } catch (e: Exception) {
            Log.e(e, "getVersionName")
            -1L
        }
    }
}