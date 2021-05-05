package hk.qqlittleice.hook.miuihome

import android.app.Activity
import android.app.Application
import android.content.Context

object HomeContext {
    lateinit var application: Application
    val context by lazy { application }
    lateinit var classLoader: ClassLoader
    lateinit var activity: Activity
    lateinit var contextForView: Context
}