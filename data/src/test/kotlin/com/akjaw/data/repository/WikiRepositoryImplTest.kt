package com.akjaw.data.repository

import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.repository.WikiRepository
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Test

import org.junit.Before
import org.mockito.Mockito

class WikiRepositoryImplTest {
    private lateinit var wikiRepository: WikiRepository

    @Before
    fun setUp(){
        val apiMock = Mockito.mock(WikipediaApi::class.java)
        Mockito.`when`(apiMock.randomArticle())
            .thenReturn(Single.just(WikiResponse(name = "1")))
            .thenReturn(Single.just(WikiResponse(name = "2")))
            .thenReturn(Single.just(WikiResponse(name = "3")))
        wikiRepository = WikiRepositoryImpl(apiMock)
    }

    @Test
    fun `subsequent calls to getTargetArticles without refresh return the same value`() {
        val testObserver = TestObserver<WikiResponse>()
        wikiRepository.getTargetArticle(false).subscribe(testObserver)
        testObserver.assertValue(WikiResponse(name = "1"))

        val testObserver2 = TestObserver<WikiResponse>()
        wikiRepository.getTargetArticle(false).subscribe(testObserver2)
        testObserver2.assertValue(WikiResponse(name = "1"))

        val testObserver3 = TestObserver<WikiResponse>()
        wikiRepository.getTargetArticle(false).subscribe(testObserver3)
        testObserver3.assertValue(WikiResponse(name = "1"))
    }

    @Test
    fun `calling getTargetArticles with refresh causes the value to change`() {
        val testObserver = TestObserver<WikiResponse>()
        wikiRepository.getTargetArticle(false).subscribe(testObserver)
        testObserver.assertValue(WikiResponse(name = "1"))

        val testObserver2 = TestObserver<WikiResponse>()
        wikiRepository.getTargetArticle(true).subscribe(testObserver2)
        testObserver2.assertValue(WikiResponse(name = "2"))

        val testObserver3 = TestObserver<WikiResponse>()
        wikiRepository.getTargetArticle(false).subscribe(testObserver3)
        testObserver3.assertValue(WikiResponse(name = "2"))
    }

    @Test
    fun getRandomArticle() {

    }

    @Test
    fun getArticleFromTitle() {

    }
}