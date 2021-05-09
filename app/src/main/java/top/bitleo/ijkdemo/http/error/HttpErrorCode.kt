package top.bitleo.ijkdemo.http.error

import androidx.annotation.StringRes
import top.bitleo.ijkdemo.ClientApplication
import top.bitleo.ijkdemo.R


import java.util.HashMap

/**
 *
 * Time: 2019/5/15
 */

object HttpErrorCode {

    /**
     * No cache and network available.
     */
    val NO_CACHE_AND_NETWORK = 0
    val PAGE_NOT_FOUND = 1
    val UNAUTHORIZED = 2

    private val ERROR_MSG_MAP = HashMap<Int, String>()

    init {
        ERROR_MSG_MAP[NO_CACHE_AND_NETWORK] =
            getString(R.string.no_cache_and_network)
        ERROR_MSG_MAP[PAGE_NOT_FOUND] =
            getString(R.string.page_no_found)
        ERROR_MSG_MAP[UNAUTHORIZED] =
            getString(R.string.unauthorized)
    }

    fun getErrorMsg(errorCode: Int): String? {
        return ERROR_MSG_MAP[errorCode]
    }

    private fun getString(@StringRes resId: Int): String {
        return ClientApplication.app!!.resources.getString(resId)
    }

}
