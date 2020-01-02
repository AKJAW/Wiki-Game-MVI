package com.akjaw.remote.model

import com.akjaw.domain.model.WikiResponse
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
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
    fun `mapFromEntity maps correctly`(){
        val article = WikiArticleResponseEntity("name", "description", "image", "url")
        val titles = listOf("a", "b", "c", "d")
        val entity = WikiApiResponseEntity(article, titles)
        val responseEntity = mapper.mapFromEntity(entity)

        //TODO take null into account
        val expectedResponse = WikiResponse(article.name, article.description, article.image!!, article.url, titles)

        assertEquals(expectedResponse, responseEntity)
    }


    @Test
    fun `mapToEntity maps correctly`(){
        val article = WikiArticleResponseEntity("name", "description", "image", "url")
        val titles = listOf("a", "b", "c", "d")
        //TODO take null into account
        val response = WikiResponse(article.name, article.description, article.image!!, article.url, titles)
        val responseEntity = mapper.mapToEntity(response)

        val expectedEntity = WikiApiResponseEntity(article, titles)

        assertEquals(expectedEntity, responseEntity)
    }
}