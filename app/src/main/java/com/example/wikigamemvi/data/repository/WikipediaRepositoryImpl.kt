package com.example.wikigamemvi.data.repository

import com.example.wikigamemvi.data.model.WikiResponse
import com.example.wikigamemvi.data.model.WikiTitle
import com.example.wikigamemvi.data.repository.base.WikipediaRepository
import io.reactivex.Single

class WikipediaRepositoryImpl(
    private val wikipediaApi: MockWikipediaApi
): WikipediaRepository {

    override fun getRandomArticle(): Single<WikiResponse> {
        return wikipediaApi.randomArticle()
    }

    override fun getArticleFromTitle(title: WikiTitle): Single<WikiResponse> {
        return wikipediaApi.articleFromTitle(title)
    }

}