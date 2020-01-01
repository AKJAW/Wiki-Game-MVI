package com.akjaw.wikigamemvi.injection.component

import com.akjaw.domain.model.WikiLanguage
import com.akjaw.domain.model.WikiTitle
import com.akjaw.wikigamemvi.injection.factory.ViewModelFactory
import com.akjaw.wikigamemvi.injection.module.WikipediaApiModule
import com.akjaw.wikigamemvi.injection.module.WikipediaRepositoryModule
import com.akjaw.wikigamemvi.ui.game.ArticleLinksAdapter
import com.akjaw.wikigamemvi.ui.game.GameViewModel
import com.akjaw.wikigamemvi.ui.game.GameViewState
import dagger.BindsInstance
import dagger.Component
import javax.inject.Named
import javax.inject.Scope

@Scope
@Retention
annotation class GameFragmentScope

@GameFragmentScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [WikipediaApiModule::class, WikipediaRepositoryModule::class]
)
interface GameComponent{

    @Component.Factory
    interface Factory{
        fun create(
            applicationComponent: ApplicationComponent,
            @BindsInstance
            @Named("onArticleNavigationClick")
            onClick: (WikiTitle) -> Unit,
            @BindsInstance
            initialGameViewState: GameViewState = GameViewState(),
            @BindsInstance
            language: WikiLanguage = "pl"
        ): GameComponent
    }

    fun gameViewModelFactory(): ViewModelFactory<GameViewModel>

    fun articleLinksAdapter(): ArticleLinksAdapter
}
