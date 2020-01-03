package com.akjaw.wikigamemvi.ui.game

import com.akjaw.domain.model.ArticleType
import com.akjaw.domain.model.WikiArticle
import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.usecase.ArticleWinConditionUseCase
import com.akjaw.domain.usecase.GetArticleFromTitleUseCase
import com.akjaw.domain.usecase.InitializeArticlesUseCase
import com.akjaw.wikigamemvi.ui.game.GameAction.*
import com.akjaw.wikigamemvi.ui.game.GameViewEffect.*

import com.akjaw.test_utils.assertLastValue
import com.akjaw.test_utils.assertSecondToLastValue
import com.akjaw.wikigamemvi.data.model.ResponseToArticleMapper
import com.akjaw.wikigamemvi.ui.common.view.ArticleView
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.observers.TestObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


class GameViewModelTest {

    @Mock private lateinit var initializeArticlesUseCase: InitializeArticlesUseCase
    @Mock private lateinit var getArticleFromTitleUseCase: GetArticleFromTitleUseCase
    @Mock private lateinit var winConditionUseCase: ArticleWinConditionUseCase
    private lateinit var responseToArticleMapper: ResponseToArticleMapper
    private lateinit var viewModel: GameViewModel

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        responseToArticleMapper = ResponseToArticleMapper()

        viewModel = GameViewModel(
            initializeArticlesUseCase,
            getArticleFromTitleUseCase,
            winConditionUseCase,
            responseToArticleMapper
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
                    it.checkInitialArticlesFinished(
                        responseToArticleMapper.mapTo(target),
                        responseToArticleMapper.mapTo(current)
                    )
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

            viewModel.process(InitializeArticlesAction)

            viewStateTester
                .assertSecondToLastValue {
                    it.checkInitialArticlesLoading()
                    true
                }

            viewStateTester
                .assertLastValue {
                    it.checkInitialArticlesFinished(
                        responseToArticleMapper.mapTo(target1),
                        responseToArticleMapper.mapTo(current1)
                    )
                    true
                }

            viewModel.process(InitializeArticlesAction)

            viewStateTester
                .assertSecondToLastValue {
                    it.checkInitialArticlesLoading()
                    true
                }

            viewStateTester
                .assertLastValue {
                    it.checkInitialArticlesFinished(
                        responseToArticleMapper.mapTo(target2),
                        responseToArticleMapper.mapTo(current2)
                    )
                    true
                }

            viewStateTester.dispose()
        }

        private fun GameViewState.checkInitialArticlesLoading(){
            val bothAreLoading = currentArticleState.isLoading && targetArticleState.isLoading
            assertTrue(bothAreLoading)
            assertNull(targetArticleState.article)
            assertNull(currentArticleState.article)
        }

        private fun GameViewState.checkInitialArticlesFinished(target: WikiArticle, current: WikiArticle){
            val bothAreLoaded = !currentArticleState.isLoading && !targetArticleState.isLoading
            assertTrue(bothAreLoaded)
            assertEquals(target, targetArticleState.article)
            assertEquals(current, currentArticleState.article)
        }

