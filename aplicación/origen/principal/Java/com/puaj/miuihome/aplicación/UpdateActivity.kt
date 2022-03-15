Depackage com.yuk.miuihome.app

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.*
import com.yuk.miuihome.BuildConfig
import com.yuk.miuihome.R

class UpdateActivity: Activity() {

    lateinit var mService: UpdateService
    private var mBound = false
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName, p1: IBinder) {
            mService = (p1 as UpdateService.UpdateBinder).getService()
            mBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            mBound = false
        }
    }

    private fun dip2px(dpValue: Int): Int = (dpValue * resources.displayMetrics.density + 0.5f).toInt()
    private fun sp2px(spValue: Float): Float = (spValue * resources.displayMetrics.scaledDensity + 0.5f)

    private fun bindService() {
        Intent(this, UpdateService::class.java).also { intent ->
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun safeToast(string: CharSequence, duration: Int) = runOnUiThread { Toast.makeText(this, string, duration).show() }

    private fun downloadLinkViewParser(map: Map<String, String>, useCDN: Boolean = false): View {
        val scrollView = ScrollView(this)
        scrollView.overScrollMode = 2
        scrollView.addView(LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(
                dip2px(14),
                dip2px(10),
                dip2px(14),
                dip2px(10)
            )
            for ((name, link) in map) {
                addView(TextView(this@UpdateActivity).apply {
                    text = name
                    textSize = sp2px(6f)
                    setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.addCategory(Intent.CATEGORY_BROWSABLE)
                        intent.data = Uri.parse(if (useCDN) link.replace("github.com", "github.com.cnpmjs.org") else link)
                        startActivity(intent)
                    }
                })
            }
        })
        return scrollView
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindService()

        val baseView = LinearLayout(this)
        baseView.orientation = LinearLayout.VERTICAL

        val nowVersionText = TextView(this)
        nowVersionText.text = "${getString(R.string.app_name)} ${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE}-${BuildConfig.BUILD_TYPE})"
        baseView.addView(nowVersionText)

        val progress = ProgressBar(this)
        progress.visibility = View.GONE
        baseView.addView(progress)

        val errorText = TextView(this)

        val checkUpdatesButton = Button(this)
        checkUpdatesButton.text = "Check Updates"
        checkUpdatesButton.setOnClickListener { button ->
            if (mBound) {
                mService.checkHasUpdates(object : UpdateService.UpdateCallback {
                    override fun onStart() {
                        button.isClickable = false
                        runOnUiThread {
                            progress.visibility = View.VISIBLE
                            errorText.text = ""
                        }
                    }

                    override fun onFinish() {
                        button.isClickable = true
                        runOnUiThread { progress.visibility = View.GONE }
                    }

                    override fun hasUpdates(info: UpdateService.UpdateInfo) {
                        runOnUiThread {
                            AlertDialog.Builder(this@UpdateActivity).apply {
                                setTitle("${getString(R.string.app_name)} ${info.versionName}(${info.versionCode})")
                                setMessage(info.content)
                                setNegativeButton("Dismiss", null)
                                setPositiveButton("Download") { dialog, _ ->
                                    dialog.dismiss()
                                    AlertDialog.Builder(this@UpdateActivity).also {
                                        it.setView(downloadLinkViewParser(info.assets))
                                        it.show()
                                    }
                                }
                                setNeutralButton("Download With CDN") { dialog, _ ->
                                    dialog.dismiss()
                                    AlertDialog.Builder(this@UpdateActivity).also {
                                        it.setView(downloadLinkViewParser(info.assets, true))
                                        it.show()
                                    }
                                }
                                show()
                            }
                        }
                    }

                    override fun noUpdates() {
                        safeToast("No Updates", Toast.LENGTH_SHORT)
                    }

                    override fun onError(e: Exception) {
                        safeToast("error", Toast.LENGTH_SHORT)
                        runOnUiThread { errorText.text = e.message }
                    }
                })
            } else {
                safeToast("Service doesn't bind", Toast.LENGTH_SHORT)
                bindService()
            }
        }
        baseView.addView(checkUpdatesButton)
        baseView.addView(errorText)

        setContentView(baseView)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
        mBound = false
    }

}
