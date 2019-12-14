package com.akjaw.wikigamemvi.ui.game

import com.akjaw.domain.model.WikiArticle
import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.usecase.ArticleWinConditionUseCase
import com.akjaw.domain.usecase.GetArticleFromTitleUseCase
import com.akjaw.domain.usecase.GetTargetArticleUseCase
import com.akjaw.domain.usecase.InitializeArticlesUseCase
import com.akjaw.wikigamemvi.ui.game.model.GameAction
import com.akjaw.wikigamemvi.ui.game.model.GameViewState
import com.akjaw.wikigamemvi.util.toArticle
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito

class GameViewModelTest {

    private lateinit var initializeArticlesUseCase: InitializeArticlesUseCase
    private lateinit var getArticleFromTitleUseCase: GetArticleFromTitleUseCase
    private lateinit var winConditionUseCase: ArticleWinConditionUseCase
    private lateinit var viewModel: GameViewModel

    @BeforeEach
    fun setUp() {
        initializeArticlesUseCase = Mockito.mock(InitializeArticlesUseCase::class.java)
        getArticleFromTitleUseCase = Mockito.mock(GetArticleFromTitleUseCase::class.java)
        winConditionUseCase = Mockito.mock(ArticleWinConditionUseCase::class.java)

        viewModel = GameViewModel(
            initializeArticlesUseCase,
            getArticleFromTitleUseCase,
            winConditionUseCase
        )

        RxAndroidPlugins.setMainThreadSchedulerHandler {
            Schedulers.trampoline()
        }

        RxAndroidPlugins.setInitMainThreadSchedulerHandler {
            Schedulers.trampoline()
        }
    }

    @AfterEach
    fun tearDown() {
        RxJavaPlugins.reset()
    }

    @Test
    fun `after subscribing the initial viewState is returned`(){
        viewModel.viewState
            .test()
            .assertValue(GameViewState())
            .dispose()
    }

    @Test
    fun `after subscribing the initial viewEffects are empty`(){
        viewModel.viewEffects
            .test()
            .assertNoValues()
            .dispose()
    }

    @Nested
    inner class InitializationTests{
        @Test
        fun `in the initialization initializeArticlesUseCase is called`(){
            viewModel.viewEffects
                .test()

            Mockito.verify(initializeArticlesUseCase, Mockito.times(1)).invoke()
        }

        @Test
        fun `after initialization the target and current are first loading and then set`(){
            val target = WikiResponse(name = "target")
            val current = WikiResponse(name = "current")
            Mockito.`when`(initializeArticlesUseCase())
                .thenReturn(Observable.just(target to current))

            val viewStateTester = viewModel.viewState.test()

            viewStateTester
                .assertValueAt(1) {
                    it.checkInitialArticlesLoading()
                    true
                }

            viewStateTester
                .assertValueAt(2) {
                    it.checkInitialArticlesFinished(target.toArticle(), current.toArticle())
                    true
                }

            viewStateTester.dispose()
        }

        @Test
        fun `processing InitializeArticlesAction causes new articles to be loaded`(){
            val target1 = WikiResponse(name = "target1")
            val current1 = WikiResponse(name = "current1")

            val target2 = WikiResponse(name = "target2")
            val current2 = WikiResponse(name = "current2")

            Mockito.`when`(initializeArticlesUseCase())
                .thenReturn(Observable.just(target1 to current1))
                .thenReturn(Observable.just(target2 to current2))

            val viewStateTester = viewModel.viewState.test()

            viewModel.process(GameAction.InitializeArticlesAction)

            viewStateTester
                .assertValueAt(3) {
                    it.checkInitialArticlesLoading()
                    true
                }

            viewStateTester
                .assertValueAt(4) {
                    it.checkInitialArticlesFinished(target2.toArticle(), current2.toArticle())
                    true
                }

            viewStateTester.dispose()
        }

        private fun GameViewState.checkInitialArticlesLoading(){
            val bothAreLoading = isCurrentArticleLoading && isTargetArticleLoading
            assertTrue(bothAreLoading)
            assertNull(targetArticle)
            assertNull(currentArticle)
        }

        private fun GameViewState.checkInitialArticlesFinished(target: WikiArticle, current: WikiArticle){
            val bothAreLoaded = !isCurrentArticleLoading && !isTargetArticleLoading
            assertTrue(bothAreLoaded)
            assertEquals(targetArticle, target)
            assertEquals(currentArticle, current)
        }

        //TODO error handling
    }


}