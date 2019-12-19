package com.akjaw.wikigamemvi.view_util

import android.animation.Animator
import android.view.View
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator


fun View.createFadeInObjectAnimator(duration: Long = 300, delay: Long = 0): ObjectAnimator {
    val fadeIn = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
    fadeIn.duration = duration
    fadeIn.startDelay = delay

    fadeIn.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            this@createFadeInObjectAnimator.visibility = View.VISIBLE
            this@createFadeInObjectAnimator.alpha = 0f
        }
    })

    return fadeIn
}