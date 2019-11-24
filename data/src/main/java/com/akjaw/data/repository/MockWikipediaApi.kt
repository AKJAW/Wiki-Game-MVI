package com.akjaw.data.repository

import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.model.WikiTitle
import io.reactivex.Single
import kotlin.random.Random

class MockWikipediaApi {
    private val articles = listOf(
        WikiResponse(name = "First", outgoingTitles = listOf("Third")),
        WikiResponse(
            name = "Second",
            outgoingTitles = listOf("Fourth", "First")
        ),
        WikiResponse(
            name = "Third",
            outgoingTitles = listOf("Fourth", "Second")
        ),
        WikiResponse(name = "Fourth", outgoingTitles = listOf("First"))
    )

    fun randomArticle(): Single<WikiResponse> {
        val index = Random.nextInt(articles.size)

        return Single.just(articles[index])
    }

    fun articleFromTitle(title: WikiTitle): Single<WikiResponse> {
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