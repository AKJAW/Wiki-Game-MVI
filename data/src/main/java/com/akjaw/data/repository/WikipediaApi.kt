package com.akjaw.data.repository

import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.model.WikiTitle
import io.reactivex.Single
import javax.inject.Inject
import kotlin.random.Random

interface WikipediaApi {
    fun randomArticle(): Single<WikiResponse>
    fun articleFromTitle(title: WikiTitle): Single<WikiResponse>
}

class MockWikipediaApiImpl @Inject constructor(): WikipediaApi {
    private val articles = listOf(
        WikiResponse(
            name = "First",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/0/06/Herv%C3%A1s_Spain.JPG",
            outgoingTitles = listOf("Third", "Fourth")),
        WikiResponse(
            name = "Second",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/c/c9/S_visoti.jpg",
            outgoingTitles = listOf("Fourth", "First")
        ),
        WikiResponse(
            name = "Third",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/3/35/Ortrand_markt.JPG",
            outgoingTitles = listOf("Fourth", "Second")
        ),
        WikiResponse(
            name = "Fourth",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/7/7f/Hrebec-04.jpg",
            outgoingTitles = listOf("First", "Third"))
    )

    override fun randomArticle(): Single<WikiResponse> {
        val index = Random.nextInt(articles.size)

        return Single.just(articles[index])
    }

    override fun articleFromTitle(title: WikiTitle): Single<WikiResponse> {
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