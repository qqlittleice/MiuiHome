package com.yuk.miuihome

import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object UpdatesManager {

    private val okHttpClient by lazy { OkHttpClient.Builder().build() }

    private const val api = "https://api.github.com/repos/Xposed-Modules-Repo/com.yuk.miuihome/releases/latest"

    suspend fun checkUpdates(): UpdatesInfo {
        try {
            val data = getApiJson()
                ?: return UpdatesInfo(
                    hasUpdates = false,
                    error = true
                )
            val tag = data.get("tag_name") as String
            val versionCode = tag.split("-")[0].toInt()
            if (versionCode <= BuildConfig.VERSION_CODE) {
                return UpdatesInfo(
                    hasUpdates = false
                )
            }
            return UpdatesInfo(
                hasUpdates = true,
                versionCode = versionCode,
                versionName = tag.split("-")[1],
                content = data.get("body") as String,
                downloadLink = ((data.get("assets") as JSONArray).get(0) as JSONObject).getString("browser_download_url")
            )
        } catch (e: Exception) {
            return UpdatesInfo(
                hasUpdates = false,
                error = true
            )
        }
    }

    private suspend fun getApiJson(): JSONObject? {
        return suspendCoroutine { continuation ->
            okHttpClient.newCall(
                Request.Builder()
                    .url(api)
                    .get()
                    .build())
                .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resume(null)
                }

                override fun onResponse(call: Call, response: Response) {
                    continuation.resume(JSONObject(response.body!!.string()))
                }
            })
        }
    }

}

data class UpdatesInfo(
    val hasUpdates: Boolean,
    val versionCode: Int? = null,
    val versionName: String? = null,
    val content: String? = null,
    val downloadLink: String? = null,
    val error: Boolean = false
)