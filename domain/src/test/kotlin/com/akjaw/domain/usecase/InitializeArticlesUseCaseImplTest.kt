package com.akjaw.domain.usecase

import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.repository.WikiRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify

class InitializeArticlesUseCaseImplTest {

    private lateinit var useCase: InitializeArticlesUseCase
    private lateinit var wikiRepository: WikiRepository
    private lateinit var targetUseCase: GetTargetArticleUseCase

    @Before
    fun setUp() {
        wikiRepository = Mockito.mock(WikiRepository::class.java)
        targetUseCase = Mockito.mock(GetTargetArticleUseCase::class.java)
        useCase = InitializeArticlesUseCaseImpl(wikiRepository, targetUseCase)
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