package com.example.wikigamemvi.injection

import android.app.Activity
import androidx.fragment.app.Fragment
import com.example.wikigamemvi.injection.component.ApplicationComponent

interface DaggerApplicationComponentProvider {
    val applicationComponent: ApplicationComponent
}

val Activity.injector
    get() = (application as DaggerApplicationComponentProvider).applicationComponent

val Fragment.injector
    get() = (requireActivity().application as DaggerApplicationComponentProvider).applicationComponent