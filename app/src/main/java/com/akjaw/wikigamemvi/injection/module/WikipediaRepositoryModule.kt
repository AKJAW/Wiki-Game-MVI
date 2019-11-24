package com.akjaw.wikigamemvi.injection.module

import com.akjaw.wikigamemvi.data.repository.WikiRepositoryImpl
import com.akjaw.domain.repository.WikiRepository
import dagger.Binds
import dagger.Module

@Module
abstract class WikipediaRepositoryModule {
    @Binds
    abstract fun bindTaskRepository(wikipediaRepositoryImpl: WikiRepositoryImpl): WikiRepository
}