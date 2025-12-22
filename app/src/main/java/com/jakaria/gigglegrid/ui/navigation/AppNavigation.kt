package com.jakaria.gigglegrid.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jakaria.gigglegrid.ui.screens.DiscoveryScreen
import com.jakaria.gigglegrid.ui.screens.FavoritesScreen
import com.jakaria.gigglegrid.ui.screens.HomeScreen
import com.jakaria.gigglegrid.ui.screens.SettingsScreen
import com.jakaria.gigglegrid.ui.viewmodel.MainViewModel

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Daily", Icons.Default.Home)
    object Discovery : Screen("discovery", "Discover", Icons.Default.Search)
    object Favorites : Screen("favorites", "Saved", Icons.Default.Favorite)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

@Composable
fun AppNavigation(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val currentJoke by viewModel.currentJoke.collectAsState()
    val favoriteJokes by viewModel.favoriteJokes.collectAsState()
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()
    val textSizeScale by viewModel.textSizeScale.collectAsState()
    val safeMode by viewModel.safeMode.collectAsState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val screens = listOf(Screen.Home, Screen.Discovery, Screen.Favorites, Screen.Settings)
                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.route == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    currentJoke = currentJoke,
                    onRefresh = { viewModel.fetchNewJoke() },
                    onToggleFavorite = { viewModel.toggleFavorite(it) },
                    onCategorySelected = { category ->
                         viewModel.fetchNewJoke(category = category)
                         // Optional: could navigate to discovery or just fetch in place.
                         // For now fetching in place is fine or we could stay on Home.
                    }
                )
            }
            composable(Screen.Discovery.route) {
                DiscoveryScreen(
                    currentJoke = currentJoke, // Reuse current joke for now or better separate state in VM
                    onSearch = { term -> viewModel.fetchNewJoke(searchTerm = term) },
                    onToggleFavorite = { viewModel.toggleFavorite(it) },
                    onFilterSelected = { cat -> viewModel.fetchNewJoke(category = cat) }
                )
            }
            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    favoriteJokes = favoriteJokes,
                    onRemoveFavorite = { viewModel.removeFavorite(it) }
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen(
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = { viewModel.toggleTheme(it) },
                    textSizeScale = textSizeScale,
                    onTextSizeChange = { viewModel.setTextSize(it) },
                    safeMode = safeMode,
                    onSafeModeChange = { viewModel.setSafeMode(it) }
                )
            }
        }
    }
}
