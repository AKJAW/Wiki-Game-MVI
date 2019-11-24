package com.akjaw.wikigamemvi.data.repository

import com.akjaw.domain.repository.WikiRepository
import io.reactivex.Single
import javax.inject.Inject

class WikiRepositoryImpl @Inject constructor(
    private val wikipediaApi: MockWikipediaApi
): WikiRepository {

    override fun getRandomArticle(): Single<com.akjaw.domain.model.WikiResponse> {
        return wikipediaApi.randomArticle()
    }

    override fun getArticleFromTitle(title: com.akjaw.domain.model.WikiTitle): Single<com.akjaw.domain.model.WikiResponse> {
        return wikipediaApi.articleFromTitle(title)
    }

}