package com.example.clothy.Model

import com.clothy.clothyandroid.services.cookies
import com.google.gson.GsonBuilder
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException



class RetrofitClient {
    private val BASE_URL = "http://10.0.2.2:9090/uploads/"
    object CookieStorage {
        val cookies = mutableListOf<String>()
    }


    val BASE_URLL = "http://10.0.2.2:9090/uploads/"


    private val okHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(cookies.AddCookiesInterceptor(MyApplication.getInstance()))
        .addInterceptor(cookies.ReceivedCookiesInterceptor(MyApplication.getInstance()))
    fun getInstance(): Retrofit {

        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl(/*"https://cicero-crm.com/api/"*/ "http://10.0.2.2:9090/")
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }
}