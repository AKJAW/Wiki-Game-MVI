package com.akjaw.wikigamemvi.data.model

import com.akjaw.domain.model.WikiArticle
import com.akjaw.domain.model.WikiResponse
import com.akjaw.remote.model.RemoteMapper
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ResponseToArticleMapperTest {
    private lateinit var mapper: ResponseToArticleMapper

    @BeforeEach
    fun setUp(){
        mapper = ResponseToArticleMapper()
    }

    @Test
    fun `mapFrom maps correctly`(){
        val outgoingTitles = listOf("1", "2", "3")
        val response = WikiResponse("name", "description", "image", "url", outgoingTitles)
        val article = mapper.mapTo(response)

        val expectedArticle = WikiArticle("name", "description", "image")


        assertEquals(expectedArticle, article)
    }

    @Test
    fun `mapTo maps correctly`(){
        val article = WikiArticle("name", "description", "image")
        val response = mapper.mapFrom(article)

        val expectedResponse = WikiResponse("name", "description", "image", "", listOf())

        assertEquals(expectedResponse, response)
    }

}