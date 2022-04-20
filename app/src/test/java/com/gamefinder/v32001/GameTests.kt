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

    @Test
    fun `given a view model with live data is populated with games then the results should return the title, url, genre and availability`() = runTest {
        GivenViewModelIsInitializedWithMockData()
        WhenJSONDataIsReadAndParsed()
        ThenResultsShouldContainTitleURLGenreAndAvailability()
    }

    private fun GivenViewModelIsInitializedWithMockData() {
        val games = ArrayList<Game>()
        games.add(Game(10036581,
            "AI: The Somnium Files",
            "https://store.steampowered.com/app/948740",
            "AVAILABLE")
        )
        games.add(Game(100894211,
            "Saviors of Sapphire Wings / Stranger of Sword City",
            "https://store.steampowered.com/app/1363840",
            "AVAILABLE")
        )

        coEvery { mockGameService.fetchGames() } returns games

        mvm = MainViewModel()
        mvm.gameService = mockGameService
    }

    private fun WhenJSONDataIsReadAndParsed() {
        mvm.fetchGames()
    }

    private fun ThenResultsShouldContainTitleURLGenreAndAvailability() {
        var allGames : List<Game>? = ArrayList<Game>()
        val latch = CountDownLatch(1)
        val observer = object : Observer<List<Game>>{
            override fun onChanged(t: List<Game>?) {
                allGames = t
                latch.countDown()
                mvm.games.removeObserver(this)
            }

        }
        mvm.games.observeForever(observer)
        latch.await(10, TimeUnit.SECONDS)
        assertNotNull(allGames)
        assertTrue(allGames!!.isNotEmpty())
        var containsData = false
        allGames!!.forEach {
            if((it.gameId.equals(10036581)
                && (it.title.equals("AI: The Somnium Files")))
                && (it.steamUrl.equals("https://store.steampowered.com/app/948740"))
                        && (it.status.equals("AVAILABLE")))
                containsData = true
        }
        assertTrue(containsData)
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