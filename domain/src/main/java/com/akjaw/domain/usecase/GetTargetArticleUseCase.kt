package com.akjaw.domain.usecase

import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.repository.WikiRepository
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

interface GetTargetArticleUseCase {
    operator fun invoke(isNewTargetArticle: Boolean = false): Observable<WikiResponse>
}

class GetTargetArticleUseCaseImpl @Inject constructor(
    private val wikiRepository: WikiRepository
): GetTargetArticleUseCase {

    private val targetBehaviorSubject = BehaviorSubject.create<WikiResponse>()

    override fun invoke(isNewTargetArticle: Boolean): Observable<WikiResponse> {
        val targetArticle = targetBehaviorSubject.value

        return if(targetArticle == null || isNewTargetArticle){
            getTargetArticleFromRepository()
        } else {
            Observable.just(targetArticle)
        }
    }

    private fun getTargetArticleFromRepository(): Observable<WikiResponse>{
        return wikiRepository.getRandomArticle()
            .doAfterSuccess { targetBehaviorSubject.onNext(it) }
            .toObservable()
    }
}