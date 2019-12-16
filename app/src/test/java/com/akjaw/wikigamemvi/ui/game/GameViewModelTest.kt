package com.akjaw.wikigamemvi.ui.game

import com.akjaw.domain.model.ArticleType
import com.akjaw.domain.model.WikiArticle
import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.usecase.ArticleWinConditionUseCase
import com.akjaw.domain.usecase.GetArticleFromTitleUseCase
import com.akjaw.domain.usecase.InitializeArticlesUseCase
import com.akjaw.test_utils.assertLastValue
import com.akjaw.test_utils.assertSecondToLastValue
import com.akjaw.wikigamemvi.ui.common.ArticleView

import com.akjaw.wikigamemvi.ui.game.model.GameAction
import com.akjaw.wikigamemvi.ui.game.model.GameViewEffect
import com.akjaw.wikigamemvi.ui.game.model.GameViewState
import com.akjaw.wikigamemvi.util.toArticle
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.observers.TestObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.Mockito


class GameViewModelTest {

    //TODO annotation mock?
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

        RxJavaPlugins.setIoSchedulerHandler {
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
            viewModel.viewState
                .test()

            viewModel.initialize()

            Mockito.verify(initializeArticlesUseCase, Mockito.times(1)).invoke()
        }

        @Test
        fun `after initialization the target and current are first loading and then set`(){
            val target = WikiResponse(name = "target")
            val current = WikiResponse(name = "current")
            Mockito.`when`(initializeArticlesUseCase())
                .thenReturn(Observable.just(target to current))

            val viewStateTester = viewModel.viewState.test()

            viewModel.initialize()

            viewStateTester
                .assertSecondToLastValue {
                    it.checkInitialArticlesLoading()
                    true
                }

            viewStateTester
                .assertLastValue {
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
                .assertSecondToLastValue {
                    it.checkInitialArticlesLoading()
                    true
                }

            viewStateTester
                .assertLastValue {
                    it.checkInitialArticlesFinished(target1.toArticle(), current1.toArticle())
                    true
                }

            viewModel.process(GameAction.InitializeArticlesAction)

            viewStateTester
                .assertSecondToLastValue {
                    it.checkInitialArticlesLoading()
                    true
                }

            viewStateTester
                .assertLastValue {
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
            assertEquals(target, targetArticle)
            assertEquals(current, currentArticle)
        }

        //TODO error handling
    }

    @Nested
    inner class LoadNextArticleActionTests{
        @BeforeEach
        fun setUp(){
            val target = WikiResponse(name = "target")
            val current = WikiResponse(name = "current")
            Mockito.`when`(initializeArticlesUseCase())
                .thenReturn(Observable.just(target to current))
        }

        @Test
        fun `it always checks if the winCondition is met`(){
            Mockito.`when`(winConditionUseCase(Mockito.anyString()))
                .thenReturn(Observable.empty())

            Mockito.`when`(getArticleFromTitleUseCase(Mockito.anyString()))
                .thenReturn(Observable.empty())

            viewModel.viewState.test().dispose()

            viewModel.process(GameAction.LoadNextArticleAction("test1"))
            Mockito.verify(winConditionUseCase, Mockito.times(1)).invoke("test1")

            viewModel.process(GameAction.LoadNextArticleAction("test2"))
            Mockito.verify(winConditionUseCase, Mockito.times(1)).invoke("test2")

            viewModel.process(GameAction.LoadNextArticleAction("test1"))
            Mockito.verify(winConditionUseCase, Mockito.times(2)).invoke("test1")
        }

        @Test
        fun `it loads the next article if the win condition is not met`(){
            Mockito.`when`(winConditionUseCase(Mockito.anyString()))
                .thenReturn(Observable.empty())

            val response1 = WikiResponse(name = "first")
            val response2 = WikiResponse(name = "second")
            val response3 = WikiResponse(name = "third")

            Mockito.`when`(getArticleFromTitleUseCase(Mockito.anyString()))
                .thenReturn(Observable.just(response1))
                .thenReturn(Observable.just(response2))
                .thenReturn(Observable.just(response3))

            val viewStateTester = viewModel.viewState.test()

            viewStateTester.processLoadNextAndAssertValues(response1)
            viewStateTester.processLoadNextAndAssertValues(response2)
            viewStateTester.processLoadNextAndAssertValues(response3)

            viewStateTester.dispose()
        }

        private fun TestObserver<GameViewState>.processLoadNextAndAssertValues(
            response: WikiResponse
        ) {
            viewModel.process(GameAction.LoadNextArticleAction(Mockito.anyString()))
            this.assertSecondToLastValue {
                it.checkLoadNextLoading()
            }
            this.assertLastValue {
                it.checkLoadNextFinished(response.toArticle())
            }
        }

        @Test
        fun `if the win condition is met then emit ShowVictoryScreenEffect`(){
            Mockito.`when`(winConditionUseCase("win"))
                .thenReturn(Observable.just(true))

            Mockito.`when`(getArticleFromTitleUseCase(Mockito.anyString()))
                .thenReturn(Observable.just(WikiResponse(name = "first")))

            val viewEffectsTester = viewModel.viewEffects.test()

            viewEffectsTester.assertEmpty()

            viewModel.process(GameAction.LoadNextArticleAction("win"))

            viewEffectsTester.assertValueCount(1)
            viewEffectsTester.assertValue(GameViewEffect.ShowVictoryScreenEffect)

            viewEffectsTester.dispose()
        }

        @Test
        fun `every time a new article is loaded increment numberOfSteps`(){
            Mockito.`when`(winConditionUseCase(Mockito.anyString()))
                .thenReturn(Observable.empty())

            Mockito.`when`(getArticleFromTitleUseCase(Mockito.anyString()))
                .thenReturn(Observable.just(WikiResponse(name = "first")))

            val viewStateTester = viewModel.viewState.test()

            viewStateTester.assertLastValue {
                assertEquals(it.numberOfSteps, 0)
                true
            }

            viewModel.process(GameAction.LoadNextArticleAction(Mockito.anyString()))
            viewStateTester.assertLastValue {
                assertEquals(it.numberOfSteps, 1)
                true
            }

            viewModel.process(GameAction.LoadNextArticleAction(Mockito.anyString()))
            viewStateTester.assertLastValue {
                assertEquals(it.numberOfSteps, 2)
                true
            }

            viewStateTester.dispose()
        }

        @Test
        fun `if the win condition is met then don't increment numberOfSteps`(){
            Mockito.`when`(winConditionUseCase(Mockito.anyString()))
                .thenReturn(Observable.empty())
                .thenReturn(Observable.just(true))

            Mockito.`when`(getArticleFromTitleUseCase(Mockito.anyString()))
                .thenReturn(Observable.just(WikiResponse(name = "first")))

            val viewStateTester = viewModel.viewState.test()

            viewModel.process(GameAction.LoadNextArticleAction(Mockito.anyString()))
            viewStateTester.assertLastValue {
                assertEquals(it.numberOfSteps, 1)
                true
            }

            viewModel.process(GameAction.LoadNextArticleAction(Mockito.anyString()))
            viewStateTester.assertLastValue {
                assertEquals(it.numberOfSteps, 1)
                true
            }

            viewStateTester.dispose()
        }

        private fun GameViewState.checkLoadNextLoading(): Boolean {
            assertTrue(isCurrentArticleLoading)
            assertNull(currentArticle)

            return true
        }

        private fun GameViewState.checkLoadNextFinished(article: WikiArticle): Boolean {
            assertFalse(isCurrentArticleLoading)
            assertEquals(article, currentArticle)

            return true
        }

        //TODO errors
    }

    @Nested
    inner class ToggleArticleModeActionTest{
        @BeforeEach
        fun setUp(){
            val target = WikiResponse(name = "target")
            val current = WikiResponse(name = "current")
            Mockito.`when`(initializeArticlesUseCase())
                .thenReturn(Observable.just(target to current))
        }

        @ParameterizedTest
        @EnumSource(ArticleType::class)
        fun `correctly toggles the state of the passed in type`(type: ArticleType){
            val viewStateTester = viewModel.viewState.test()

            viewStateTester.assertLastValue {
                it.checkModeChange(type, ArticleView.ArticleViewMode.COLLAPSED)
            }

            viewModel.process(GameAction.ToggleArticleModeAction(type))

            viewStateTester.assertLastValue {
                it.checkModeChange(type, ArticleView.ArticleViewMode.EXPANDED)
            }

            viewModel.process(GameAction.ToggleArticleModeAction(type))

            viewStateTester.assertLastValue {
                it.checkModeChange(type, ArticleView.ArticleViewMode.COLLAPSED)
            }

            viewStateTester.dispose()
        }

        @ParameterizedTest
        @EnumSource(ArticleType::class)
        fun `if the article is loading then the mode isn't changed`(type: ArticleType){
            Mockito.`when`(initializeArticlesUseCase())
                .thenReturn(Observable.empty())

            val initialViewState = when(type){
                ArticleType.TARGET -> GameViewState(isTargetArticleLoading = true)
                ArticleType.CURRENT -> GameViewState(isCurrentArticleLoading = true)
            }

            val viewModel = GameViewModel(
                initializeArticlesUseCase,
                getArticleFromTitleUseCase,
                winConditionUseCase,
                initialViewState
            )

            val viewStateTester = viewModel.viewState.test()

            viewStateTester.assertLastValue {
                it.checkModeChange(type, ArticleView.ArticleViewMode.COLLAPSED)
            }

            viewModel.process(GameAction.ToggleArticleModeAction(type))

            viewStateTester.assertLastValue {
                it.checkModeChange(type, ArticleView.ArticleViewMode.COLLAPSED)
            }

            viewModel.process(GameAction.ToggleArticleModeAction(type))

            viewStateTester.assertLastValue {
                it.checkModeChange(type, ArticleView.ArticleViewMode.COLLAPSED)
            }

            viewStateTester.dispose()
        }

        private fun GameViewState.checkModeChange(
            type: ArticleType,
            expected: ArticleView.ArticleViewMode): Boolean {

            val mode = when(type){
                ArticleType.TARGET -> this.targetArticleMode
                ArticleType.CURRENT -> this.currentArticleMode
            }

            assertEquals(expected, mode)
            return true
        }
    }
}