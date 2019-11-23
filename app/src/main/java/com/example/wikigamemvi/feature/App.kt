package com.example.wikigamemvi.feature

import android.app.Application
import com.example.wikigamemvi.injection.DaggerApplicationComponentProvider
import com.example.wikigamemvi.injection.component.ApplicationComponent
import com.example.wikigamemvi.injection.component.DaggerApplicationComponent

class App: Application(), DaggerApplicationComponentProvider {
    override val applicationComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent
            .builder()
            .applicationContext(applicationContext)
            .build()
    }
}