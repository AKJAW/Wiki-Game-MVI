package com.akjaw.wikigamemvi.ui.victory

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.akjaw.wikigamemvi.R
import com.akjaw.wikigamemvi.ui.game.GameViewModel
import com.akjaw.wikigamemvi.ui.game.model.GameViewState
import com.akjaw.wikigamemvi.injection.injector
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

        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_victory, container, false).also {
            val titleTransitionName = getString(R.string.articleTitleTransition)
            it.article_title.transitionName = titleTransitionName
        }
    }

    override fun onResume() {
        super.onResume()

        disposables += viewModel.viewState.subscribe(::render)
    }

    private fun render(state: GameViewState){
        val targetArticle = state.targetArticle

        if(targetArticle != null){
            article_title.text = targetArticle.name
            article_description.text = targetArticle.description

            if(targetArticle.imageUrl.isNotBlank()){
                Glide
                    .with(this)
                    .load(targetArticle.imageUrl)
                    .fitCenter()
//                    .transition(GenericTransitionOptions.with(R.anim.article_image_zoom_fade_in))
                    .transition(GenericTransitionOptions.with {
                            dataSource, isFirstResource ->

                        DrawableCrossFadeTransition(600, false)
                    })
                    .into(article_image)
            }
        }

        val stepsText = resources.getString(R.string.victory_steps, state.numberOfSteps)
        victory_steps_text_view.text = stepsText

        activity?.findViewById<TextView>(R.id.toolbar_steps)?.apply {
            isVisible = false
        }
    }

    override fun onPause() {
        super.onPause()
        disposables.clear()
    }

}

