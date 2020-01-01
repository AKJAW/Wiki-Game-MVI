package com.akjaw.wikigamemvi.injection.module

import com.akjaw.base.WikipediaApi
import com.akjaw.local.WikipediaMockApi
import com.akjaw.remote.WikiApiService
import com.akjaw.remote.WikipediaRemoteApi
import com.akjaw.remote.model.RemoteMapper
import com.akjaw.wikigamemvi.BuildConfig
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class WikipediaApiModule {
    @Provides
    @Singleton
    fun retrofit(): Retrofit {

        val builder = Retrofit.Builder()
            .baseUrl("https://akjaw-wiki-api.herokuapp.com")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())

        if(BuildConfig.DEBUG && false){
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            builder.client(client)
        }

        return builder.build()
    }

    @Provides
    fun wikiApiService(retrofit: Retrofit): WikiApiService{
        return retrofit.create(WikiApiService::class.java)
    }

    @Provides
    fun bindWikiApi(impl: WikipediaRemoteApi)
            : WikipediaApi{
        return impl
    }

}