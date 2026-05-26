package com.example.stream.Data.Remote.Client

import com.example.stream.Data.Remote.Service.ApiService
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy

object ApiClient {
//    private const val BASE_URL = "http://10.0.2.2:8000/"
//    private const val BASE_URL = "http://10.200.99.243:8000/"
    private const val BASE_URL = "https://koryuz.com/"
    private val cookieManager = CookieManager().apply {
        setCookiePolicy(CookiePolicy.ACCEPT_ALL)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .cookieJar(JavaNetCookieJar(cookieManager))
        .build()
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}