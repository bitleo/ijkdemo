package top.bitleo.ijkdemo.http.core

import top.bitleo.ijkdemo.util.NetHelper
import top.bitleo.ijkdemo.util.StringUtils
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.converter.scalars.ScalarsConverterFactory
import top.bitleo.ijkdemo.AppConfig
import top.bitleo.ijkdemo.ClientApplication
import top.bitleo.ijkdemo.util.FileUtil

import java.io.IOException
import java.net.Proxy
import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

/**
 * Retrofit 网络请求
 *
 * Time: 2019/5/15
 */
enum class AppRetrofit {
    INSTANCE;


    private val retrofitMap = HashMap<String, Retrofit>()
    private var token: String? = null

    private fun createRetrofit(baseUrl: String) {
        val timeOut = AppConfig.HTTP_TIME_OUT
        val cache = Cache(
            FileUtil.getHttpImageCacheDir(ClientApplication.app!!)!!,
            AppConfig.HTTP_MAX_CACHE_SIZE.toLong()
        )

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(timeOut.toLong(), TimeUnit.MILLISECONDS)
            .addInterceptor(BaseInterceptor())
            .addNetworkInterceptor(NetworkBaseInterceptor())
            .cache(cache)
            .build()

        val builder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
        builder.addConverterFactory(GsonConverterFactory.create())

        retrofitMap[baseUrl] = builder.build()
    }

    fun getRetrofit(baseUrl: String, token: String? = null): Retrofit? {
        token?.let {
            this.token = it
        }
        if (!retrofitMap.containsKey(baseUrl)) {
            createRetrofit(baseUrl)
        }
        return retrofitMap[baseUrl]
    }

    fun getRetrofitNormal(baseUrl: String):Retrofit?{
        if (!retrofitMap.containsKey(baseUrl)) {
            val timeOut = AppConfig.HTTP_TIME_OUT

            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(timeOut.toLong(), TimeUnit.MILLISECONDS)
                .build()

            val builder = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(okHttpClient)
            retrofitMap[baseUrl] = builder.build()
        }
        return retrofitMap[baseUrl]
    }


    /**
     * 拦截器
     */
    private inner class BaseInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()

            request = request.newBuilder()
                .build()

            if (token!=null && token!!.isNotBlank()) {
                val auth = if (token!!.startsWith("Token ")) token else "Token $token"
                request = request.newBuilder()
                    .addHeader("Authorization", auth.toString())
                    .build()
            }
//            request = request.newBuilder()
//                    .cacheControl(CacheControl.FORCE_NETWORK)
//                    .build()
            //第二次请求，强制使用网络请求
            val forceNetWork = request.header("forceNetWork")
            //有forceNetWork且无网络状态下取从缓存中取
            if (!forceNetWork.isNullOrBlank() && !NetHelper.INSTANCE.netEnabled) {
                request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build()
            } else if ("true" == forceNetWork) {
                request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .build()
            }

            return chain.proceed(request)
        }
    }

    /**
     * 网络请求拦截器
     */
    private inner class NetworkBaseInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {

            val request = chain.request()
            val originalResponse = chain.proceed(request)
            var requestCacheControl = request.cacheControl.toString()
            //有forceNetWork时，强制更改缓存策略
            val forceNetWork = request.header("forceNetWork")
            if (forceNetWork?.isBlank()==false) {
                requestCacheControl = cacheString
            }

            return if (StringUtils.isBlank(requestCacheControl)) {
                originalResponse
            } else {
                originalResponse.newBuilder()
                    .header("Cache-Control", requestCacheControl)
                    .removeHeader("Pragma")
                    .build()
            }//设置缓存策略
        }
    }

    companion object {

        private const val TAG = "AppRetrofit"
        private val gmtTime: String
            get() {
                val date = Date()
                val format = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
                format.timeZone = TimeZone.getTimeZone("GMT+8")
                return format.format(date)
            }

        val cacheString: String
            get() = "public, max-age=" + AppConfig.CACHE_MAX_AGE
    }

}
