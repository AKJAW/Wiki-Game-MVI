package com.example.wikigamemvi.injection.module

import android.content.Context
import com.example.wikigamemvi.data.repository.MockWikipediaApi
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
object WikipediaApiModule {

    @JvmStatic @Singleton @Provides
    fun providesApi(): MockWikipediaApi {
        return MockWikipediaApi()
    }


}