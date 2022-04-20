package com.gamefinder.service

import com.gamefinder.RetrofitClientInstance
import com.gamefinder.dao.IGameDAO
import com.gamefinder.dto.Game
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class GameService {

    internal suspend fun fetchGames() : List<Game>? {
        return withContext(Dispatchers.IO) {
            val service = RetrofitClientInstance.retrofitInstance?.create(IGameDAO::class.java)
            val games = async { service?.getAllGames() }
            var result = games.await()?.awaitResponse()?.body()
            return@withContext result
        }
    }
}