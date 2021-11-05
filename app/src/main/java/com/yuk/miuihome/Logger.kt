package com.yuk.miuihome

import android.util.Log
import de.robv.android.xposed.XposedBridge

object Logger {

    private const val maxLength = 4000

    @JvmStatic
    fun log(obj: Any) {
        val content = if (obj is Throwable) Log.getStackTraceString(obj) else obj.toString()
        if (content.length > maxLength) {
            val chunkCount = content.length / maxLength
            for (i in 0..chunkCount) {
                val max = 4000 * (i + 1)
                if (max >= content.length) {
                    XposedBridge.log(content.substring(maxLength * i))
                } else {
                    XposedBridge.log(content.substring(maxLength * i, max))
                }
            }
        } else {
            XposedBridge.log(content)
        }
    }
}
