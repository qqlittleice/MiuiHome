package hk.qqlittleice.hook.miuihome

import android.app.Activity
import android.app.Application
import android.content.Context

object HomeContext {
    lateinit var application: Application
    lateinit var context: Context
    lateinit var classLoader: ClassLoader
    lateinit var activity: Activity
    lateinit var myRes: ResHook
}