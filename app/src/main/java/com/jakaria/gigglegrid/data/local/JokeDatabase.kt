package com.jakaria.gigglegrid.data.local

import androidx.room.*
import com.jakaria.gigglegrid.data.model.Joke
import kotlinx.coroutines.flow.Flow

@Dao
interface JokeDao {
    @Query("SELECT * FROM favorite_jokes")
    fun getAllFavorites(): Flow<List<Joke>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJoke(joke: Joke)

    @Delete
    suspend fun deleteJoke(joke: Joke)
    
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_jokes WHERE id = :id)")
    suspend fun isFavorite(id: Int): Boolean
}

@Database(entities = [Joke::class], version = 1, exportSchema = false)
abstract class JokeDatabase : RoomDatabase() {
    abstract fun jokeDao(): JokeDao

    companion object {
        @Volatile
        private var INSTANCE: JokeDatabase? = null

        fun getDatabase(context: android.content.Context): JokeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    JokeDatabase::class.java,
                    "joke_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
