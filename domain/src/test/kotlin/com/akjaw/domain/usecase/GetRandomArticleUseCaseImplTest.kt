package com.akjaw.domain.usecase

import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.repository.WikiRepository
import io.reactivex.Single
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeUnit

class GetRandomArticleUseCaseImplTest {
    @Mock private lateinit var wikiRepository: WikiRepository
    private lateinit var useCase: GetRandomArticleUseCase

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        useCase = GetRandomArticleUseCaseImpl(wikiRepository)

        RxJavaPlugins.setIoSchedulerHandler {
            Schedulers.trampoline()
        }
    }

    @AfterEach
    fun tearDown(){
        RxJavaPlugins.reset()
    }

    @Test
    fun `The use case calls getRandomArticle on the repository once and returns the value`(){
        Mockito.`when`(wikiRepository.getRandomArticle())
            .thenReturn(Single.just(WikiResponse("First")))
            .thenReturn(Single.just(WikiResponse("Second")))

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