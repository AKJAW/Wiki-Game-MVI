package com.akjaw.remote

import io.reactivex.rxkotlin.subscribeBy
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

//For testing and debugging
internal object ApiFactory {
    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://akjaw-wiki-api.herokuapp.com")
        .client(client)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val api = retrofit.create(WikiApiService::class.java)
}

fun main() {
    ApiFactory.api.articleFromTitle("pl", "Kotlin")
        .subscribeBy(
            onSuccess = {
                println("s")
            }
        )

    Thread.sleep(2000)
}
