package com.akjaw.domain.usecase

import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.repository.WikiRepository
import io.reactivex.Single
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito
import java.util.concurrent.TimeUnit

class GetRandomArticleUseCaseImplTest {
    private lateinit var wikiRepository: WikiRepository
    private lateinit var useCase: GetRandomArticleUseCase

    @Before
    fun setUp() {
        wikiRepository = Mockito.mock(WikiRepository::class.java)
        useCase = GetRandomArticleUseCaseImpl(wikiRepository)

        RxJavaPlugins.setIoSchedulerHandler {
            Schedulers.trampoline()
        }
    }

    @After
    fun tearDown(){
        RxJavaPlugins.reset()
    }

    @Test
    fun `The use case calls getRandomArticle on the repository once and returns the value`(){
        Mockito.`when`(wikiRepository.getRandomArticle())
            .thenReturn(Single.just(WikiResponse("First")))
            .thenReturn(Single.just(WikiResponse("Second")))

        val scheduler = TestScheduler()
        RxJavaPlugins.setComputationSchedulerHandler {
            scheduler
        }

        useCase()
            .test()
            .assertValue(WikiResponse("First"))

        Mockito.verify(wikiRepository, Mockito.times(1)).getRandomArticle()

        useCase()
            .test()
            .assertValue(WikiResponse("Second"))

        Mockito.verify(wikiRepository, Mockito.times(2)).getRandomArticle()
    }
}