package com.akjaw.local

import com.akjaw.base.WikipediaApi
import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.model.WikiTitle
import io.reactivex.Single
import javax.inject.Inject
import kotlin.random.Random

class WikipediaMockApi @Inject constructor(): WikipediaApi {

    private val articles = listOf(
        WikiResponse(
            name = "First",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/0/06/Herv%C3%A1s_Spain.JPG",
            description = "The description for the first item",
            outgoingTitles = listOf("Third", "Fourth", "Third", "Fourth", "Third", "Fourth", "Third", "Fourth", "Third", "Fourth", "Third", "Fourth", "Third", "Fourth", "Third", "Fourth", "Third", "Fourth", "Third", "Fourth")),
        WikiResponse(
            name = "Second",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/c/c9/S_visoti.jpg",
            description = "The description for the second item",
            outgoingTitles = listOf("Fourth", "First")
        ),
        WikiResponse(
            name = "Third",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/3/35/Ortrand_markt.JPG",
            description = "The description for the third item",
            outgoingTitles = listOf("Fourth", "Second")
        ),
        WikiResponse(
            name = "Fourth",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/7/7f/Hrebec-04.jpg",
            description = "The description for the fourth item, The description for the fourth item, The description for the fourth item, The description for the fourth item, The description for the fourth item, The description for the fourth item, The description for the fourth item, The description for the fourth item, The description for the fourth item, The description for the fourth item, The description for the fourth item, The description for the fourth item, The description for the fourth item, The description for the fourth item, The description for the fourth item, The description for the fourth item, The description for the fourth item, The description for the fourth item, The description for the fourth item, The description for the fourth item, The description for the fourth item, The description for the fourth item, The description for the fourth item, The description for the fourth item, The description for the fourth item, The description for the fourth item, The description for the fourth item, The description for the fourth item, The description for the fourth item, ",
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