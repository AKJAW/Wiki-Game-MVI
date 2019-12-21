package com.akjaw.wikigamemvi.injection.module

import com.akjaw.common.WikiRepositoryImpl
import com.akjaw.domain.repository.WikiRepository
import com.akjaw.domain.usecase.*
import com.akjaw.domain.usecase.GetTargetArticleUseCase
import com.akjaw.domain.usecase.GetTargetArticleUseCaseImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton


@Module
abstract class WikipediaRepositoryModule {
    @Binds @Singleton
    abstract fun bindTaskRepository(wikipediaRepositoryImpl: WikiRepositoryImpl): WikiRepository

    @Binds @Singleton
    abstract fun bindGetRandomArticleUseCase(impl: GetRandomArticleUseCaseImpl)
            : GetRandomArticleUseCase

    @Binds @Singleton
    abstract fun bindGetArticleFromTitleUseCase(impl: GetArticleFromTitleUseCaseImpl)
            : GetArticleFromTitleUseCase

    @Binds @Singleton
    abstract fun bindGetTargetArticleUseCase(impl: GetTargetArticleUseCaseImpl)
            : GetTargetArticleUseCase

    @Binds @Singleton
    abstract fun bindInitializeArticlesUseCase(impl: InitializeArticlesUseCaseImpl)
            : InitializeArticlesUseCase

    @Binds @Singleton
    abstract fun bindArticleWinConditionUseCase(impl: ArticleWinConditionUseCaseImpl)
            : ArticleWinConditionUseCase
}