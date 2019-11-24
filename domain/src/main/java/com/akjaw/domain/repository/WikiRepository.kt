package com.akjaw.domain.repository

import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.model.WikiTitle
import io.reactivex.Single

interface WikiRepository {

    fun getRandomArticle(): Single<com.akjaw.domain.model.WikiResponse>

    fun getArticleFromTitle(title: com.akjaw.domain.model.WikiTitle): Single<com.akjaw.domain.model.WikiResponse>

}