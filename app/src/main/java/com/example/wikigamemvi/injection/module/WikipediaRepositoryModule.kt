package com.example.wikigamemvi.injection.module

import android.content.Context
import com.example.wikigamemvi.data.repository.MockWikipediaApi
import com.example.wikigamemvi.data.repository.WikipediaRepositoryImpl
import com.example.wikigamemvi.data.repository.base.WikipediaRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
abstract class WikipediaRepositoryModule {
    @Binds
    abstract fun bindTaskRepository(wikipediaRepositoryImpl: WikipediaRepositoryImpl): WikipediaRepository
}