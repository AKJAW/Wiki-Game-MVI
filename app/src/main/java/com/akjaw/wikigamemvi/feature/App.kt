package com.akjaw.wikigamemvi.feature

import android.app.Application
import com.akjaw.wikigamemvi.injection.DaggerApplicationComponentProvider
import com.akjaw.wikigamemvi.injection.component.ApplicationComponent
import com.akjaw.wikigamemvi.injection.component.DaggerApplicationComponent

class App: Application(), DaggerApplicationComponentProvider {
    override val applicationComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent
            .builder()
            .applicationContext(applicationContext)
            .build()
    }
}