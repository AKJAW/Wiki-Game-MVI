package com.akjaw.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WikiArticleResponseEntity(
    @field:Json(name = "name") val name: String = "",
    @field:Json(name = "description") val description: String = "",
    @field:Json(name = "image") val image: String = "",
    @field:Json(name = "url") val url: String = ""
)