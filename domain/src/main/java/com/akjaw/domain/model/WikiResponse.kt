package com.akjaw.domain.model

data class WikiResponse(
    val name: WikiTitle = "",
    val description: String = "",
    val imageUrl: String? = null,
    val wikipediaUrl: String = "",
    val outgoingTitles: List<WikiTitle> = listOf()
)