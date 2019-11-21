package com.example.wikigamemvi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.example.wikigamemvi.feature.game.GameViewModel
import com.example.wikigamemvi.feature.game.model.GameAction
import com.example.wikigamemvi.feature.game.model.GameViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var disposables: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProviders.of(this)
            .get(GameViewModel::class.java)

        disposables += viewModel.viewState
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::render)

        viewModel.process(GameAction.LoadTargetArticleAction)
    }

    private fun render(state: GameViewState) {
        if(state.isTargetArticleLoading){
            testTextview.text = "Loading"
        }

        if(state.targetArticle != null){
            testTextview.text = state.targetArticle.name
        }
    }

}
