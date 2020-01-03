package com.akjaw.remote

import com.akjaw.domain.model.Mapper
import com.akjaw.base.WikipediaApi
import com.akjaw.domain.model.WikiLanguage
import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.model.WikiTitle
import com.akjaw.remote.model.RemoteMapper
import com.akjaw.remote.model.WikiApiResponseEntity
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class WikipediaRemoteApi @Inject constructor(
    private val language: WikiLanguage,
    private val apiService: WikiApiService,
    private val mapper: Mapper<WikiApiResponseEntity, WikiResponse>
): WikipediaApi {
    override fun randomArticle(): Single<WikiResponse> {
        return apiService.randomArticle(language).map {
            mapper.mapFrom(it)
        }
    }

    override fun articleFromTitle(title: WikiTitle): Single<WikiResponse> {
        return apiService.articleFromTitle(language, title).map {
            mapper.mapFrom(it)
        }
    }

}


fun main() {
    val remoteApi = WikipediaRemoteApi("pl", ApiFactory.api, RemoteMapper())
    remoteApi.randomArticle()
        .subscribeBy(
            onSuccess = {
                println("s")
            }
        )
    Thread.sleep(2000)
}