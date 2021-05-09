package top.bitleo.ijkdemo.http.core

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.Response


class RxObserver<T : Any>(private var mObserver: HttpObserver<T>?): Observer<Response<T>> {
    override fun onComplete() {

    }

    override fun onSubscribe(d: Disposable) {

    }

    override fun onNext(response: Response<T>) {
        mObserver?.onSuccess(HttpResponse(response))
    }

    override fun onError(e: Throwable) {
        mObserver?.onError(e)
    }
    fun get(): HttpObserver<T>?{
        return mObserver
    }
}