package com.akjaw.wikigamemvi.injection.component

import android.content.Context
import com.akjaw.domain.model.WikiTitle
import com.akjaw.wikigamemvi.ui.base.ViewModelFactory
import com.akjaw.wikigamemvi.ui.game.GameViewModel
import com.akjaw.wikigamemvi.injection.module.WikipediaApiModule
import com.akjaw.wikigamemvi.injection.module.WikipediaRepositoryModule
import com.akjaw.wikigamemvi.ui.game.ArticleLinksAdapter
import com.akjaw.wikigamemvi.ui.game.model.GameViewState
import dagger.BindsInstance
import dagger.Component
import dagger.Provides
import javax.inject.Named
import javax.inject.Scope

@Scope
@Retention
annotation class GameFragmentScope

@GameFragmentScope
@Component(dependencies = [ApplicationComponent::class])
interface GameComponent{

    @Component.Factory
    interface Factory{
        fun create(
            applicationComponent: ApplicationComponent,
            @BindsInstance
            @Named("onArticleNavigationClick")
            onClick: (WikiTitle) -> Unit
        ): GameComponent

//        fun applicationComponent(applicationComponent: ApplicationComponent): Builder
//        @BindsInstance
//        fun onArticleNavigationClick(@Named("onArticleNavigationClick") onClick: (WikiTitle) -> Unit): Builder
//
//        fun build(): GameComponent
    }

    fun articleLinksAdapter(): ArticleLinksAdapter
//
//    @Provides
//    fun initialGaveViewState(): GameViewState = GameViewState()
}
