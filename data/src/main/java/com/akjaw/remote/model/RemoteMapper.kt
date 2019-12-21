package com.akjaw.remote.model

import com.akjaw.remote.model.WikiApiResponseEntity
import com.akjaw.remote.model.WikiArticleResponseEntity
import com.akjaw.base.EntityMapper
import com.akjaw.domain.model.WikiResponse
import javax.inject.Inject

class RemoteMapper @Inject constructor(): EntityMapper<WikiApiResponseEntity, WikiResponse> {
    override fun mapFromEntity(entity: WikiApiResponseEntity): WikiResponse {
        return WikiResponse(
            name = entity.article.name,
            description = entity.article.description,
            imageUrl = entity.article.image,
            wikipediaUrl = entity.article.url,
            outgoingTitles = entity.links
        )
    }

    override fun mapToEntity(domain: WikiResponse): WikiApiResponseEntity {
        val article = WikiArticleResponseEntity(
            name = domain.name,
            description = domain.description,
            image = domain.imageUrl,
            url = domain.wikipediaUrl
        )

        return WikiApiResponseEntity(
            article = article,
            links = domain.outgoingTitles
        )
    }

}