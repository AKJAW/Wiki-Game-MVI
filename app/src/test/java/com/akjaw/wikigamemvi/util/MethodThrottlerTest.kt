package com.akjaw.wikigamemvi.util

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.concurrent.TimeUnit

class MethodThrottlerTest {
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var mockClick: (Unit) -> Unit
    private lateinit var scheduler: TestScheduler

    @BeforeEach
    fun setUp(){
        compositeDisposable = CompositeDisposable()

        @Suppress("UNCHECKED_CAST")
        mockClick = Mockito.mock<(Unit) -> Unit>(Function1::class.java as Class<Function1<Unit, Unit>>)

        scheduler = TestScheduler()

        RxJavaPlugins.setComputationSchedulerHandler { scheduler }
    }

    @AfterEach
    fun tearDown(){
        RxJavaPlugins.reset()

        compositeDisposable.clear()
    }

    @Test
    fun `when onClick are called one after another inside the time window then the method is only invoked once`(){
        Mockito.verify(mockClick, Mockito.times(0)).invoke(Unit)

        val throttler =
            MethodThrottler(compositeDisposable, 500, mockClick)

        throttler.onClick(Unit)
        throttler.onClick(Unit)
        throttler.onClick(Unit)

        Mockito.verify(mockClick, Mockito.times(1)).invoke(Unit)
    }

    @Test
    fun `when onClick is called after the time window it invokes the method again`(){
        Mockito.verify(mockClick, Mockito.times(0)).invoke(Unit)

        val throttler =
            MethodThrottler(compositeDisposable, 500, mockClick)

        throttler.onClick(Unit)
        Mockito.verify(mockClick, Mockito.times(1)).invoke(Unit)

        scheduler.advanceTimeBy(500, TimeUnit.MILLISECONDS)

        throttler.onClick(Unit)
        Mockito.verify(mockClick, Mockito.times(2)).invoke(Unit)
    }

    @Test
    fun `when the method invocation throws an error the stream is recreated`(){
        Mockito.`when`(mockClick.invoke(Unit))
            .thenThrow(Error())
            .thenReturn(Unit)

        Mockito.verify(mockClick, Mockito.times(0)).invoke(Unit)

        val throttler =
            MethodThrottler(compositeDisposable, 500, mockClick)

        throttler.onClick(Unit)

        //Error
        Mockito.verify(mockClick, Mockito.times(1)).invoke(Unit)

        throttler.onClick(Unit)
        throttler.onClick(Unit)
        Mockito.verify(mockClick, Mockito.times(2)).invoke(Unit)
    }

    @Test
    fun `when clearing the passed in compositeDisposable then the stream is ended`(){
        Mockito.verify(mockClick, Mockito.times(0)).invoke(Unit)

        val throttler =
            MethodThrottler(compositeDisposable, 500, mockClick)

        compositeDisposable.clear()

        throttler.onClick(Unit)

        Mockito.verify(mockClick, Mockito.times(0)).invoke(Unit)

    }
}