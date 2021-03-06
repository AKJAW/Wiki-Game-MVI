package com.akjaw.wikigamemvi.util

import com.akjaw.domain.model.WikiArticle
import com.akjaw.domain.model.WikiResponse

fun WikiResponse.toArticle(): WikiArticle {
    return WikiArticle(name, description, imageUrl)
}