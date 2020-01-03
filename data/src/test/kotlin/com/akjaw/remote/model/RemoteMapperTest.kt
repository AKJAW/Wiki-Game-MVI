package com.akjaw.remote.model

import com.akjaw.domain.model.WikiResponse
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RemoteMapperTest {
    private lateinit var mapper: RemoteMapper

    @BeforeEach
    fun setUp(){
        mapper = RemoteMapper()
    }

    @Test
    fun `mapFrom maps correctly`(){
        val article = WikiArticleResponseEntity("name", "description", "image", "url")
        val titles = listOf("a", "b", "c", "d")
        val entity = WikiApiResponseEntity(article, titles)
        val responseEntity = mapper.mapFrom(entity)

        val expectedResponse = WikiResponse(article.name, article.description, article.image, article.url, titles)

        assertEquals(expectedResponse, responseEntity)
    }


    @Test
    fun `mapTo maps correctly`(){
        val article = WikiArticleResponseEntity("name", "description", "image", "url")
        val titles = listOf("a", "b", "c", "d")

        val response = WikiResponse(article.name, article.description, article.image, article.url, titles)
        val responseEntity = mapper.mapTo(response)

        val expectedEntity = WikiApiResponseEntity(article, titles)

        assertEquals(expectedEntity, responseEntity)
    }
}