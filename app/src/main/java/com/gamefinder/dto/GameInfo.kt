package com.gamefinder.dto

import com.google.gson.annotations.SerializedName

data class GameInfo(var documentId: String = "",var gameId: Int = 0, var title: String = "", var steamUrl: String = "", var status: String = "") {


}