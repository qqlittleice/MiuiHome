package com.yuk.miuihome.app

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.yuk.miuihome.BuildConfig
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class UpdateService : Service() {

    private val okHttpClient by lazy { OkHttpClient.Builder().build() }
    private val api = "https://api.github.com/repos/Xposed-Modules-Repo/com.yuk.miuihome/releases/latest"
    private val binder = UpdateBinder()

    fun checkHasUpdates(callback: UpdateCallback) {
        callback.onStart()

        okHttpClient.newCall(
            Request.Builder()
                .url(api)
                .get()
                .build()
        ).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
                callback.onFinish()
            }

            override fun onResponse(call: Call, response: Response) {
                val data = JSONObject(response.body!!.string())
                val tag = data.get("tag_name").toString().split("-")
                if (tag.size != 2) {
                    callback.onError(RuntimeException("size of tag is ${tag.size} but expect 2"))
                    callback.onFinish()
                    return
                }
                val versionCode = tag[0].toInt()
                val versionName = tag[1]
                if (versionCode <= BuildConfig.VERSION_CODE) {
                    callback.noUpdates()
                } else {
                    try {
                        val content = data.get("body").toString()
                        val assets = data.get("assets") as JSONArray
                        val assetsMap = mutableMapOf<String, String>()
                        for (i in 0 until assets.length()) {
                            (assets.get(i) as JSONObject).also {
                                assetsMap[it.getString("name")] = it.getString("browser_download_url")
                            }
                        }
                        callback.hasUpdates(UpdateInfo(
                            versionCode,
                            versionName,
                            assetsMap,
                            content
                        ))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }
                callback.onFinish()
            }
        })
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    inner class UpdateBinder : Binder() {
        fun getService(): UpdateService = this@UpdateService
    }

    interface UpdateCallback {
        // method is called when start checking updates
        fun onStart()

        // method is called when checked updates
        fun onFinish()

        // method is called if has updates
        fun hasUpdates(info: UpdateInfo)

        // method is called if has no updates
        fun noUpdates()

        // method is called if error happened
        fun onError(e: Exception)
    }

    data class UpdateInfo(
        val versionCode: Int,
        val versionName: String,
        val assets: Map<String, String>,
        val content: String
    )

}