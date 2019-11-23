package com.example.wikigamemvi.data.repository.base

import com.example.wikigamemvi.data.model.WikiResponse
import com.example.wikigamemvi.data.model.WikiTitle
import io.reactivex.Single

interface WikipediaRepository {

    fun getRandomArticle(): Single<WikiResponse>

    fun getArticleFromTitle(title: WikiTitle): Single<WikiResponse>

}