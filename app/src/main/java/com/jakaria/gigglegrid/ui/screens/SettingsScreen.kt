package com.jakaria.gigglegrid.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    isDarkTheme: Boolean,
    onToggleTheme: (Boolean) -> Unit,
    textSizeScale: Float,
    onTextSizeChange: (Float) -> Unit,
    safeMode: Boolean,
    onSafeModeChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Theme Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Dark Mode", style = MaterialTheme.typography.bodyLarge)
            Switch(checked = isDarkTheme, onCheckedChange = onToggleTheme)
        }
        
        Divider(modifier = Modifier.padding(vertical = 16.dp))
        
        // Safe Mode Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                 Text("Safe Mode", style = MaterialTheme.typography.bodyLarge)
                 Text("Filter NSFW content", style = MaterialTheme.typography.bodySmall)
            }
           
            Switch(checked = safeMode, onCheckedChange = onSafeModeChange)
        }

         Divider(modifier = Modifier.padding(vertical = 16.dp))

        // Text Size
        Text("Text Size Multiplier: ${String.format("%.1f", textSizeScale)}x", style = MaterialTheme.typography.bodyLarge)
        Slider(
            value = textSizeScale,
            onValueChange = onTextSizeChange,
            valueRange = 0.8f..1.5f,
            steps = 5
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        Text(
            "Powered by JokeAPI",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
         Spacer(modifier = Modifier.height(32.dp))
    }
}
