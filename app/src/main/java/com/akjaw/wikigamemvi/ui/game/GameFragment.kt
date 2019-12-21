package com.akjaw.wikigamemvi.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.akjaw.domain.model.ArticleType
import com.akjaw.wikigamemvi.R
import com.akjaw.domain.model.WikiTitle
import com.akjaw.wikigamemvi.injection.DaggerGameComponentProvider
import com.akjaw.wikigamemvi.injection.component.DaggerGameComponent
import com.akjaw.wikigamemvi.injection.component.GameComponent
import com.akjaw.wikigamemvi.injection.gameInjector
import com.akjaw.wikigamemvi.ui.victory.VictoryFragment
import com.akjaw.wikigamemvi.injection.injector
import com.akjaw.wikigamemvi.ui.common.ActionObservable
import com.akjaw.wikigamemvi.util.MethodThrottler
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.article_collapsed.view.*
import kotlinx.android.synthetic.main.article_header.view.*
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.android.synthetic.main.fragment_game.toolbar
import kotlinx.android.synthetic.main.fragment_game.view.*
import java.lang.Exception
import java.util.concurrent.TimeUnit

class GameFragment: Fragment(), ActionObservable<GameAction>, DaggerGameComponentProvider {
    override val gameComponent: GameComponent by lazy {
        DaggerGameComponent
            .factory()
            .create(injector, ::onArticleNavigationClick)
    }

    private var disposables = CompositeDisposable()
    private var eventDisposable: Disposable? = null

    private lateinit var viewModel: GameViewModel

    private lateinit var wikiLinksAdapter: ArticleLinksAdapter

    private lateinit var navigationClickThrottler: MethodThrottler<WikiTitle, Unit>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders
                .of(this, injector.gameViewModelFactory())
                .get(GameViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        viewModel.initialize()

        wikiLinksAdapter = gameInjector.articleLinksAdapter()

        navigationClickThrottler = MethodThrottler(disposables, 500) {
            val action = GameAction.LoadNextArticleAction(it)
            viewModel.process(action)
        }

        disposables += viewModel.viewState.subscribe(::render)
        disposables += viewModel.viewEffects.subscribe(::trigger)
    }

    private fun onArticleNavigationClick(wikiTitle: WikiTitle){
        navigationClickThrottler.onClick(wikiTitle)
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

        }
    }

    override fun onResume() {
        super.onResume()

        eventDisposable = actions().subscribe(viewModel::process)
    }

    override fun onPause() {
        super.onPause()

        eventDisposable?.dispose()
    }

    override fun actions(): Observable<GameAction> {
        return Observable.merge(
            target_article_view.article_header_button_text_view
                .clicks()
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .map {
                    GameAction.ToggleArticleModeAction(ArticleType.TARGET)
                },
            current_article_view.article_header_button_text_view
                .clicks()
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .map {
                    GameAction.ToggleArticleModeAction(ArticleType.CURRENT)
                }
        )
    }

    private fun render(state: GameViewState){
        target_article_view.setIsLoading(state.targetArticleState.isLoading)
        current_article_view.setIsLoading(state.currentArticleState.isLoading)

        target_article_view.toggleView(state.targetArticleState.mode)
        current_article_view.toggleView(state.currentArticleState.mode)

        val targetArticle = state.targetArticleState.article
        if(targetArticle != null){
            target_article_view.setArticle(targetArticle)
        }

        val currentArticle = state.currentArticleState.article
        if(currentArticle != null){
            current_article_view.setArticle(currentArticle, true)
        }

        if(state.wikiNavigationLinks.isNotEmpty()){
            wikiLinksAdapter.submitList(state.wikiNavigationLinks)
        }

        val stepsText = resources.getString(R.string.toolbar_step, state.numberOfSteps)
        toolbar_steps.text = stepsText
    }


    private fun trigger(effect: GameViewEffect) {
        when(effect){
            is GameViewEffect.ShowVictoryScreenEffect -> showVictoryFragment()
        }
    }

    private fun showVictoryFragment() {
        val transition = fragmentManager?.beginTransaction() ?: return

        val imageTransitionName = getString(R.string.articleImageTransition)
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