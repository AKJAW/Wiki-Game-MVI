package com.akjaw.wikigamemvi.feature.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.akjaw.wikigamemvi.R
import com.akjaw.domain.model.WikiTitle
import com.akjaw.wikigamemvi.MainActivity
import com.akjaw.wikigamemvi.feature.game.model.GameAction
import com.akjaw.wikigamemvi.feature.game.model.GameViewEffect
import com.akjaw.wikigamemvi.feature.game.model.GameViewState
import com.akjaw.wikigamemvi.feature.victory.VictoryFragment
import com.akjaw.wikigamemvi.injection.injector
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.android.synthetic.main.fragment_game.view.*
import java.lang.Exception

class GameFragment: Fragment(){
    private var disposables = CompositeDisposable()

    //TODO inject
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
//        Toast.makeText(activity, wikiTitle, Toast.LENGTH_SHORT).show()
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
        }
    }

//    override fun onResume() {
//        super.onResume()
//
//        disposables += viewModel.viewState.subscribe(::render)
//        disposables += viewModel.viewEffects.subscribe(::trigger)
//
//
//    }

    private fun render(state: GameViewState){

        target_article_progress_bar.isVisible = state.isTargetArticleLoading
        current_article_progress_bar.isVisible = state.isCurrentArticleLoading

        if(state.targetArticle != null){
            target_article_text_view.text = state.targetArticle.name
        }

        if(state.currentArticle != null){
            current_article_title_text_view.text = state.currentArticle.name
        }

        if(state.wikiNavigationLinks.isNotEmpty()){
            wikiLinksAdapter.submitList(state.wikiNavigationLinks)
        }

        wiki_navigation_recycler_view.isVisible = !state.hasWon
        current_article_title_text_view.isVisible = !state.hasWon
        current_article_header_text_view.isVisible = !state.hasWon
    }


    private fun trigger(effect: GameViewEffect) {
        when(effect){
            is GameViewEffect.SomeToastEffect -> {
                Toast.makeText(activity, effect.text, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        disposables.clear()
    }

    //    override fun onPause() {
//        super.onPause()
//
//        disposables.clear()
//    }
}