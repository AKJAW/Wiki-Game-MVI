package com.akjaw.domain.usecase

import com.akjaw.domain.model.WikiTitle
import io.reactivex.Observable
import javax.inject.Inject

interface ArticleWinConditionUseCase {
    operator fun invoke(wikiTitle: WikiTitle): Observable<Boolean>
}

class ArticleWinConditionUseCaseImpl @Inject constructor(
    private val getTargetArticleUseCase: GetTargetArticleUseCase
): ArticleWinConditionUseCase {
    override fun invoke(wikiTitle: WikiTitle): Observable<Boolean> {
        return getTargetArticleUseCase(false)
            .filter { response -> response.name == wikiTitle }
            .map { true }
    }
}