package com.example.wikigamemvi.injection.component

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.wikigamemvi.data.repository.MockWikipediaApi
import com.example.wikigamemvi.data.repository.WikipediaRepositoryImpl
import com.example.wikigamemvi.data.repository.base.WikipediaRepository
import com.example.wikigamemvi.feature.base.ViewModelFactory
import com.example.wikigamemvi.feature.game.GameViewModel
import com.example.wikigamemvi.injection.module.WikipediaApiModule
import com.example.wikigamemvi.injection.module.WikipediaRepositoryModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [WikipediaApiModule::class, WikipediaRepositoryModule::class])
interface ApplicationComponent{

    @Component.Builder
    interface Builder{
        @BindsInstance
        fun applicationContext(@Named("applicationContext") applicationContext: Context): Builder

        fun build(): ApplicationComponent
    }

    fun gameViewModelFactory(): ViewModelFactory<GameViewModel>
}
