package com.zhenxiang.blur

import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewRootImpl
import androidx.annotation.RequiresApi
import com.android.internal.graphics.drawable.BackgroundBlurDrawable
import org.lsposed.hiddenapibypass.HiddenApiBypass

@RequiresApi(Build.VERSION_CODES.S)
fun View.createBackgroundBlurDrawable(): BackgroundBlurDrawable? {

    return try {
        val viewRootImpl = HiddenApiBypass.invoke(
            View::class.java,
            this,
            "getViewRootImpl" /*, args*/
        ) as ViewRootImpl

        HiddenApiBypass.invoke(
            ViewRootImpl::class.java,
            viewRootImpl,
            "createBackgroundBlurDrawable" /*, args*/
        ) as BackgroundBlurDrawable
    } catch (e: Exception) {
        Log.w(null, e)
        null
    }
}