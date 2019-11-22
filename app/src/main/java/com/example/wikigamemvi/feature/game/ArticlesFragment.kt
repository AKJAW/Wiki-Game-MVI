package com.example.wikigamemvi.feature.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.wikigamemvi.R
import io.reactivex.disposables.CompositeDisposable

class ArticlesFragment: Fragment(){
    private var disposables = CompositeDisposable()

    private lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_articles, container, false).also {

        }
    }
}