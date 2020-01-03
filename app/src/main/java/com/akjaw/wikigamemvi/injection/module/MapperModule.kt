package com.akjaw.wikigamemvi.injection.module

import com.akjaw.domain.model.Mapper
import com.akjaw.domain.model.WikiArticle
import com.akjaw.domain.model.WikiResponse
import com.akjaw.remote.model.RemoteMapper
import com.akjaw.remote.model.WikiApiResponseEntity
import com.akjaw.wikigamemvi.data.model.ResponseToArticleMapper
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
class MapperModule{

    @Provides
    fun bindResponseToArticleMapper(impl: ResponseToArticleMapper)
            : Mapper<WikiArticle, WikiResponse> {
        return impl
    }

    @Provides
    fun bindRemoteMapper(impl: RemoteMapper)
            : Mapper<WikiApiResponseEntity, WikiResponse> {
        return impl
    }
}