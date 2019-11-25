package com.akjaw.domain.usecase

import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.repository.WikiRepository
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
            .thenReturn(Single.just(WikiResponse("target")))

        Mockito.`when`(wikiRepository.getRandomArticle())
            .thenReturn(Single.just(WikiResponse("random")))

        val testObserver = TestObserver<Pair<WikiResponse, WikiResponse>>()

        useCase().subscribe(testObserver)
        testObserver.assertValue { (target, current) ->
            target == WikiResponse("target") &&
                    current == WikiResponse("random")
        }
    }

    @Test
    fun `If the initial target and random article are the same then the function is repated`(){
        Mockito.`when`(targetUseCase(true))
            .thenReturn(Single.just(WikiResponse("target")))

        Mockito.`when`(wikiRepository.getRandomArticle())
            .thenReturn(Single.just(WikiResponse("target")))
            .thenReturn(Single.just(WikiResponse("random")))

        val testObserver = TestObserver<Pair<WikiResponse, WikiResponse>>()

        useCase().subscribe(testObserver)
        testObserver.assertValue { (target, current) ->
            target == WikiResponse("target") &&
                    current == WikiResponse("random")
        }

        verify(targetUseCase, Mockito.times(1)).invoke(true)
        verify(wikiRepository, Mockito.times(2)).getRandomArticle()
    }
}