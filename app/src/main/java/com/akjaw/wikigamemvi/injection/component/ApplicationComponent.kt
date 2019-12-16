package com.akjaw.wikigamemvi.injection.component

import android.content.Context
import com.akjaw.wikigamemvi.ui.base.ViewModelFactory
import com.akjaw.wikigamemvi.ui.game.GameViewModel
import com.akjaw.wikigamemvi.injection.module.WikipediaApiModule
import com.akjaw.wikigamemvi.injection.module.WikipediaRepositoryModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [WikipediaApiModule::class, WikipediaRepositoryModule::class])
interface ApplicationComponent{

    @Component.Factory
    interface Builder{
        fun create(
            @BindsInstance
            @Named("applicationContext")
            applicationContext: Context
        ): ApplicationComponent
    }

    fun gameViewModelFactory(): ViewModelFactory<GameViewModel>
}
