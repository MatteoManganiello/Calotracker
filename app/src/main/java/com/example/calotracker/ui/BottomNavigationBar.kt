package com.example.calotracker.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

/**
 * Barra di navigazione inferiore dell'app.
 * Contiene l'accesso all'obiettivo settimanale e alle idee per i pasti.
 */
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val lightBlue = Color(0xFFB3E5FC) // Colore di sfondo per la barra

    NavigationBar(
        tonalElevation = 8.dp,
        containerColor = lightBlue
    ) {
        // Pulsante per impostare l'obiettivo settimanale
        NavigationBarItem(
            icon = { Icon(Icons.Default.Flag, contentDescription = "Obiettivo") },
            label = { Text("Obiettivo") },
            selected = false,
            onClick = { navController.navigate("weeklyGoal") }
        )

        //  Pulsante per accedere alle idee pasto
        NavigationBarItem(
            icon = { Icon(Icons.Default.Lightbulb, contentDescription = "Idee") },
            label = { Text("Idee") },
            selected = false,
            onClick = { navController.navigate("mealIdeas") }
        )
    }
}
