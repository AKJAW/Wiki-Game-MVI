package com.akjaw.wikigamemvi.data.repository

import com.akjaw.wikigamemvi.data.model.WikiResponse
import com.akjaw.wikigamemvi.data.model.WikiTitle
import io.reactivex.Single
import kotlin.random.Random

class MockWikipediaApi {
    private val articles = listOf<WikiResponse>(
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