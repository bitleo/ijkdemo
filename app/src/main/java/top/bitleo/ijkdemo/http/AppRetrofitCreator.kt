package top.bitleo.ijkdemo.http

import io.reactivex.Observable
import retrofit2.Response
import top.bitleo.ijkdemo.http.core.AppRetrofit
import top.bitleo.ijkdemo.http.service.ApiService


object AppRetrofitCreator {
    fun createGetConfig(url: String): Observable<Response<String>>? {
        val base = url.substring(0,url.lastIndexOf("/")+1);
        val end = url.substring(url.lastIndexOf("/")+1);
        return  AppRetrofit.INSTANCE
            .getRetrofitNormal(base)?.create(ApiService::class.java)?.getConfig(end)
    }
}

