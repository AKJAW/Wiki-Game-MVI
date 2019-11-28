package com.akjaw.domain.usecase

import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.repository.WikiRepository
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface InitializeArticlesUseCase {
    operator fun invoke(): Observable<Pair<WikiResponse, WikiResponse>>
}

class InitializeArticlesUseCaseImpl @Inject constructor(
    private val wikiRepository: WikiRepository,
    private val getTargetArticleUseCase: GetTargetArticleUseCase
): InitializeArticlesUseCase {
    override fun invoke(): Observable<Pair<WikiResponse, WikiResponse>> {
        return getTargetArticleUseCase(true)
            .observeOn(Schedulers.io())
            .flatMap { target ->
                wikiRepository.getRandomArticle()
                    .map { random -> target to random }
                    .toObservable()
            }
            .repeat()
            .takeUntil { (target, random) ->
                target != random
            }
            .takeLast(1)
    }

}