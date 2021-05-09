package top.bitleo.ijkdemo.http.core

/**
 *
 * Time: 2019/5/14
 */

class HttpResponse<T : Any>(private val oriResponse: retrofit2.Response<T>) {

    val isSuccessful: Boolean
        get() = oriResponse.isSuccessful

    val isFromCache: Boolean
        get() = isResponseEnable(oriResponse.raw().cacheResponse)

    val isFromNetWork: Boolean
        get() = isResponseEnable(oriResponse.raw().networkResponse)

    private fun isResponseEnable(response: okhttp3.Response?): Boolean {
        return response != null && response.code == ResultStatus.HTTP_OK
    }

    fun body(): T? {
        return oriResponse.body()
    }
    fun get(): retrofit2.Response<T>{
        return oriResponse
    }
    val message:String
        get() = oriResponse.message()
}
