package com.akjaw.domain.model

typealias WikiTitle = String

data class WikiResponse(
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val wikipediaUrl: String = "",
    val outgoingTitles: List<WikiTitle> = listOf()
)