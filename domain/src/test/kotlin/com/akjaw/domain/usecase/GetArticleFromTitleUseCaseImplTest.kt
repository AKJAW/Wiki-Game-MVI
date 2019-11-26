package com.akjaw.domain.usecase

import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.repository.WikiRepository
import io.reactivex.Single
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito

class GetArticleFromTitleUseCaseImplTest {
    private lateinit var wikiRepository: WikiRepository
    private lateinit var useCase: GetArticleFromTitleUseCase

    @Before
    fun setUp() {
        wikiRepository = Mockito.mock(WikiRepository::class.java)
        useCase = GetArticleFromTitleUseCaseImpl(wikiRepository)

        RxJavaPlugins.setIoSchedulerHandler {
            Schedulers.trampoline()
        }
    }

    @After
    fun tearDown() {
        RxJavaPlugins.reset()
    }

    @Test
    fun `The use case returns the article if found`(){
        Mockito.`when`(wikiRepository.getArticleFromTitle("aaa"))
            .thenReturn(Single.just(WikiResponse("aaa")))

        useCase("aaa")
            .test()
            .assertValue(WikiResponse("aaa"))

        Mockito.verify(wikiRepository, Mockito.times(1)).getArticleFromTitle("aaa")
    }
}