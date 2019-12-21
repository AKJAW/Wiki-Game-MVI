package com.akjaw.remote

import com.akjaw.remote.model.WikiApiResponseEntity
import com.akjaw.base.EntityMapper
import com.akjaw.base.WikipediaApi
import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.model.WikiTitle
import com.akjaw.remote.model.RemoteMapper
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy

class WikipediaRemoteApi(
    private val api: WikiApiService,
    private val mapper: EntityMapper<WikiApiResponseEntity, WikiResponse>
): WikipediaApi {
    override fun randomArticle(): Single<WikiResponse> {
        return api.randomArticle().map {
            mapper.mapFromEntity(it)
        }
    }

    override fun articleFromTitle(title: WikiTitle): Single<WikiResponse> {
        return api.articleFromTitle(title).map {
            mapper.mapFromEntity(it)
        }
    }

}


fun main() {
    val remoteApi = WikipediaRemoteApi(ApiFactory.api, RemoteMapper())
    remoteApi.randomArticle()
        .subscribeBy(
            onSuccess = {
                println("s")
            }
        )
    Thread.sleep(2000)
}