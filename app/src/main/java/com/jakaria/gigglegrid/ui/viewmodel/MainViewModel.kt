package com.jakaria.gigglegrid.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jakaria.gigglegrid.data.local.JokeDatabase
import com.jakaria.gigglegrid.data.model.Joke
import com.jakaria.gigglegrid.data.repository.JokeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: JokeRepository
    
    // UI State for specific actions
    private val _currentJoke = MutableStateFlow<Joke?>(null)
    val currentJoke: StateFlow<Joke?> = _currentJoke.asStateFlow()
    
    // Settings State
    private val _isDarkTheme = MutableStateFlow(false) // This would ideally come from DataStore
    val isDarkTheme = _isDarkTheme.asStateFlow()
    
    private val _textSizeScale = MutableStateFlow(1.0f)
    val textSizeScale = _textSizeScale.asStateFlow()
    
    private val _safeMode = MutableStateFlow(true)
    val safeMode = _safeMode.asStateFlow()

    // Discovery State
    private val _discoveryJokeList = MutableStateFlow<List<Joke>>(emptyList()) // Ideally api doesn't return list but single joke, but we might simulate list or just show one by one
    // For Discovery we might just show one at a time or fetch a few. JokeAPI gives Single or List (amount param).
    // Let's stick to single joke fetching for now, or maybe fetch 10 at a time if needed.
    
    val favoriteJokes: StateFlow<List<Joke>>
    
    init {
        val db = JokeDatabase.getDatabase(application)
        repository = JokeRepository(db.jokeDao())
        
        favoriteJokes = repository.getFavoriteJokes().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        
        // Initial fetch
        fetchNewJoke()
    }

    fun fetchNewJoke(category: String = "Any", searchTerm: String? = null) {
        viewModelScope.launch {
            try {
                val joke = repository.getRandomJoke(
                    category = category, 
                    searchTerm = searchTerm, 
                    safeMode = _safeMode.value
                )
                _currentJoke.value = joke
            } catch (e: Exception) {
                // Handle error
                e.printStackTrace()
            }
        }
    }

    fun toggleFavorite(joke: Joke) {
        viewModelScope.launch {
            if (joke.isFavorite) {
                repository.removeJoke(joke)
                // Update current joke state if it matches
                if (_currentJoke.value?.id == joke.id) {
                     _currentJoke.value = _currentJoke.value?.copy(isFavorite = false)
                }
            } else {
                repository.saveJoke(joke)
                if (_currentJoke.value?.id == joke.id) {
                     _currentJoke.value = _currentJoke.value?.copy(isFavorite = true)
                }
            }
        }
    }
    
    fun removeFavorite(joke: Joke) {
        viewModelScope.launch {
            repository.removeJoke(joke)
        }
    }

    fun toggleTheme(isDark: Boolean) {
        _isDarkTheme.value = isDark
    }
    
    fun setTextSize(scale: Float) {
        _textSizeScale.value = scale
    }
    
    fun setSafeMode(enabled: Boolean) {
        _safeMode.value = enabled
    }
}
