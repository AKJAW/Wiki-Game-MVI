package com.akjaw.wikigamemvi.feature.victory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.akjaw.wikigamemvi.R
import com.akjaw.wikigamemvi.feature.game.GameViewModel
import com.akjaw.wikigamemvi.feature.game.model.GameViewState
import com.akjaw.wikigamemvi.injection.injector
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

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

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_victory, container, false)
    }

    override fun onResume() {
        super.onResume()

        disposables += viewModel.viewState.subscribe(::render)
    }

    private fun render(state: GameViewState){
//        victory_steps_text_view.text = state.targetArticle?.name ?: " aaa"
    }

    override fun onPause() {
        super.onPause()
        disposables.clear()
    }

}