        @Test
        fun `when an error occurs then both articles state changes accordingly`(){
            Mockito.`when`(initializeArticlesUseCase())
                .thenReturn(Observable.error(Error()))

            val viewStateTester = viewModel.viewState.test()

            viewModel.process(InitializeArticlesAction)

            viewStateTester
                .assertLastValue {
                    checkArticleErrorState(it.targetArticleState)
                    checkArticleErrorState(it.currentArticleState)
                }
        }
    }

    @Nested
    inner class LoadNextArticleActionTests{

        @Test
        fun `it always checks if the winCondition is met`(){
            Mockito.`when`(winConditionUseCase(Mockito.anyString()))
                .thenReturn(Observable.empty())

            Mockito.`when`(getArticleFromTitleUseCase(Mockito.anyString()))
                .thenReturn(Observable.empty())

            viewModel.viewState.test().dispose()

            viewModel.process(LoadNextArticleAction("test1"))
            Mockito.verify(winConditionUseCase, Mockito.times(1)).invoke("test1")

            viewModel.process(LoadNextArticleAction("test2"))
            Mockito.verify(winConditionUseCase, Mockito.times(1)).invoke("test2")

            viewModel.process(LoadNextArticleAction("test1"))
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
            viewModel.process(LoadNextArticleAction(Mockito.anyString()))
            this.assertSecondToLastValue {
                it.checkLoadNextLoading()
            }
            this.assertLastValue {
                it.checkLoadNextFinished(responseToArticleMapper.mapTo(response))
            }
        }

        @Test
        fun `if the win condition is met then emit ShowVictoryScreenEffect with the correct state`(){
            val targetResponse = WikiResponse("target")
            val currentResponse = WikiResponse("current")

            Mockito.`when`(initializeArticlesUseCase())
                .thenReturn(Observable.just(targetResponse to currentResponse))

            Mockito.`when`(winConditionUseCase(Mockito.anyString()))
                .thenReturn(Observable.empty())
                .thenReturn(Observable.just(true))

            Mockito.`when`(getArticleFromTitleUseCase(Mockito.anyString()))
                .thenReturn(Observable.just(WikiResponse(name = "first")))

            val viewStateTester = viewModel.viewState.test()
            val viewEffectsTester = viewModel.viewEffects.test()

            viewEffectsTester.assertEmpty()
            viewModel.initialize()
            viewModel.process(LoadNextArticleAction(""))
            viewModel.process(LoadNextArticleAction("win"))

            viewEffectsTester.assertValueCount(1)
            val expectedEffect = ShowVictoryScreenEffect(
                responseToArticleMapper.mapTo(targetResponse),
                1)
            viewEffectsTester.assertValue(expectedEffect)

            viewStateTester.dispose()
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

            viewModel.process(LoadNextArticleAction(Mockito.anyString()))
            viewStateTester.assertLastValue {
                assertEquals(it.numberOfSteps, 1)
                true
            }

            viewModel.process(LoadNextArticleAction(Mockito.anyString()))
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

            viewModel.process(LoadNextArticleAction(Mockito.anyString()))
            viewStateTester.assertLastValue {
                assertEquals(it.numberOfSteps, 1)
                true
            }

            viewModel.process(LoadNextArticleAction(Mockito.anyString()))
            viewStateTester.assertLastValue {
                assertEquals(it.numberOfSteps, 1)
                true
            }

            viewStateTester.dispose()
        }

        private fun GameViewState.checkLoadNextLoading(): Boolean {
            assertTrue(currentArticleState.isLoading)
            assertNull(currentArticleState.article)

            return true
        }

        private fun GameViewState.checkLoadNextFinished(article: WikiArticle): Boolean {
            assertFalse(currentArticleState.isLoading)
            assertEquals(article, currentArticleState.article)

            return true
        }

        @Test
        fun `when the getArticleFromTitleUseCase has an error then the current article state changes accordingly`(){
            Mockito.`when`(winConditionUseCase(Mockito.anyString()))
                .thenReturn(Observable.empty())

            Mockito.`when`(getArticleFromTitleUseCase(Mockito.anyString()))
                .thenReturn(Observable.error(Error()))

            val viewStateTester = viewModel.viewState.test()

            viewModel.process(LoadNextArticleAction(Mockito.anyString()))
            viewStateTester.assertLastValue {
                checkArticleErrorState(it.currentArticleState)
            }
        }
    }

    fun checkArticleErrorState(article: ArticleState): Boolean {
        assertFalse(article.isLoading)
        assertTrue(article.hasError)
        assertNull(article.article)

        return true
    }

    @Nested
    inner class ToggleArticleModeActionTest{

        @ParameterizedTest
        @EnumSource(ArticleType::class)
        fun `correctly toggles the state of the passed in type`(type: ArticleType){
            val viewStateTester = viewModel.viewState.test()

            viewStateTester.assertLastValue {
                it.checkModeChange(type, ArticleView.ArticleViewMode.COLLAPSED)
            }
            viewStateTester.processToggleModeAndAssert(type, ArticleView.ArticleViewMode.EXPANDED)
            viewStateTester.processToggleModeAndAssert(type, ArticleView.ArticleViewMode.COLLAPSED)

            viewStateTester.dispose()
        }

        @ParameterizedTest
        @EnumSource(ArticleType::class)
        fun `if the article is loading then the mode isn't changed`(type: ArticleType){
            Mockito.`when`(initializeArticlesUseCase())
                .thenReturn(Observable.empty())

            val initialViewState = when(type){
                ArticleType.TARGET -> GameViewState(
                    targetArticleState = ArticleState(
                        isLoading = true
                    )
                )
                ArticleType.CURRENT -> GameViewState(
                    currentArticleState = ArticleState(
                        isLoading = true
                    )
                )
            }

            viewModel = GameViewModel(
                initializeArticlesUseCase,
                getArticleFromTitleUseCase,
                winConditionUseCase,
                responseToArticleMapper,
                initialViewState
            )

            val viewStateTester = viewModel.viewState.test()

            viewStateTester.processToggleModeAndAssert(type, ArticleView.ArticleViewMode.COLLAPSED)
            viewStateTester.processToggleModeAndAssert(type, ArticleView.ArticleViewMode.COLLAPSED)
            viewStateTester.processToggleModeAndAssert(type, ArticleView.ArticleViewMode.COLLAPSED)

            viewStateTester.dispose()
        }

        private fun TestObserver<GameViewState>.processToggleModeAndAssert(
            type: ArticleType,
            expected: ArticleView.ArticleViewMode
        ){

            viewModel.process(ToggleArticleModeAction(type))
            this.assertLastValue {
                it.checkModeChange(type, expected)
            }
        }

        private fun GameViewState.checkModeChange(
            type: ArticleType,
            expected: ArticleView.ArticleViewMode
        ): Boolean {

            val mode = when(type){
                ArticleType.TARGET -> this.targetArticleState.mode
                ArticleType.CURRENT -> this.currentArticleState.mode
            }

            assertEquals(expected, mode)
            return true
        }
    }
}