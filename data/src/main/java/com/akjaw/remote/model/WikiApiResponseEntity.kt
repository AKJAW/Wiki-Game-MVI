package com.akjaw.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WikiApiResponseEntity(
    @field:Json(name = "article") val article: WikiArticleResponseEntity,
    @field:Json(name = "links") val links: List<String> = listOf()
)