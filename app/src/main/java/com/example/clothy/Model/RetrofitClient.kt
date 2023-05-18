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

    class ReceivedCookiesInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalResponse: Response = chain.proceed(chain.request())
            if (originalResponse.headers("Set-Cookie").isNotEmpty()) {

                for (header in originalResponse.headers("Set-Cookie")) {
                    CookieStorage.cookies.add(header)
                }
            }
            return originalResponse
        }
    }
    class AddCookiesInterceptor : Interceptor {
        // Save the user's email address in shared preferences
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder: Request.Builder = chain.request().newBuilder()
            for (cookie in CookieStorage.cookies) {
                builder.addHeader("Cookie", cookie)
            }
            return chain.proceed(builder.build())
        }
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