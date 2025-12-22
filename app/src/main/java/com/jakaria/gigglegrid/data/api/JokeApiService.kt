package com.jakaria.gigglegrid.data.api

import com.jakaria.gigglegrid.data.model.Joke
import retrofit2.http.GET
import retrofit2.http.Query

interface JokeApiService {
    
    // https://v2.jokeapi.dev/joke/Any?safe-mode
    
    @GET("joke/Any")
    suspend fun getRandomJoke(
        @Query("safe-mode") safeMode: String? = null, // Pass "" (empty string) to enable, or null to disable
        @Query("contains") contains: String? = null, // Search term
        @Query("categories") categories: String? = null // e.g., "Programming,Dad"
    ): Joke
}
