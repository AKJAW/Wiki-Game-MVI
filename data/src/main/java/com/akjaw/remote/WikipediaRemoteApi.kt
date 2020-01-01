package com.akjaw.remote

import com.akjaw.remote.model.WikiApiResponseEntity
import com.akjaw.base.EntityMapper
import com.akjaw.base.WikipediaApi
import com.akjaw.domain.model.WikiLanguage
import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.model.WikiTitle
import com.akjaw.remote.model.RemoteMapper
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject
import javax.inject.Named

class WikipediaRemoteApi @Inject constructor(
    private val language: WikiLanguage,
    private val api: WikiApiService,
    private val mapper: RemoteMapper
): WikipediaApi {
    override fun randomArticle(): Single<WikiResponse> {
        return api.randomArticle(language).map {
            mapper.mapFromEntity(it)
        }
    }

    override fun articleFromTitle(title: WikiTitle): Single<WikiResponse> {
        return api.articleFromTitle(language, title).map {
            mapper.mapFromEntity(it)
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