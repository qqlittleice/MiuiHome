package com.yuk.miuihome.module

import android.widget.Toast
import com.yuk.miuihome.HomeContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class ModifyDT2W {

    private fun readStream(input: InputStream) {
        val reader = BufferedReader(InputStreamReader(input))
        var read = ""
        while (true) {
            val temp: String = reader.readLine() ?: break
            read += temp
        }
        Toast.makeText(HomeContext.context, read, Toast.LENGTH_LONG).show()
    }

    fun init() {
        //if (isDoubleTap) {
            readStream(
                Runtime.getRuntime()
                    .exec("su -c input keyevent 26").inputStream
            )
        //}
    }
}