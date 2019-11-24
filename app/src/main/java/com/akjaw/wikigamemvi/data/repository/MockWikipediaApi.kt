package com.akjaw.wikigamemvi.data.repository

import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.model.WikiTitle
import io.reactivex.Single
import kotlin.random.Random

class MockWikipediaApi {
    private val articles = listOf<com.akjaw.domain.model.WikiResponse>(
        com.akjaw.domain.model.WikiResponse(name = "First"),
        com.akjaw.domain.model.WikiResponse(name = "Second"),
        com.akjaw.domain.model.WikiResponse(name = "Third"),
        com.akjaw.domain.model.WikiResponse(name = "Fourth")
    )

    fun randomArticle(): Single<com.akjaw.domain.model.WikiResponse> {
        val index = Random.nextInt(articles.size)

        return Single.just(articles[index])
    }

    fun articleFromTitle(title: com.akjaw.domain.model.WikiTitle): Single<com.akjaw.domain.model.WikiResponse> {
        val article = articles.firstOrNull{
            it.name == title
        }

        return if(article != null){
            Single.just(article)
        } else {
            Single.error(NoSuchElementException())
        }
    }
}