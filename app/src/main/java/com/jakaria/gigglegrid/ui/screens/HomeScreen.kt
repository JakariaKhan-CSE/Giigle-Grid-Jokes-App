package com.jakaria.gigglegrid.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jakaria.gigglegrid.data.model.Joke
import com.jakaria.gigglegrid.ui.theme.CardGradientEnd
import com.jakaria.gigglegrid.ui.theme.CardGradientStart

@Composable
fun HomeScreen(
    currentJoke: Joke?,
    onRefresh: () -> Unit,
    onToggleFavorite: (Joke) -> Unit,
    onCategorySelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally // Centered content
    ) {
        Text(
            text = "Daily Dose",
            style = MaterialTheme.typography.displaySmall, // Somewhat large title
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Featured Joke Card
        JokeCard(
            joke = currentJoke,
            onToggleFavorite = onToggleFavorite,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 250.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Quick Actions
        Button(
            onClick = onRefresh,
            modifier = Modifier.fillMaxWidth(0.6f),
            shape = RoundedCornerShape(50)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = "New Joke")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Get New Joke")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Quick Categories",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start)
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            CategoryButton("Programming", "Programming", onCategorySelected)
            CategoryButton("Dad Jokes", "Pun", onCategorySelected) // API typically uses "Pun" or "Spooky" etc, lets send "Pun" for dad jokes approx or just "Any"
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            CategoryButton("Dark", "Dark", onCategorySelected)
            CategoryButton("Random", "Any", onCategorySelected)
        }
    }
}

@Composable
fun JokeCard(
    joke: Joke?,
    onToggleFavorite: (Joke) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(24.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(CardGradientStart, CardGradientEnd)
                    )
                )
                .padding(24.dp)
        ) {
            if (joke != null) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = joke.category,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.2f), RoundedCornerShape(50))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(
                        text = joke.getText(),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                        textAlign = TextAlign.Center
                    )
                }

                IconButton(
                    onClick = { onToggleFavorite(joke) },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (joke.isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = "Favorite",
                        tint = Color.Yellow
                    )
                }
            } else {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun CategoryButton(
    label: String,
    categoryApiValue: String,
    onClick: (String) -> Unit
) {
    OutlinedButton(
        onClick = { onClick(categoryApiValue) },
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(label)
    }
}
