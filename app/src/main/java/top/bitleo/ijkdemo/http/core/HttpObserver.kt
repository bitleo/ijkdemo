package top.bitleo.ijkdemo.http.core

/**
 *
 * Time: 2019/5/14
 */
interface HttpObserver<T : Any> {
    /**
     * Error
     * @param error
     */
    fun onError(error: Throwable?)

    /**
     * success
     * @param response
     */
    fun onSuccess(response: HttpResponse<T>?)
}
