package com.akjaw.domain.repository

import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.model.WikiTitle
import io.reactivex.Single

interface WikiRepository {

    fun getRandomArticle(): Single<WikiResponse>

    fun getArticleFromTitle(title: WikiTitle): Single<WikiResponse>

}