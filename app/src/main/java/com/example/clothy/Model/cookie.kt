package com.clothy.clothyandroid.services

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
class cookies {
    class MySharedPreferences(context: Context) {

        private val sharedPreferences =
            context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        fun saveCookies(cookies: Set<String>) {
            sharedPreferences.edit().putStringSet("cookies", cookies).apply()
        }

        fun getCookies(): Set<String> {
            return sharedPreferences.getStringSet("cookies", HashSet()) ?: HashSet()
        }

        fun clearCookies() {
            sharedPreferences.edit().remove("cookies").apply()
        }
    }

    class ReceivedCookiesInterceptor(private val context: Context) : Interceptor {
        private val sharedPreferences = MySharedPreferences(context)
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalResponse = chain.proceed(chain.request())
            if (originalResponse.headers("Set-Cookie").isNotEmpty()) {
                val cookies: HashSet<String> = HashSet()
                for (header in originalResponse.headers("Set-Cookie")) {
                    cookies.add(header)
                }
               sharedPreferences.saveCookies(cookies)
            }
            return originalResponse
        }
    }

    class AddCookiesInterceptor(private val context: Context) : Interceptor {

        private val sharedPreferences = MySharedPreferences(context)

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder: Request.Builder = chain.request().newBuilder()
            val prefCookies: Set<String> = sharedPreferences.getCookies()

            for (cookie in prefCookies) {
                builder.addHeader("Cookie", cookie)
            }
            return chain.proceed(builder.build())
        }
    }
}