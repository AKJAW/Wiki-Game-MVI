package com.akjaw.wikigamemvi.data.repository

import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.model.WikiTitle
import io.reactivex.Observable
import io.reactivex.Single
import kotlin.random.Random

class MockWikipediaApi {
    private val articles = listOf(
        WikiResponse(name = "First"),
        WikiResponse(name = "Second"),
        WikiResponse(name = "Third"),
        WikiResponse(name = "Fourth")
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