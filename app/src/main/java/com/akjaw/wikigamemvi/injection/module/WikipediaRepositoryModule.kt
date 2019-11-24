package com.akjaw.wikigamemvi.injection.module

import com.akjaw.wikigamemvi.data.repository.WikiRepositoryImpl
import com.akjaw.domain.repository.WikiRepository
import com.akjaw.domain.usecase.GetArticleFromTitleUseCase
import com.akjaw.domain.usecase.GetArticleFromTitleUseCaseImpl
import com.akjaw.domain.usecase.GetRandomArticleUseCase
import com.akjaw.domain.usecase.GetRandomArticleUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton



@Module
abstract class WikipediaRepositoryModule {
    @Binds
    abstract fun bindTaskRepository(wikipediaRepositoryImpl: WikiRepositoryImpl): WikiRepository

    @Binds
    abstract fun bindGetRandomArticleUseCase(impl: GetRandomArticleUseCaseImpl)
            : GetRandomArticleUseCase

    @Binds
    abstract fun bindGetArticleFromTitleUseCase(impl: GetArticleFromTitleUseCaseImpl)
            : GetArticleFromTitleUseCase
}