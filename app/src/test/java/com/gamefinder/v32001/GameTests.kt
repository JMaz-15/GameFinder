package com.gamefinder.v32001

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.gamefinder.dto.Game
import com.gamefinder.service.GameService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class GameTests {
    @get: Rule
    var rule: TestRule = InstantTaskExecutorRule()
    lateinit var gameService: GameService
    var allGames : List<Game>? = ArrayList<Game>()

    lateinit var mvm : MainViewModel

    @MockK
    lateinit var mockGameService: GameService

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun initMocksAndMainThread() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `Given Game Data Is Available When I Search Construction Simulator 2 US - Pocket I Should Receive URL, Simulation, AVAILABLE`() = runTest {
        GivenGameServiceIsInitialized()
        WhenGameDataIsReadAndParsed()
        ThenTheGameCollectionShouldContainURLSimulationAVAILABLE()
    }

    private fun GivenGameServiceIsInitialized() {
        gameService = GameService()
    }

    private suspend fun WhenGameDataIsReadAndParsed() {
        allGames = gameService.fetchGames()
    }

    private fun ThenTheGameCollectionShouldContainURLSimulationAVAILABLE() {
        assertNotNull(allGames)
        assertTrue(allGames!!.isNotEmpty())
        var containsData = false
        allGames!!.forEach {
           if((it.title.equals("AI: The Somnium Files"))
               && (it.steamUrl.equals("https://store.steampowered.com/app/948740"))
                       && (it.status.equals("AVAILABLE")))
                           containsData = true
        }
        assertTrue(containsData)
    }

}