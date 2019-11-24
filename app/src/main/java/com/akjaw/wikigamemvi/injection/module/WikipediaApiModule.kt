package com.akjaw.wikigamemvi.injection.module

import com.akjaw.wikigamemvi.data.repository.MockWikipediaApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object WikipediaApiModule {

    @JvmStatic @Singleton @Provides
    fun providesApi(): MockWikipediaApi {
        return MockWikipediaApi()
    }


}