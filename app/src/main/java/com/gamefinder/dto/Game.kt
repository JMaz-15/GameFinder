package com.gamefinder.dto

import com.google.gson.annotations.SerializedName

data class Game(@SerializedName("id") var gameId: Int = 0, @SerializedName("title")var title: String = "", @SerializedName("steamUrl") var steamUrl: String = "", @SerializedName("status")var status: String = "") {
    override fun toString(): String {
        return ("$title $steamUrl $status")
    }
}