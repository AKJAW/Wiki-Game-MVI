package com.akjaw.wikigamemvi.injection.component

import com.akjaw.domain.model.WikiTitle
import com.akjaw.wikigamemvi.ui.game.ArticleLinksAdapter
import dagger.BindsInstance
import dagger.Component
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
    }

    fun articleLinksAdapter(): ArticleLinksAdapter
}
