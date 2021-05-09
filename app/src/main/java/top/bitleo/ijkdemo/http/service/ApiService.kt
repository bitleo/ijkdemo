package top.bitleo.ijkdemo.http.service

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface ApiService {

    @GET("{url}")
    fun getConfig(@Path("url") url:String): Observable<Response<String>>

}