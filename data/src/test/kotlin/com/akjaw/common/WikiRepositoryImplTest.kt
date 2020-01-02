package com.akjaw.common

import com.akjaw.base.WikipediaApi
import com.akjaw.domain.model.WikiResponse
import io.reactivex.Single
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class WikiRepositoryImplTest {
    @Mock private lateinit var api: WikipediaApi

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `getRandomArticle calls the api randomArticle and returns its value`(){
        Mockito.`when`(api.randomArticle())
            .thenReturn(Single.just(WikiResponse("first")))
            .thenReturn(Single.just(WikiResponse("second")))

        api.randomArticle()
            .test()
            .assertValue(WikiResponse("first"))
            .dispose()

        api.randomArticle()
            .test()
            .assertValue(WikiResponse("second"))
            .dispose()

        Mockito.verify(api, Mockito.times(2)).randomArticle()
    }

    @Test
    fun `getArticleFromTitle calls the api articleFromTitle and returns its value`(){
        Mockito.`when`(api.articleFromTitle("first"))
            .thenReturn(Single.just(WikiResponse("first")))

        Mockito.`when`(api.articleFromTitle("second"))
            .thenReturn(Single.just(WikiResponse("second")))

        api.articleFromTitle("first")
            .test()
            .assertValue(WikiResponse("first"))
            .dispose()

        Mockito.verify(api, Mockito.times(1)).articleFromTitle("first")

        api.articleFromTitle("second")
            .test()
            .assertValue(WikiResponse("second"))
            .dispose()

        Mockito.verify(api, Mockito.times(1)).articleFromTitle("second")
    }
}