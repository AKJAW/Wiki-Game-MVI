package com.akjaw.domain.usecase

import com.akjaw.domain.model.WikiResponse
import io.reactivex.Observable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class ArticleWinConditionUseCaseImplTest {
    @Mock private lateinit var targetArticleUseCase: GetTargetArticleUseCase
    private lateinit var useCase: ArticleWinConditionUseCase

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        useCase = ArticleWinConditionUseCaseImpl(targetArticleUseCase)

        RxJavaPlugins.setIoSchedulerHandler {
            Schedulers.trampoline()
        }
    }

    @AfterEach
    fun tearDown() {
        RxJavaPlugins.reset()
    }

    @Test
    fun `when the passed in article is different than the target then return empty`(){
        Mockito.`when`(targetArticleUseCase(false))
            .thenReturn(Observable.just(WikiResponse("target")))

        useCase("current")
            .test()
            .assertValueCount(0)
            .assertComplete()
    }

    @Test
    fun `when the passed in article is the same as the target then return true`(){
        Mockito.`when`(targetArticleUseCase(false))
            .thenReturn(Observable.just(WikiResponse("target")))

        useCase("target")
            .test()
            .assertValue(true)
            .assertComplete()
    }
}