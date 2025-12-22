package com.jakaria.gigglegrid.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.jakaria.gigglegrid.data.model.Joke

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoveryScreen(
    currentJoke: Joke?,
    onSearch: (String) -> Unit,
    onFilterSelected: (String) -> Unit, // category
    onToggleFavorite: (Joke) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search keywords...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            shape = MaterialTheme.shapes.large,
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch(searchText) })
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Categories", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        
        // Filter Chips Row (Scrollable if needed, but simple row for now)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = false,
                onClick = { onFilterSelected("Programming") },
                label = { Text("Dev") }
            )
            FilterChip(
                selected = false,
                onClick = { onFilterSelected("Misc") },
                label = { Text("Misc") }
            )
            FilterChip(
                selected = false,
                onClick = { onFilterSelected("Pun") },
                label = { Text("Pun") }
            )
             FilterChip(
                selected = false,
                onClick = { onFilterSelected("Any") },
                label = { Text("All") }
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Display Result - Reusing JokeCard because api gives one random joke typically
        // Unless we change to search endpoint which can give list. 
        // For simplicity, lets show the "discovered" joke here.
        
        if (currentJoke != null) {
            Text("Result:", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(8.dp))
            JokeCard(
                joke = currentJoke, 
                onToggleFavorite = onToggleFavorite,
                modifier = Modifier.fillMaxWidth().heightIn(min = 200.dp)
            )
        }
    }
}
