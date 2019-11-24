package com.akjaw.domain.usecase

import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.model.WikiTitle
import com.akjaw.domain.repository.WikiRepository
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject


interface GetArticleFromTitleUseCase {
    operator fun invoke(title: WikiTitle): Observable<WikiResponse>
}

class GetArticleFromTitleUseCaseImpl @Inject constructor(
    private val wikiRepository: WikiRepository
): GetArticleFromTitleUseCase {
    override fun invoke(title: WikiTitle): Observable<WikiResponse>
            = wikiRepository.getArticleFromTitle(title).toObservable()
}