package com.akjaw.wikigamemvi.injection.module

import com.akjaw.common.WikiRepositoryImpl
import com.akjaw.domain.repository.WikiRepository
import com.akjaw.domain.usecase.*
import com.akjaw.domain.usecase.GetTargetArticleUseCase
import com.akjaw.domain.usecase.GetTargetArticleUseCaseImpl
import com.akjaw.wikigamemvi.injection.component.GameFragmentScope
import dagger.Binds
import dagger.Module
import javax.inject.Singleton


@Module
abstract class WikipediaRepositoryModule {
    @Binds @GameFragmentScope
    abstract fun bindTaskRepository(wikipediaRepositoryImpl: WikiRepositoryImpl): WikiRepository

    @Binds @GameFragmentScope
    abstract fun bindGetRandomArticleUseCase(impl: GetRandomArticleUseCaseImpl)
            : GetRandomArticleUseCase

    @Binds @GameFragmentScope
    abstract fun bindGetArticleFromTitleUseCase(impl: GetArticleFromTitleUseCaseImpl)
            : GetArticleFromTitleUseCase

    @Binds @GameFragmentScope
    abstract fun bindGetTargetArticleUseCase(impl: GetTargetArticleUseCaseImpl)
            : GetTargetArticleUseCase

    @Binds @GameFragmentScope
    abstract fun bindInitializeArticlesUseCase(impl: InitializeArticlesUseCaseImpl)
            : InitializeArticlesUseCase

    @Binds @GameFragmentScope
    abstract fun bindArticleWinConditionUseCase(impl: ArticleWinConditionUseCaseImpl)
            : ArticleWinConditionUseCase
}