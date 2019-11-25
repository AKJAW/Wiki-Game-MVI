package com.akjaw.domain.usecase

import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.repository.WikiRepository
import io.reactivex.Observable
import javax.inject.Inject

interface GetTargetArticleUseCase {
    operator fun invoke(isNewTargetArticle: Boolean = false): Observable<WikiResponse>
}

class GetTargetArticleUseCaseImpl @Inject constructor(
    private val wikiRepository: WikiRepository
): GetTargetArticleUseCase {
    //TODO get random article -> save it to the repository (saveTargetRepo?)
    //or
    //TODO get target article (the article is save internally inside repository)
    override fun invoke(isNewTargetArticle: Boolean): Observable<WikiResponse> {
        return wikiRepository.getTargetArticle(isNewTargetArticle).toObservable()
//            .switchIfEmpty(
//                loadRandomArticleUseCase()
//                    .doOnNext {
//                        wikiRepository.setTargetArticle()
//                        print("only first $it")
//                        val s = "s"
//                    }
//            )
//            .doOnNext {
//                print("everyTime $it")
//                val s = "s"
//            }
    }

}