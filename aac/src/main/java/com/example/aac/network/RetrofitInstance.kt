package com.example.aac.network

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val BASE_URL = "https://api.kcisa.kr/openapi/service/rest/meta2/"

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val start = System.currentTimeMillis()
            val response = chain.proceed(chain.request())
            val end = System.currentTimeMillis()
            Log.d("API_TIMER", "응답 시간: ${end - start}ms")
            response
        }
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val api: BookService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()
            .create(BookService::class.java)
    }
}
