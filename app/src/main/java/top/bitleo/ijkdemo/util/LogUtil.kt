package top.bitleo.ijkdemo.util

import android.util.Log
import top.bitleo.ijkdemo.BuildConfig


object LogUtil {

    private const val debug = true


    fun d(TAG: String, msg: String) {
        if (debug&& BuildConfig.DEBUG) {
            Log.d(TAG, "ssr-debug: $msg")

        }
    }

    fun i(TAG: String, msg: String) {
        Log.i(TAG, "ssr-info: $msg")
    }

    fun e(TAG: String, msg: String) {
        Log.e(TAG, "ssr-error: $msg")
    }
}
