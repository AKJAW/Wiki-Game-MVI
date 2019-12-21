package com.akjaw.common

import com.akjaw.base.WikipediaApi
import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.model.WikiTitle
import com.akjaw.domain.repository.WikiRepository
import io.reactivex.Single
import javax.inject.Inject

class WikiRepositoryImpl @Inject constructor(
    private val wikipediaApi: WikipediaApi
): WikiRepository {

    override fun getRandomArticle(): Single<WikiResponse> {
        return wikipediaApi.randomArticle()
    }

    override fun getArticleFromTitle(title: WikiTitle): Single<WikiResponse> {
        return wikipediaApi.articleFromTitle(title)
    }

}