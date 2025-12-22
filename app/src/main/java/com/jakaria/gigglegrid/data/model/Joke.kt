package com.jakaria.gigglegrid.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

// Using JokeAPI format as primary source
// https://v2.jokeapi.dev/joke/Any?safe-mode

@Entity(tableName = "favorite_jokes")
data class Joke(
    @PrimaryKey
    val id: Int,
    val type: String, // "single" or "twopart"
    val category: String,
    
    @SerializedName("joke")
    val jokeContent: String? = null, // For single part jokes
    
    val setup: String? = null, // For two part jokes
    val delivery: String? = null, // For two part jokes
    
    var isFavorite: Boolean = false,
    val safe: Boolean = true
) {
    fun getText(): String {
        return if (type == "single") {
            jokeContent ?: ""
        } else {
            "$setup\n\n$delivery"
        }
    }
}
