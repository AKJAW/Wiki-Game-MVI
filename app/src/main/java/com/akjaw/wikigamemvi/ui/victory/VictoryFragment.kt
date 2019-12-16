package com.akjaw.wikigamemvi.ui.victory

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.transition.TransitionSet
import com.akjaw.wikigamemvi.R
import com.akjaw.wikigamemvi.ui.game.GameViewModel
import com.akjaw.wikigamemvi.ui.game.model.GameViewState
import com.akjaw.wikigamemvi.injection.injector
import com.akjaw.wikigamemvi.util.createFadeInObjectAnimator
import com.akjaw.wikigamemvi.util.glideLoadImage
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_article_details.*
import kotlinx.android.synthetic.main.fragment_article_details.view.*
import kotlinx.android.synthetic.main.fragment_victory.*

class VictoryFragment: Fragment(){

    private var disposables = CompositeDisposable()
    private lateinit var viewModel: GameViewModel //TODO neccessary?

    private var hasEnterTransitionRun: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders
                .of(this, injector.gameViewModelFactory())
                .get(GameViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

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

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1){
                val imageTransitionName = getString(R.string.articleImageTransition)
                it.article_image.transitionName = imageTransitionName
            } else {
                it.article_image.visibility = View.INVISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()

        disposables += viewModel.viewState.subscribe(::render)
    }

    //TODO remove viewModel and pass the data through the intent?
    private fun render(state: GameViewState){
        val targetArticle = state.targetArticleState.article

        if(targetArticle != null){
            article_title.text = targetArticle.name
            article_description.text = targetArticle.description

            if(targetArticle.imageUrl.isNotBlank()){
                article_image.glideLoadImage(targetArticle.imageUrl) {
                    startPostponedEnterTransition()
                }
            } else {
                startPostponedEnterTransition()
            }
        }

        val stepsText = resources.getString(R.string.victory_steps, state.numberOfSteps)
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

    override fun onPause() {
        super.onPause()
        disposables.clear()
    }

    companion object {
        const val DEFAULT_FADE_IN_DURATION = 500L
        const val IMAGE_SHARED_TRANSITION_DURATION = 500L
    }

}

