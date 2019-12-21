package com.akjaw.remote

import com.akjaw.remote.model.WikiApiResponseEntity
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.Query


interface WikiApiService {
    @GET("random-article")
    fun randomArticle(): Single<WikiApiResponseEntity>


    @GET("article-from-title")
    fun articleFromTitle(@Query("title") title: String): Single<WikiApiResponseEntity>
}

fun main() {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://wiki-api-us.herokuapp.com/")
        .client(client)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val service = retrofit.create(WikiApiService::class.java)


    service.articleFromTitle("Kotlin")
        .toObservable()
        .subscribe {
            print(it)
        }

    Thread.sleep(2000)
}