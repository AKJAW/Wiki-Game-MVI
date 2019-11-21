package com.example.wikigamemvi.data.model

typealias WikiUrl = String

data class WikiResponse(
    val name: String,
    val description: String,
    val imageUrl: String,
    val url: WikiUrl,
    val outgoingUrls: List<WikiUrl>
)