package com.akjaw.domain.usecase

import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.repository.WikiRepository
import io.reactivex.Single
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock

import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class GetArticleFromTitleUseCaseImplTest {
    @Mock private lateinit var wikiRepository: WikiRepository
    private lateinit var useCase: GetArticleFromTitleUseCase

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        useCase = GetArticleFromTitleUseCaseImpl(wikiRepository)

        RxJavaPlugins.setIoSchedulerHandler {
            Schedulers.trampoline()
        }
    }

    @AfterEach
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