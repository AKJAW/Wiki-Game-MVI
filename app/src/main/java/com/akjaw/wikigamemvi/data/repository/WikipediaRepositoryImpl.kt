package com.akjaw.wikigamemvi.data.repository

import com.akjaw.wikigamemvi.data.model.WikiResponse
import com.akjaw.wikigamemvi.data.model.WikiTitle
import com.akjaw.wikigamemvi.data.repository.base.WikipediaRepository
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