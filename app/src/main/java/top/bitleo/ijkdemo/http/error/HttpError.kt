package top.bitleo.ijkdemo.http.error

/**
 * 网络请求错误
 *
 * Time: 2019/5/15
 */

open class HttpError(errorCode: Int) : Error(HttpErrorCode.getErrorMsg(errorCode)) {

    var errorCode = -1

    init {
        this.errorCode = errorCode
    }
}
