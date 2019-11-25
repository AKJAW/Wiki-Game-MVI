package com.akjaw.data.repository

import com.akjaw.domain.model.WikiArticle
import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.model.WikiTitle
import com.akjaw.domain.repository.WikiRepository
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class WikiRepositoryImpl @Inject constructor(
    private val wikipediaApi: MockWikipediaApi
): WikiRepository {

//    private val targetBehaviorSubject = BehaviorSubject.create<WikiResponse>()

    override fun getTargetArticle(): Observable<WikiResponse> {
        return getRandomArticle().toObservable().publish().replay(1)

//        targetBehaviorSubject.switchIfEmpty {
//            targetBehaviorSubject
//        }
//
//        return if(targetArticle != null){
//            Single.just(targetArticle)
//        } else {
//            getRandomArticle()
//                .doAfterSuccess { targetBehaviorSubject.onNext(it) }
//        }
    }

    override fun getRandomArticle(): Single<WikiResponse> {
        return wikipediaApi.randomArticle()
    }

    override fun getArticleFromTitle(title: WikiTitle): Single<WikiResponse> {
        return wikipediaApi.articleFromTitle(title)
    }

}