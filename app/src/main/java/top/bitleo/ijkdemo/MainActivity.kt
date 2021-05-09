package top.bitleo.ijkdemo

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import top.bitleo.ijkdemo.http.AppRetrofitCreator
import top.bitleo.ijkdemo.http.core.HttpObserver
import top.bitleo.ijkdemo.http.core.HttpResponse
import top.bitleo.ijkdemo.http.core.ResultStatus
import top.bitleo.ijkdemo.http.core.RxObserver
import top.bitleo.ijkdemo.util.LogUtil
import top.bitleo.ijkdemo.util.NetHelper
import top.bitleo.ijkdemo.util.PrefUtils
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException
import javax.net.ssl.SSLPeerUnverifiedException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        urlTextView?.text = PrefUtils.getVideoUrl()
    }


    @JvmField @BindView(R.id.url_address)
    internal  var urlTextView: TextView? = null



    @OnClick(R.id.refresh)
    fun onRefreshClick(){
        showProgressDialog("正在获取video url...")
        val observable = AppRetrofitCreator.createGetConfig("https://dl.bitleo.top/video.txt")
        val observer = RxObserver<String>(object : HttpObserver<String>{
            override fun onSuccess(response: HttpResponse<String>?) {

                dismissProgressDialog()
                val body = response?.body()

                body?.let {

                    PrefUtils[PrefUtils.VIDEO_URL_ADDRRESS] = it

                    urlTextView?.text = body
                }


            }

            override fun onError(error: Throwable?) {
                dismissProgressDialog()

                LogUtil.e(TAG,"get video url error")
            }
        })
        generalRxHttpExecute(observable!!,observer)
    }
    @OnClick(R.id.play_video)
    fun onPlayVideoClick(){
        if(urlTextView?.text?.isNullOrBlank() == true){
            val toast = Toast.makeText(this,"url is null",Toast.LENGTH_LONG)
            toast.show()
        }else{
            val intent = Intent(this, VideoActivity::class.java)
            intent.putExtra("videoPath", urlTextView?.text)
            this.startActivity(intent)
        }

    }





    private var mProgressDialog: ProgressDialog? = null

    private fun showProgressDialog(content: String) {
        getProgressDialog(content)
        mProgressDialog?.show()
    }

    fun dismissProgressDialog() {
        mProgressDialog?.dismiss()
    }

     private fun getProgressDialog(content: String): ProgressDialog {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(this)
            mProgressDialog!!.setCancelable(false)
        }
        mProgressDialog!!.setMessage(content)
        return mProgressDialog as ProgressDialog
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
        observable: Observable<Response<T>>, observer: Observer<Response<T>>?, callback:(()-> Observable<Response<T>>)? = null
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
                            var observableNew : Observable<Response<T>> = callback.invoke()
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
                            var observableNew : Observable<Response<T>> = callback.invoke()
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

    private val TAG = "MainActivity"


}