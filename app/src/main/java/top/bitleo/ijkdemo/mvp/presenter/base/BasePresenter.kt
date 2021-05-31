package top.bitleo.ijkdemo.mvp.presenter.base

import android.content.Context
import android.os.Bundle

import top.bitleo.ijkdemo.http.AppRetrofitCreator
import top.bitleo.ijkdemo.http.core.HttpObserver
import top.bitleo.ijkdemo.http.core.HttpResponse
import top.bitleo.ijkdemo.http.core.ResultStatus
import top.bitleo.ijkdemo.http.core.RxObserver
import top.bitleo.ijkdemo.http.error.HttpError
import top.bitleo.ijkdemo.http.error.HttpErrorCode
import top.bitleo.ijkdemo.http.error.HttpPageNoFoundError
import top.bitleo.ijkdemo.http.error.UnauthorizedError
import top.bitleo.ijkdemo.mvp.contract.base.IBaseContract
import top.bitleo.ijkdemo.mvp.model.*
import top.bitleo.ijkdemo.util.NetHelper
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import retrofit2.Response
import top.bitleo.ijkdemo.util.LogUtil
import java.io.*
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import javax.net.ssl.SSLHandshakeException
import javax.net.ssl.SSLPeerUnverifiedException



abstract class BasePresenter<V : IBaseContract.View>(
) : IBaseContract.Presenter<V> {

    private val TAG = "BasePresenter"

    //View
    protected var mView: V? = null

    protected var isViewInitialized = false
        private set

    private var isAttached = false

//    private val observers: ArrayList<Observer<*>> = ArrayList()


    /**
     * 获取上下文，需在onViewAttached()后调用
     *
     * @return
     */
    override val context: Context
        get() = if (mView is Context) {
            mView as Context
        } else if (mView is androidx.fragment.app.Fragment) {
            (mView as androidx.fragment.app.Fragment).context!!
        } else {
            throw NullPointerException("BasePresenter:mView is't instance of Context,can't use getContext() method.")
        }

    override fun onSaveInstanceState(outState: Bundle?) {
//        DataAutoAccess.saveData(this, outState)
    }

    override fun onRestoreInstanceState(outState: Bundle?) {
        if (outState == null) return
//        DataAutoAccess.getData(this, outState)

    }

    /**
     * 绑定View
     *
     * @param view view
     */
    override fun attachView(view: V) {
        mView = view
        isAttached = true
    }

    /**
     * 取消View绑定
     */
    override fun detachView() {
        mView = null

    }

    override fun onViewInitialized() {
        isViewInitialized = true
    }

    /**
     * 一般的rx http请求执行
     *
     * @param observable
     * @param subscriber null 表明不管数据回调
     * @param <T>
    </T> */
    protected fun <T : Any>  generalRxHttpExecuteExtend(
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



    /**
     * 一般的rx http请求执行 包括切换域名
     *
     * @param observable
     * @param subscriber null 表明不管数据回调
     * @param <T>
    </T> */
    protected fun <T : Any>  generalRxHttpExecute(
        observable: Observable<Response<T>>, observer: Observer<Response<T>>?,callback:(()->Observable<Response<T>>)? = null
    ) {

        val observerRaw: RxObserver<T> =
            RxObserver<T>(object :
                HttpObserver<T> {
                override fun onSuccess(response: HttpResponse<T>?) {

                    val httpCode = response?.get()?.code()
                            if(httpCode == ResultStatus.HTTP_PAGE_NO_FOUND_ERROR){
                               LogUtil.e(TAG,"generalRxHttpExecute 404")

                                try {
                                        Thread.sleep(50)
                                    }catch (_:Exception){

                                }

                                if(callback != null){
                                        var observableNew :Observable<Response<T>>  = callback.invoke()
                                        generalRxHttpExecute(observableNew,observer,callback)
                                    }else{
                                        (observer as RxObserver).get()?.onSuccess(response)
                                    }
                            }else{
                                (observer as RxObserver).get()?.onSuccess(response)
                            }

                }

                override fun onError(error: Throwable?) {
                    if(NetHelper.INSTANCE.netEnabled && (error is ConnectException || error is UnknownHostException ||
                                error is SSLHandshakeException || error is SocketTimeoutException || error is SSLPeerUnverifiedException)){ //域名解析不到
                        LogUtil.e(TAG,"generalRxHttpExecute ${error?.message}")

                        try {
                            Thread.sleep(50)
                        }catch (_:Exception){

                        }

                        if(callback != null){
                            var observableNew :Observable<Response<T>>  = callback.invoke()
                            generalRxHttpExecute(observableNew,observer,callback)
                        }else{
                            (observer as RxObserver).get()?.onError(error)
                        }
                    }else{
                        (observer as RxObserver).get()?.onError(error)
                    }
                }
            })
        generalRxHttpExecuteExtend(observable,observerRaw)

    }


    /**
     * 处理code的rx http请求执行
     *
     * @param observable
     * @param subscriber null 表明不管数据回调
     * @param <T>
    </T> */
    protected fun <T : Any>  generalRxHttpExecuteResponseCode(
        observable: Observable<Response<T>>, observer: Observer<Response<T>>?
    ) {
        val observerRaw: RxObserver<T> =
            RxObserver<T>(object :
                HttpObserver<T> {
                override fun onSuccess(response: HttpResponse<T>?) {
                    when {
                        response!!.isSuccessful -> (observer as RxObserver).get()?.onSuccess(
                            response
                        )
                        response.get().code() === ResultStatus.HTTP_PAGE_NO_FOUND_ERROR -> onError(
                            HttpPageNoFoundError()
                        )
                        response.get().code() === ResultStatus.HTTP_ERROR -> onError(
                            HttpError(HttpErrorCode.NO_CACHE_AND_NETWORK)
                        )
                        response.get().code() === ResultStatus.UNAUTHORIZED_ERROR -> onError(
                            UnauthorizedError()
                        )
                        else -> onError(Error(response.get().message()))
                    }
                }

                override fun onError(error: Throwable?) {
                    if (!checkIsUnauthorized(error)) {
                        (observer as RxObserver).get()?.onError(error)
                    }
                }
            })
        generalRxHttpExecute(observable,observerRaw)
    }
    private fun checkIsUnauthorized(error: Throwable?): Boolean {
//        if (error is UnauthorizedError) {
//            mView?.showErrorToast(error.message!!)
//            daoSession.authUserDao.delete(AppData.INSTANCE.authUser)
//            AppData.INSTANCE.authUser=null
//            AppData.INSTANCE.loggedUser = null
//            mView?.showLoginPage()
//            return true
//        }
        return false
    }


}
