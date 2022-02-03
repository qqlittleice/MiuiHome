package com.yuk.miuihome.view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.yuk.miuihome.XposedInit
import com.yuk.miuihome.utils.HomeContext

open class TransferActivity: Activity() {

    class FixedClassLoader(private val mModuleClassLoader: ClassLoader, private val mHostClassLoader: ClassLoader): ClassLoader(mBootstrap) {

        companion object {
            private val mBootstrap: ClassLoader = Context::class.java.classLoader!!
        }

        override fun loadClass(name: String, resolve: Boolean): Class<*> {
            runCatching {
                return mBootstrap.loadClass(name)
            }

            runCatching {
                if ("androidx.lifecycle.ReportFragment" == name) {
                    return mHostClassLoader.loadClass(name)
                }
            }

            return try {
                mModuleClassLoader.loadClass(name)
            } catch (e: Exception) {
                mHostClassLoader.loadClass(name)
            }
        }

    }

    override fun getClassLoader(): ClassLoader {
        return FixedClassLoader(XposedInit::class.java.classLoader!!, HomeContext.classLoader)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        val windowState = savedInstanceState.getBundle("android:viewHierarchyState")
        windowState?.let {
            it.classLoader = TransferActivity::class.java.classLoader
        }
        super.onRestoreInstanceState(savedInstanceState)
    }
}