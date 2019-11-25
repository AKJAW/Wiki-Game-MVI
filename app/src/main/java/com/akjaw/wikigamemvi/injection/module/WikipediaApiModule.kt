package com.akjaw.wikigamemvi.injection.module

import com.akjaw.data.repository.MockWikipediaApiImpl
import com.akjaw.data.repository.WikipediaApi
import dagger.Binds
import dagger.Module

@Module
abstract class WikipediaApiModule {

    @Binds
    abstract fun bindWikiApi(impl: MockWikipediaApiImpl)
            : WikipediaApi


}