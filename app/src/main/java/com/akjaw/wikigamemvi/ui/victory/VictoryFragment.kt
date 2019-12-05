package com.akjaw.wikigamemvi.ui.victory

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.TextView
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
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.TransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeTransition
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.request.transition.TransitionFactory
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_article.*
import kotlinx.android.synthetic.main.fragment_article.view.*
import kotlinx.android.synthetic.main.fragment_victory.*
import javax.sql.DataSource

class VictoryFragment: Fragment(){
    private var disposables = CompositeDisposable()
    private lateinit var viewModel: GameViewModel //TODO neccessary?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders
                .of(this, injector.gameViewModelFactory())
                .get(GameViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        val transitionSet = android.transition.TransitionSet()
        transitionSet.duration = 500
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
            //            val titleTransitionName = getString(R.string.articleTitleTransition)
//            it.article_title.transitionName = titleTransitionName

            val imageTransitionName = getString(R.string.articleImageTransition)
            it.article_image.transitionName = imageTransitionName
        }
    }

    override fun onResume() {
        super.onResume()

        disposables += viewModel.viewState.subscribe(::render)
    }

    //TODO remove viewModel and pass the data through the intent?
    private fun render(state: GameViewState){
        val targetArticle = state.targetArticle

        if(targetArticle != null){
            article_title.text = targetArticle.name
            article_description.text = targetArticle.description

            if(targetArticle.imageUrl.isNotBlank()){
                article_image.glideLoadImage(targetArticle.imageUrl) {
                    startPostponedEnterTransition()
                    animateEnterTransition()
                }
            } else {
                startPostponedEnterTransition()
                animateEnterTransition()
            }
        }

        val stepsText = resources.getString(R.string.victory_steps, state.numberOfSteps)
        victory_steps_text_view.text = stepsText

        activity?.findViewById<TextView>(R.id.toolbar_steps)?.apply {
            isVisible = false
        }
    }

    private fun animateEnterTransition(){
        val headerFadeIn = victory_header_text_view.createFadeInObjectAnimator(500, 100)
        val stepsFadeIn = victory_steps_text_view.createFadeInObjectAnimator(500, 200)
        val articleTitleFadeIn = article_title.createFadeInObjectAnimator(500, 500)
        val articleDescriptionFadeIn = article_description.createFadeInObjectAnimator(500, 600)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(
            headerFadeIn,
            stepsFadeIn,
            articleTitleFadeIn,
            articleDescriptionFadeIn
        )

        animatorSet.start()
    }

    override fun onPause() {
        super.onPause()
        disposables.clear()
    }

}

