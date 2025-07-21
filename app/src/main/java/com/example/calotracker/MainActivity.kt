package com.example.calotracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.calotracker.ui.*
import com.example.calotracker.ui.theme.CalotrackerTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * Activity principale dell'app CaloTracker.
 * Gestisce la configurazione grafica e il sistema di navigazione tra le schermate.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // ✅ Permette contenuti sotto la status bar (utile per splash personalizzati)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        super.onCreate(savedInstanceState)
        setContent {
            val systemUiController = rememberSystemUiController()

            // ✅ Configurazione visiva della status e navigation bar
            SideEffect {
                systemUiController.setStatusBarColor(
                    color = Color.Black,
                    darkIcons = false // Icone bianche
                )
                systemUiController.setNavigationBarColor(
                    color = Color(0xFFB3E5FC), // Colore celeste
                    darkIcons = true
                )
            }

            // ✅ App con tema globale personalizzato
            CalotrackerTheme {
                CaloTrackerApp()
            }
        }
    }
}

/**
 * Entry point dell'app: gestisce la navigazione tra le schermate.
 */
@Composable
fun CaloTrackerApp() {
    val navController = rememberNavController()

    Surface(color = MaterialTheme.colorScheme.background) {
        NavHost(navController = navController, startDestination = "welcome") {

            // 🔸 Schermata iniziale (può essere rimossa cambiando startDestination in "home")
            composable("welcome") {
                MainScreen(navController)
            }

            // 🔸 Schermata principale con diario settimanale
            composable("home") {
                HomeScreen(navController)
            }

            // 🔸 Schermata di inserimento pasto per un determinato giorno
            composable(
                route = "addMeal/{dayOfWeek}",
                arguments = listOf(navArgument("dayOfWeek") {
                    type = NavType.StringType
                })
            ) { backStackEntry ->
                val rawDay = backStackEntry.arguments?.getString("dayOfWeek")
                val supportedDays = listOf(
                    "Lunedì", "Martedì", "Mercoledì", "Giovedì",
                    "Venerdì", "Sabato", "Domenica"
                )
                val dayOfWeek = if (rawDay in supportedDays) rawDay!! else "Lunedì"
                AddMealScreen(navController = navController, dayOfWeek = dayOfWeek)
            }

            // 🔸 Schermata con idee per i pasti (colazione/pranzo/cena)
            composable("mealIdeas") {
                MealIdeasScreen()
            }

            // 🔸 Schermata per inserimento obiettivo settimanale e suggerimento basato sul peso
            composable("weeklyGoal") {
                WeeklyGoalScreen()
            }
        }
    }
}
