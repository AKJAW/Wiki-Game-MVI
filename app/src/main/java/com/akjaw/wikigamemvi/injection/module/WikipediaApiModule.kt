package com.akjaw.wikigamemvi.injection.module

import com.akjaw.data.repository.MockWikipediaApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object WikipediaApiModule {

    @JvmStatic @Singleton @Provides
    fun providesApi(): com.akjaw.data.repository.MockWikipediaApi {
        return com.akjaw.data.repository.MockWikipediaApi()
    }


}