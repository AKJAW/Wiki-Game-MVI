package com.akjaw.wikigamemvi.injection.component

import android.content.Context
import com.akjaw.domain.usecase.*
import com.akjaw.wikigamemvi.ui.base.ViewModelFactory
import com.akjaw.wikigamemvi.ui.game.GameViewModel
import com.akjaw.wikigamemvi.injection.module.WikipediaApiModule
import com.akjaw.wikigamemvi.injection.module.WikipediaRepositoryModule
import com.akjaw.wikigamemvi.ui.game.model.GameViewState
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [WikipediaApiModule::class, WikipediaRepositoryModule::class])
interface ApplicationComponent{

    @Component.Factory
    interface Factory{
        fun create(
            @BindsInstance
            @Named("applicationContext")
            applicationContext: Context,
            @BindsInstance
            initialGameViewState: GameViewState = GameViewState()
        ): ApplicationComponent
    }

    fun gameViewModelFactory(): ViewModelFactory<GameViewModel>

//    fun getRandomArticleUseCase(): GetRandomArticleUseCase
//
//    fun getArticleFromTitleUseCase(): GetArticleFromTitleUseCase
//
//    fun getTargetArticleUseCase(): GetTargetArticleUseCase
//
//    fun initializeArticlesUseCase(): InitializeArticlesUseCase
//
//    fun articleWinConditionUseCase(): ArticleWinConditionUseCase
}
