package com.gamefinder.v32001

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gamefinder.dto.Game
import com.gamefinder.dto.GameInfo
import com.gamefinder.service.GameService
import com.gamefinder.service.IGameService
import kotlinx.coroutines.launch
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings


class MainViewModel (var gameService: IGameService = GameService()) : ViewModel() {

    var games : MutableLiveData<List<Game>> = MutableLiveData<List<Game>>()
    var localGame : MutableLiveData<List<GameInfo>> = MutableLiveData<List<GameInfo>>()

    private lateinit var firestore : FirebaseFirestore

    init{
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    private fun listenToGames(){
        firestore.collection("Games").addSnapshotListener{
            snapshot, e ->
            if (e != null){
                Log.w("Listen Failed", e)
                return@addSnapshotListener
            }
            snapshot?.let{
                val allGames = ArrayList<GameInfo>()
                val documents = snapshot.documents
                documents.forEach{
                    val localGame = it.toObject(GameInfo::class.java)
                    localGame?.let{
                    allGames.add(it)
                }
                }
                localGame.value = allGames
            }
        }
    }
    fun fetchGames() {
        viewModelScope.launch {
            var innerGames = gameService.fetchGames()
            games.postValue(innerGames)

        }
    }

    fun save(gameInfo: GameInfo) {
       val document = firestore.collection("Games").document()
        gameInfo.documentId = document.id
        val handle = document.set(gameInfo)
        handle.addOnSuccessListener {
            Log.d("Firebase", "Document Saved")
        }
        handle.addOnFailureListener{
            Log.d("Firebase", "Document Save Failed $it")
        }

    }

}