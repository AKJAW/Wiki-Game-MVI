package com.akjaw.domain.usecase

import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.repository.WikiRepository
import io.reactivex.Single
import javax.inject.Inject

interface GetRandomArticleUseCase {
    operator fun invoke(): Single<WikiResponse>
}

class GetRandomArticleUseCaseImpl @Inject constructor(
    private val wikiRepository: WikiRepository
): GetRandomArticleUseCase {
    override fun invoke(): Single<WikiResponse> = wikiRepository.getRandomArticle()
}