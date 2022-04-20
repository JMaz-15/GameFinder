package com.gamefinder.v32001

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gamefinder.dto.Game
import com.gamefinder.service.GameService
import com.gamefinder.service.IGameService
import kotlinx.coroutines.launch

class MainViewModel (var gameService: IGameService = GameService()) : ViewModel() {

    var games : MutableLiveData<List<Game>> = MutableLiveData<List<Game>>()

    fun fetchGames() {
        viewModelScope.launch {
            var innerGames = gameService.fetchGames()
            games.postValue(innerGames)

        }
    }

}