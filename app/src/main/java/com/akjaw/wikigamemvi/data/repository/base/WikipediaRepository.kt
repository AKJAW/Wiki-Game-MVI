package com.akjaw.wikigamemvi.data.repository.base

import com.akjaw.wikigamemvi.data.model.WikiResponse
import com.akjaw.wikigamemvi.data.model.WikiTitle
import io.reactivex.Single

interface WikipediaRepository {

    fun getRandomArticle(): Single<WikiResponse>

    fun getArticleFromTitle(title: WikiTitle): Single<WikiResponse>

}