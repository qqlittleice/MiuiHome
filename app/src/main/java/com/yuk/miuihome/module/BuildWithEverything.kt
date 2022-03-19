package com.yuk.miuihome.module

import android.widget.Toast
import com.github.kyuubiran.ezxhelper.init.InitFields.appContext
import com.yuk.miuihome.utils.Config
import com.yuk.miuihome.utils.Config.AndroidSDK
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class BuildWithEverything {

    private fun readStream(input: InputStream) {
        val reader = BufferedReader(InputStreamReader(input))
        var read = ""
        while (true) {
            val temp: String = reader.readLine() ?: break
            read += temp
        }
        Toast.makeText(appContext, read, Toast.LENGTH_LONG).show()
    }

    fun init() {
        if (AndroidSDK >= 31) {
            try {
                readStream(Runtime.getRuntime().exec("su -c cmd package compile -m everything ${Config.hookPackage}").inputStream)
            } catch (ignore: Exception) {
            }
        } else {
            readStream(Runtime.getRuntime().exec("cmd package compile -m everything ${Config.hookPackage}").inputStream)
        }
    }
}