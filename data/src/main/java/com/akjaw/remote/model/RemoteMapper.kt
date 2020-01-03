package com.akjaw.remote.model

import com.akjaw.domain.model.Mapper
import com.akjaw.domain.model.WikiResponse
import javax.inject.Inject

class RemoteMapper @Inject constructor():
    Mapper<WikiApiResponseEntity, WikiResponse> {
    override fun mapFrom(entity: WikiApiResponseEntity): WikiResponse {
        return WikiResponse(
            name = entity.article.name,
            description = entity.article.description,
            imageUrl = entity.article.image,
            wikipediaUrl = entity.article.url,
            outgoingTitles = entity.titles
        )
    }

    override fun mapTo(domain: WikiResponse): WikiApiResponseEntity {
        val article = WikiArticleResponseEntity(
            name = domain.name,
            description = domain.description,
            image = domain.imageUrl,
            url = domain.wikipediaUrl
        )

        return WikiApiResponseEntity(
            article = article,
            titles = domain.outgoingTitles
        )
    }

}