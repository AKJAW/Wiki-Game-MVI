package com.example.wikigamemvi.data.repository

import com.example.wikigamemvi.data.model.WikiResponse
import com.example.wikigamemvi.data.model.WikiTitle
import com.example.wikigamemvi.data.repository.base.WikipediaRepository
import io.reactivex.Single
import javax.inject.Inject

class WikipediaRepositoryImpl @Inject constructor(
    private val wikipediaApi: MockWikipediaApi
): WikipediaRepository {

    override fun getRandomArticle(): Single<WikiResponse> {
        return wikipediaApi.randomArticle()
    }

    override fun getArticleFromTitle(title: WikiTitle): Single<WikiResponse> {
        return wikipediaApi.articleFromTitle(title)
    }

}