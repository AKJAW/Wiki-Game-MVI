package com.akjaw.wikigamemvi.data.model

import com.akjaw.domain.model.Mapper
import com.akjaw.domain.model.WikiArticle
import com.akjaw.domain.model.WikiResponse
import javax.inject.Inject

class ResponseToArticleMapper @Inject constructor(): Mapper<WikiArticle, WikiResponse>{
    override fun mapFrom(entity: WikiArticle): WikiResponse {
        return WikiResponse(
            entity.name,
            entity.description,
            entity.imageUrl
        )
    }

    override fun mapTo(domain: WikiResponse): WikiArticle {
        return WikiArticle(
            domain.name,
            domain.description,
            domain.imageUrl
        )
    }
}