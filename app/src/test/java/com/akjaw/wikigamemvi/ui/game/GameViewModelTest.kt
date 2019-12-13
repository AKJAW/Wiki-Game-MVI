package com.akjaw.wikigamemvi.ui.game

import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.usecase.ArticleWinConditionUseCase
import com.akjaw.domain.usecase.GetArticleFromTitleUseCase
import com.akjaw.domain.usecase.GetTargetArticleUseCase
import com.akjaw.domain.usecase.InitializeArticlesUseCase
import com.akjaw.wikigamemvi.ui.game.model.GameViewState
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
    }

    @Test
    fun `after subscribing the initial viewEffects are empty`(){
        viewModel.viewEffects
            .test()
            .assertNoValues()
    }

    @Test
    fun `in the initialization initializeArticlesUseCase is called`(){
        viewModel.viewEffects
            .test()

        Mockito.verify(initializeArticlesUseCase, Mockito.times(1)).invoke()
    }
}