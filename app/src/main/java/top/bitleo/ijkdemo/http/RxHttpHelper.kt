package top.bitleo.ijkdemo.http

import top.bitleo.ijkdemo.http.core.RxObserver
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

/**
 *
 * Time: 2019/7/31
 */
object RxHttpHelper {

    /**
     * 一般的rx http请求执行
     *
     * @param observable
     * @param subscriber null 表明不管数据回调
     * @param <T>
    </T> */
    fun <T : Any>  generalRxHttpExecute(
        observable: Observable<Response<T>>, observer: Observer<Response<T>>?
    ) {

        if (observer != null) {
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        } else {
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(RxObserver<T>(null))
        }
    }
}