package com.akjaw.wikigamemvi.data.repository

import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.repository.WikiRepository
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class WikiRepositoryImpl @Inject constructor(
    private val wikipediaApi: MockWikipediaApi
): WikiRepository {

    override fun getRandomArticle(): Single<WikiResponse> {
        return wikipediaApi.randomArticle()
    }

    override fun getArticleFromTitle(title: com.akjaw.domain.model.WikiTitle): Single<WikiResponse> {
        return wikipediaApi.articleFromTitle(title)
    }

}