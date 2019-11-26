package com.akjaw.wikigamemvi.injection.module

import com.akjaw.data.repository.WikiRepositoryImpl
import com.akjaw.domain.repository.WikiRepository
import com.akjaw.domain.usecase.*
import dagger.Binds
import dagger.Module


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

    @Binds
    abstract fun bindGetTargetArticleUseCase(impl: GetTargetArticleUseCaseImpl)
            : GetTargetArticleUseCase

    @Binds
    abstract fun bindInitializeArticlesUseCase(impl: InitializeArticlesUseCaseImpl)
            : InitializeArticlesUseCase
}