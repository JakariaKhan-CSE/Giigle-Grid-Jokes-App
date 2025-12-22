package com.jakaria.gigglegrid.data.repository

import com.jakaria.gigglegrid.data.api.JokeApiService
import com.jakaria.gigglegrid.data.local.JokeDao
import com.jakaria.gigglegrid.data.model.Joke
import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JokeRepository(private val jokeDao: JokeDao) {

    private val api: JokeApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://v2.jokeapi.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(JokeApiService::class.java)
    }

    suspend fun getRandomJoke(category: String = "Any", searchTerm: String? = null, safeMode: Boolean = true): Joke {
        val safeModeParam = if (safeMode) "" else null
        return api.getRandomJoke(
            categories = category,
            contains = searchTerm,
            safeMode = safeModeParam
        )
    }

    fun getFavoriteJokes(): Flow<List<Joke>> {
        return jokeDao.getAllFavorites()
    }

    suspend fun saveJoke(joke: Joke) {
        val favoriteJoke = joke.copy(isFavorite = true)
        jokeDao.insertJoke(favoriteJoke)
    }

    suspend fun removeJoke(joke: Joke) {
        jokeDao.deleteJoke(joke)
    }
}
