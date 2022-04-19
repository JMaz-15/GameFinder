package com.gamefinder.v32001

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gamefinder.dto.Game
import com.gamefinder.service.GameService
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    var games : MutableLiveData<List<Game>> = MutableLiveData<List<Game>>()
    var gameService : GameService = GameService()

    fun fetchGames() {
        viewModelScope.launch {
            var innerGames = gameService.fetchGames()
            games.postValue(innerGames)

        }
    }

}