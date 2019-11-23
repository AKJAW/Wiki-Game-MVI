package com.example.wikigamemvi.injection.component

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.wikigamemvi.data.repository.MockWikipediaApi
import com.example.wikigamemvi.data.repository.WikipediaRepositoryImpl
import com.example.wikigamemvi.injection.module.WikipediaApiModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [WikipediaApiModule::class])
interface ApplicationComponent{

    @Component.Builder
    interface Builder{
        @BindsInstance
        fun applicationContext(@Named("applicationContext") applicationContext: Context): Builder

        fun build(): ApplicationComponent
    }

    fun wikipediaApi(): MockWikipediaApi

    fun wikipediaRepository(): WikipediaRepositoryImpl
}
