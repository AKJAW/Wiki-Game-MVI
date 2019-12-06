package com.akjaw.wikigamemvi.ui.game

import android.os.Bundle
import android.transition.TransitionInflater
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.akjaw.wikigamemvi.R
import com.akjaw.domain.model.WikiTitle
import com.akjaw.wikigamemvi.ui.game.model.GameAction
import com.akjaw.wikigamemvi.ui.game.model.GameViewEffect
import com.akjaw.wikigamemvi.ui.game.model.GameViewState
import com.akjaw.wikigamemvi.ui.victory.VictoryFragment
import com.akjaw.wikigamemvi.injection.injector
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.article.view.*
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.android.synthetic.main.fragment_game.toolbar
import kotlinx.android.synthetic.main.fragment_game.view.*
import java.lang.Exception

class GameFragment: Fragment(){
    private var disposables = CompositeDisposable()

    private lateinit var viewModel: GameViewModel
    private lateinit var wikiLinksAdapter: ArticleLinksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders
                .of(this, injector.gameViewModelFactory())
                .get(GameViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        wikiLinksAdapter = ArticleLinksAdapter(::onArticleNavigationClick)

        disposables += viewModel.viewState.subscribe(::render)
        disposables += viewModel.viewEffects.subscribe(::trigger)
    }

    private fun onArticleNavigationClick(wikiTitle: WikiTitle){
        viewModel.process(GameAction.LoadNextArticleAction(wikiTitle))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game, container, false).also {
            it.wiki_navigation_recycler_view.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(activity)
                adapter = wikiLinksAdapter
            }

            (activity as? AppCompatActivity?)?.setSupportActionBar(toolbar)

            it.target_article_view.setOnMoreClickListener {
                Toast.makeText(activity, "More info", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun render(state: GameViewState){
        target_article_view.setIsLoading(state.isTargetArticleLoading)
        current_article_view.setIsLoading(state.isCurrentArticleLoading)

        if(state.targetArticle != null){
            target_article_view.setArticle(state.targetArticle)
        }

        if(state.currentArticle != null){
            current_article_view.setArticle(state.currentArticle, true)
        }

        if(state.wikiNavigationLinks.isNotEmpty()){
            wikiLinksAdapter.submitList(state.wikiNavigationLinks)
        }

        val stepsText = resources.getString(R.string.toolbar_step, state.numberOfSteps)
        toolbar_steps.text = stepsText
    }


    private fun trigger(effect: GameViewEffect) {
        when(effect){
            is GameViewEffect.SomeToastEffect -> {
                Toast.makeText(activity, effect.text, Toast.LENGTH_SHORT).show()
            }

            is GameViewEffect.ShowVictoryScreenEffect -> showVictoryFragment()
        }
    }

    private fun showVictoryFragment() {
        val transition = fragmentManager?.beginTransaction() ?: return

        val imageTransitionName = getString(R.string.articleImageTransition)
        //TODO a function on the ArticleView that returns the current image_view (collapsed/expanded)
        transition.addSharedElement(target_article_view.article_image_view, imageTransitionName)

        val fragment = VictoryFragment()

        transition.replace(R.id.main_fragment_placeholder, fragment)
        transition.commit()
    }

    override fun onDestroy() {
        super.onDestroy()

        disposables.clear()
    }
}