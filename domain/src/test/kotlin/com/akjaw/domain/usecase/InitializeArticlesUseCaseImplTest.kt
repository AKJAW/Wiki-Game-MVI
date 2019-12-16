package com.akjaw.domain.usecase

import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.repository.WikiRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class InitializeArticlesUseCaseImplTest {
    @Mock private lateinit var wikiRepository: WikiRepository
    @Mock private lateinit var targetUseCase: GetTargetArticleUseCase
    private lateinit var useCase: InitializeArticlesUseCase

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        useCase = InitializeArticlesUseCaseImpl(wikiRepository, targetUseCase)

        RxJavaPlugins.setIoSchedulerHandler {
            Schedulers.trampoline()
        }
    }

    @AfterEach
    fun tearDown(){
        RxJavaPlugins.reset()
    }

    @Test
    fun `The use case correctly returns one target and one random article`(){
        Mockito.`when`(targetUseCase(true))
            .thenReturn(Observable.just(WikiResponse("target")))

        Mockito.`when`(wikiRepository.getRandomArticle())
            .thenReturn(Single.just(WikiResponse("random")))

        useCase()
            .test()
            .assertValue(
                WikiResponse("target") to WikiResponse("random")
            )

        verify(targetUseCase, Mockito.times(1)).invoke(true)
        verify(wikiRepository, Mockito.times(1)).getRandomArticle()
    }

    @Test
    fun `If the initial target and random article are the same then the function is repeated`(){
        Mockito.`when`(targetUseCase(true))
            .thenReturn(Observable.just(WikiResponse("target")))

        Mockito.`when`(wikiRepository.getRandomArticle())
            .thenReturn(Single.just(WikiResponse("target")))
            .thenReturn(Single.just(WikiResponse("random")))

        useCase()
            .test()
            .assertValue(
                WikiResponse("target") to WikiResponse("random")
            )

        verify(targetUseCase, Mockito.times(1)).invoke(true)
        verify(wikiRepository, Mockito.times(2)).getRandomArticle()
    }
}