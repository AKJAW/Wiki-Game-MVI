package com.akjaw.wikigamemvi.ui.victory

import android.animation.AnimatorSet
import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.akjaw.wikigamemvi.ui.game.GameViewModel
import com.akjaw.wikigamemvi.ui.game.GameViewState
import com.akjaw.wikigamemvi.R
import com.akjaw.wikigamemvi.data.model.ParcelableWikiArticle
import com.akjaw.wikigamemvi.injection.injector
import com.akjaw.wikigamemvi.util.createFadeInObjectAnimator
import com.akjaw.wikigamemvi.util.glideLoadImage
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_victory.*
import kotlinx.android.synthetic.main.fragment_victory.view.*

class VictoryFragment: Fragment(){
    private var hasEnterTransitionRun: Boolean = false

    companion object {
        private const val DEFAULT_FADE_IN_DURATION = 500L
        private const val IMAGE_SHARED_TRANSITION_DURATION = 500L
        private const val EXTRA_ARTICLE = "EXTRA_ARTICLE"
        private const val EXTRA_STEPS = "EXTRA_STEPS"

        fun create(article: ParcelableWikiArticle, steps: Int): VictoryFragment{
            val fragment = VictoryFragment()
            val args = Bundle()
            args.putInt(EXTRA_STEPS, steps)
            args.putParcelable(EXTRA_ARTICLE, article)
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val transitionSet = android.transition.TransitionSet()
        transitionSet.duration = IMAGE_SHARED_TRANSITION_DURATION
        transitionSet.addTransition(TransitionInflater.from(activity).inflateTransition(android.R.transition.move))
        sharedElementEnterTransition = transitionSet
        postponeEnterTransition()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_victory, container, false).also {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                val imageTransitionName = getString(R.string.articleImageTransition)
                it.article_image.transitionName = imageTransitionName
            } else {
                it.article_image.visibility = View.INVISIBLE
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeView()
    }

    private fun initializeView(){
        val steps = arguments?.getInt(EXTRA_STEPS) ?: -1
        val article = arguments?.getParcelable(EXTRA_ARTICLE) ?: ParcelableWikiArticle()

        if(article.imageUrl != null){
            article_image.glideLoadImage(article.imageUrl) {
                startPostponedEnterTransition()
            }
        } else {
            startPostponedEnterTransition()
        }

        val stepsText = resources.getString(R.string.victory_steps, steps)
        victory_steps_text_view.text = stepsText

        activity?.findViewById<TextView>(R.id.toolbar_steps)?.apply {
            isVisible = false
        }
    }

    override fun startPostponedEnterTransition() {
        super.startPostponedEnterTransition()

        if(!hasEnterTransitionRun){
            animateEnterTransition()
        }
    }

    private fun animateEnterTransition(){
        val headerFadeIn = victory_header_text_view
            .createFadeInObjectAnimator(DEFAULT_FADE_IN_DURATION, 100)
        val stepsFadeIn = victory_steps_text_view
            .createFadeInObjectAnimator(DEFAULT_FADE_IN_DURATION, 200)
        val articleTitleFadeIn = article_title
            .createFadeInObjectAnimator(DEFAULT_FADE_IN_DURATION, IMAGE_SHARED_TRANSITION_DURATION)
        val articleDescriptionFadeIn = article_description_scroll_view
            .createFadeInObjectAnimator(DEFAULT_FADE_IN_DURATION, IMAGE_SHARED_TRANSITION_DURATION + 100)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(
            headerFadeIn,
            stepsFadeIn,
            articleTitleFadeIn,
            articleDescriptionFadeIn
        )


        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            val imageFadeIn = article_image
                .createFadeInObjectAnimator(IMAGE_SHARED_TRANSITION_DURATION - 100, 500)

            animatorSet.play(imageFadeIn)
        }

        animatorSet.doOnEnd {
            hasEnterTransitionRun = true
        }

        animatorSet.start()
    }
}

