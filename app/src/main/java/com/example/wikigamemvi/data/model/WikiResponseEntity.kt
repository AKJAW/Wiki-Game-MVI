package com.example.wikigamemvi.data.model

data class WikiResponseEntity(
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val wikipediaUrl: String = "",
    val outgoingTitles: List<WikiTitle> = listOf()
)