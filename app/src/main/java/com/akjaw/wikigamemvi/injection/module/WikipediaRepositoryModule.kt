package com.akjaw.wikigamemvi.injection.module

import com.akjaw.wikigamemvi.data.repository.WikipediaRepositoryImpl
import com.akjaw.wikigamemvi.data.repository.base.WikipediaRepository
import dagger.Binds
import dagger.Module

@Module
abstract class WikipediaRepositoryModule {
    @Binds
    abstract fun bindTaskRepository(wikipediaRepositoryImpl: WikipediaRepositoryImpl): WikipediaRepository
}