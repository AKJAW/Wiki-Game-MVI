package com.example.wikigamemvi.feature.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.wikigamemvi.R
import com.example.wikigamemvi.feature.game.model.GameViewState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_articles.*
import java.lang.Exception

class ArticlesFragment: Fragment(){
    private var disposables = CompositeDisposable()

    private lateinit var viewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this).get(GameViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_articles, container, false).also {

        }
    }

    override fun onResume() {
        super.onResume()

        disposables += viewModel.viewState.subscribe(::render)
    }

    private fun render(state: GameViewState){

        target_article_progress_bar.isVisible = state.isTargetArticleLoading
        current_article_progress_bar.isVisible = state.isCurrentArticleLoading

        if(state.targetArticle != null){
            target_article_text_view.text = state.targetArticle.name
        }

        if(state.currentArticle != null){
            current_article_title_text_view.text = state.currentArticle.name
        }
    }

    override fun onPause() {
        super.onPause()

        disposables.clear()
    }
}