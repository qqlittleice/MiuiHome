package com.yuk.miuihome.app

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import com.yuk.miuihome.BuildConfig
import com.yuk.miuihome.R
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import kotlin.concurrent.thread

class UpdateActivity: Activity() {

    private val okHttpClient by lazy { OkHttpClient.Builder().build() }
    private val api = "https://api.github.com/repos/Xposed-Modules-Repo/com.yuk.miuihome/releases/latest"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val baseView = LinearLayout(this)
        baseView.orientation = LinearLayout.VERTICAL

        val nowVersionText = TextView(this)
        nowVersionText.text = "${getString(R.string.app_name)} ${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE}-${BuildConfig.BUILD_TYPE})"
        baseView.addView(nowVersionText)

        val progress = ProgressBar(this)
        progress.visibility = View.GONE
        baseView.addView(progress)

        val errorText = TextView(this)
        errorText.visibility = View.GONE

        val checkUpdatesButton = Button(this)
        checkUpdatesButton.text = "Check Updates"
        checkUpdatesButton.setOnClickListener { button ->
            progress.visibility = View.VISIBLE
            button.isClickable = false
            thread {
                okHttpClient.newCall(
                    Request.Builder()
                        .url(api)
                        .get()
                        .build()
                ).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        runOnUiThread {
                            Toast.makeText(this@UpdateActivity, "error", Toast.LENGTH_SHORT).show()
                            progress.visibility = View.GONE
                            errorText.text = e.message
                            errorText.visibility = View.VISIBLE
                            button.isClickable = true
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val data = JSONObject(response.body!!.string())
                        val tag = data.get("tag_name") as String
                        val versionCode = tag.split("-")[0].toInt()
                        val versionName = tag.split("-")[1]
                        if (versionCode <= BuildConfig.VERSION_CODE) {
                            runOnUiThread { Toast.makeText(this@UpdateActivity, "No Updates", Toast.LENGTH_SHORT).show() }
                        } else {
                            val content = data.get("body") as String
                            val downloadLink = ((data.get("assets") as JSONArray).get(0) as JSONObject).getString("browser_download_url")
                            runOnUiThread {
                                AlertDialog.Builder(this@UpdateActivity).apply {
                                    setTitle("${getString(R.string.app_name)} ${versionName}(${versionCode})")
                                    setMessage(content)
                                    setPositiveButton("Download") { _, _ ->
                                        val intent = Intent(Intent.ACTION_VIEW)
                                        intent.addCategory(Intent.CATEGORY_BROWSABLE)
                                        intent.data = Uri.parse(downloadLink)
                                        startActivity(intent)
                                    }
                                    setNegativeButton("Dismiss", null)
                                    setNeutralButton("Download With CDN") { _, _ ->
                                        val intent = Intent(Intent.ACTION_VIEW)
                                        intent.addCategory(Intent.CATEGORY_BROWSABLE)
                                        intent.data = Uri.parse(downloadLink.replace("github.com", "github.com.cnpmjs.org"))
                                        startActivity(intent)
                                    }
                                    show()
                                }
                            }
                        }
                        runOnUiThread {
                            progress.visibility = View.GONE
                            errorText.visibility = View.GONE
                            button.isClickable = true
                        }
                    }
                })
            }
        }
        baseView.addView(checkUpdatesButton)
        baseView.addView(errorText)

        setContentView(baseView)
    }

}