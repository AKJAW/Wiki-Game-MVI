package com.example.wikigamemvi.data.model

data class WikiArticle(
    val name: String,
    val description: String,
    val imageUrl: String
) {
    companion object {
        fun fromResponse(wikiResponse: WikiResponse): WikiArticle {
            val (name, description, imageUrl) = wikiResponse
            return WikiArticle(name, description, imageUrl)
        }
    }
}