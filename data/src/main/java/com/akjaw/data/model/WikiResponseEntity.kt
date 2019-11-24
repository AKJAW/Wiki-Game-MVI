package com.akjaw.data.model

import com.akjaw.domain.model.WikiTitle

data class WikiResponseEntity(
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val wikipediaUrl: String = "",
    val outgoingTitles: List<WikiTitle> = listOf()
)