package com.akjaw.domain.usecase

import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.repository.WikiRepository
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito

class GetTargetArticleUseCaseImplTest {
    private lateinit var wikiRepository: WikiRepository
    private lateinit var usecase: GetTargetArticleUseCase


    @Before
    fun setUp(){
        wikiRepository = Mockito.mock(WikiRepository::class.java)
        usecase = GetTargetArticleUseCaseImpl(wikiRepository)

        RxJavaPlugins.setIoSchedulerHandler {
            Schedulers.trampoline()
        }
    }

    @Test
    fun `subsequent invocations without refresh return the same value and doesn't call the repository`() {
        Mockito.`when`(wikiRepository.getRandomArticle())
            .thenReturn(Single.just(WikiResponse(name = "first")))
            .thenReturn(Single.just(WikiResponse(name = "second")))
            .thenReturn(Single.just(WikiResponse(name = "third")))

        usecase(true)
            .test()
            .assertValue(WikiResponse(name = "first"))

        usecase(false)
            .test()
            .assertValue(WikiResponse(name = "first"))

        usecase(false)
            .test()
            .assertValue(WikiResponse(name = "first"))

        Mockito.verify(wikiRepository, Mockito.times(1)).getRandomArticle()
    }

    @Test
    fun `invocations with isNewTargetArticle = true causes the value to change`() {
        Mockito.`when`(wikiRepository.getRandomArticle())
            .thenReturn(Single.just(WikiResponse(name = "first")))
            .thenReturn(Single.just(WikiResponse(name = "second")))
            .thenReturn(Single.just(WikiResponse(name = "third")))

        usecase(true)
            .test()
            .assertValue(WikiResponse(name = "first"))

        usecase(true)
            .test()
            .assertValue(WikiResponse(name = "second"))

        usecase(true)
            .test()
            .assertValue(WikiResponse(name = "third"))

        Mockito.verify(wikiRepository, Mockito.times(3)).getRandomArticle()
    }
}