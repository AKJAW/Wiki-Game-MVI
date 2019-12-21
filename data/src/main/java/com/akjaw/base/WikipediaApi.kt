package com.akjaw.base

import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.model.WikiTitle
import io.reactivex.Single
import javax.inject.Inject
import kotlin.random.Random

interface WikipediaApi {
    fun randomArticle(): Single<WikiResponse>
    fun articleFromTitle(title: WikiTitle): Single<WikiResponse>
}

