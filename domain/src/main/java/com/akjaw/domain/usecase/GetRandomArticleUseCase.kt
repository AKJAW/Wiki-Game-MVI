package com.akjaw.domain.usecase

import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.repository.WikiRepository
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface GetRandomArticleUseCase {
    operator fun invoke(): Observable<WikiResponse>
}

class GetRandomArticleUseCaseImpl @Inject constructor(
    private val wikiRepository: WikiRepository
): GetRandomArticleUseCase {
    override fun invoke(): Observable<WikiResponse>
            = wikiRepository.getRandomArticle()
        .delay(3L, TimeUnit.SECONDS)
        .toObservable()
}