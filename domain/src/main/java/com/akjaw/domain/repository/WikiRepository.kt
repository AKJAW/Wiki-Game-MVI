package com.akjaw.domain.repository

import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.model.WikiTitle
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

interface WikiRepository {

    fun getTargetArticle(refresh: Boolean): Single<WikiResponse>

    fun getRandomArticle(): Single<WikiResponse>

    fun getArticleFromTitle(title: WikiTitle): Single<WikiResponse>

}