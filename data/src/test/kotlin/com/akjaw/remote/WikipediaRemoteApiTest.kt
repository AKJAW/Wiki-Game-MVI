package com.akjaw.remote

import com.akjaw.domain.model.WikiLanguage
import com.akjaw.domain.model.WikiResponse
import com.akjaw.remote.model.WikiApiResponseEntity
import com.akjaw.remote.model.WikiArticleResponseEntity
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.MockitoAnnotations

internal class WikipediaRemoteApiTest {
    private val language: WikiLanguage = "pl"
    @Mock private lateinit var apiService: WikiApiService
    @Mock private lateinit var mapper: com.akjaw.domain.model.Mapper<WikiApiResponseEntity, WikiResponse>
    private lateinit var wikipediaRemoteApi: WikipediaRemoteApi
    private lateinit var wikiApiResponseEntity: WikiApiResponseEntity

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        wikipediaRemoteApi = WikipediaRemoteApi(language, apiService, mapper)

        val article = WikiArticleResponseEntity("name")
        val titles = listOf("a", "b", "c")
        wikiApiResponseEntity = WikiApiResponseEntity(article, titles)
    }

    @Nested
    inner class Language{
        @Test
        fun `randomArticle passes the language to the apiService call`(){
            Mockito.`when`(apiService.randomArticle(Mockito.anyString()))
                .thenReturn(Single.just(wikiApiResponseEntity))

            val remoteApi = WikipediaRemoteApi("pl", apiService, mapper)

            remoteApi.randomArticle().test().dispose()
            Mockito.verify(apiService, times(1))
                .randomArticle("pl")
        }

        @Test
        fun `articleFromTitle passes the language to the apiService call`(){
            Mockito.`when`(apiService.articleFromTitle(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Single.just(wikiApiResponseEntity))

            val remoteApi = WikipediaRemoteApi("pl", apiService, mapper)

            remoteApi.articleFromTitle("title").test().dispose()
            Mockito.verify(apiService, times(1))
                .articleFromTitle("pl", "title")
        }
    }

    @Nested
    inner class Mapper{
        lateinit var entity: WikiApiResponseEntity

        @BeforeEach
        fun setUp(){
            val article = WikiArticleResponseEntity("name")
            val titles = listOf("a", "b", "c")
            entity = WikiApiResponseEntity(article, titles)
        }

        @Test
        fun `randomArticle maps to the domain model before returning`(){
            Mockito.`when`(apiService.randomArticle(Mockito.anyString()))
                .thenReturn(Single.just(entity))

            wikipediaRemoteApi.randomArticle().test().dispose()
            Mockito.verify(mapper, times(1))
                .mapFrom(entity)
        }

        @Test
        fun `articleFromTitle maps to the domain model before returning`(){
            Mockito.`when`(apiService.articleFromTitle(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Single.just(entity))

            wikipediaRemoteApi.articleFromTitle("title").test().dispose()
            Mockito.verify(mapper, times(1))
                .mapFrom(entity)
        }
    }


}