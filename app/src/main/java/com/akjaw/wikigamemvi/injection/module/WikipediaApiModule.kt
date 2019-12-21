package com.akjaw.wikigamemvi.injection.module

import com.akjaw.base.WikipediaApi
import com.akjaw.local.WikipediaMockApi
import dagger.Binds
import dagger.Module

@Module
abstract class WikipediaApiModule {

    @Binds
    abstract fun bindWikiApi(impl: WikipediaMockApi)
            : WikipediaApi


}