package com.gamefinder.dao

import com.gamefinder.dto.Game
import retrofit2.Call
import retrofit2.http.GET

interface IGameDAO {
    @GET("/supported-public-game-list/gfnpc.json?JSON")
    fun getAllGames() : Call<ArrayList<Game>>

}