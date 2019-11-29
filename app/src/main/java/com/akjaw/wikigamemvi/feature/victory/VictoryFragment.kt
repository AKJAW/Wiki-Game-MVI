package com.akjaw.wikigamemvi.feature.victory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akjaw.wikigamemvi.R

class VictoryFragment: Fragment(){
//    private lateinit var viewModel: GameViewModel //TODO neccessary?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        viewModel = activity?.run {
//            ViewModelProviders
//                .of(this, injector.gameViewModelFactory())
//                .get(GameViewModel::class.java)
//        } ?: throw Exception("Invalid Activity")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_victory, container, false).also {

        }
    }

}