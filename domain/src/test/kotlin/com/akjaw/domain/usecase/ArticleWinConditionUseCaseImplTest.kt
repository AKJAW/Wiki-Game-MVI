package com.akjaw.domain.usecase

import com.akjaw.domain.model.WikiResponse
import io.reactivex.Observable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before

import org.junit.Test
import org.mockito.Mockito

class ArticleWinConditionUseCaseImplTest {

    private lateinit var useCase: ArticleWinConditionUseCase
    private lateinit var targetArticleUseCase: GetTargetArticleUseCase

    @Before
    fun setUp() {
        targetArticleUseCase = Mockito.mock(GetTargetArticleUseCase::class.java)
        useCase = ArticleWinConditionUseCaseImpl(targetArticleUseCase)

        RxJavaPlugins.setIoSchedulerHandler {
            Schedulers.trampoline()
        }
    }

    @After
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