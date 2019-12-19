package com.akjaw.wikigamemvi.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.akjaw.wikigamemvi.BuildConfig
import com.akjaw.wikigamemvi.ui.game.GameFragment
import timber.log.Timber
import com.akjaw.wikigamemvi.R


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_fragment_placeholder, GameFragment())
            .commit()
    }

}
