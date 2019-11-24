package com.akjaw.wikigamemvi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
//    private var disposables: CompositeDisposable = CompositeDisposable()

//    private lateinit var viewModel: GameViewModel

//    override fun events(): Observable<GameAction> {
////        return testButton.clicks().publish {
////            Observable.merge(
////                it.map { GameAction.LoadTargetArticleAction },
////                it.map { GameAction.ShowToastAction("aaa") }
////            )
////        }
//        return testButton.clicks().map { GameAction.ShowToastAction("aaa") }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

//        val viewModel = ViewModelProviders.of(this)
//            .get(GameViewModel::class.java)

    }

//    override fun onResume() {
//        super.onResume()
//
//        disposables += viewModel.viewState.subscribe(::render)
//        disposables += viewModel.viewEffects.subscribe(::trigger)
//        disposables += events().subscribe(viewModel::process)
//    }

//    private fun render(state: GameViewState) {
//        if(state.isTargetArticleLoading){
//            testTextview.text = "Loading"
//        }
//
//        if(state.targetArticle != null){
//            testTextview.text = state.targetArticle.name
//        }
//    }

//    private fun trigger(effect: GameViewEffect) {
//        when(effect){
//            is GameViewEffect.SomeToastEffect -> {
//                Toast.makeText(this@MainActivity, effect.text, Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

//    override fun onPause() {
//        super.onPause()
//
//        disposables.clear()
//    }
}
