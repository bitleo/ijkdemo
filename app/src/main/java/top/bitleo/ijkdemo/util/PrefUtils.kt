package top.bitleo.ijkdemo.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

import top.bitleo.ijkdemo.ClientApplication


object PrefUtils {


    val VIDEO_URL_ADDRRESS = "video_url_address"

    val defaultSp: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(ClientApplication.app)


    operator fun set(key: String, value: Any) {
        if (key.isBlank() || value == null) {
            throw NullPointerException(String.format("Key and value not be null key=%s, value=%s", key, value))
        }
        val edit = defaultSp.edit()
        if (value is String) {
            edit.putString(key, value)
        } else if (value is Int) {
            edit.putInt(key, value)
        } else if (value is Long) {
            edit.putLong(key, value)
        } else if (value is Boolean) {
            edit.putBoolean(key, value)
        } else if (value is Float) {
            edit.putFloat(key, value)
        } else if (value is Set<*>) {
            edit.putStringSet(key, value as Set<String>)
        } else {
            throw IllegalArgumentException(String.format("Type of value unsupported key=%s, value=%s", key, value))
        }
        edit.apply()
    }



    fun getDefaultSp(context: Context?): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }


    fun getVideoUrl():String{
        return getDefaultSp(ClientApplication.app).getString(VIDEO_URL_ADDRRESS,"http://vjs.zencdn.net/v/oceans.mp4").toString()
    }


}
