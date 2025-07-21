package com.example.calotracker.ui

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.calotracker.R
import com.example.calotracker.data.MealDatabase
import com.example.calotracker.viewmodel.MealViewModel
import com.example.calotracker.viewmodel.MealViewModelFactory
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/**
 * Schermata principale dell'app (Home).
 * Mostra i pasti inseriti, calorie totali e obiettivo settimanale.
 */
@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    val dao = remember { MealDatabase.getDatabase(context).mealDao() }
    val viewModel: MealViewModel = viewModel(factory = MealViewModelFactory(dao))
    val meals by viewModel.meals.collectAsState()

    // Stato per l'obiettivo settimanale salvato nelle preferenze
    var weeklyGoal by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        weeklyGoal = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
            .getInt("weekly_goal", 0)
    }

    // Calcolo inizio/fine settimana
    val today = LocalDate.now()
    val monday = today.minusDays(today.dayOfWeek.value.toLong() - 1)
    val sunday = monday.plusDays(6)

    // Somma delle calorie settimanali
    val weeklyCalories = meals
        .filter {
            val mealDate = Instant.ofEpochMilli(it.date).atZone(ZoneId.systemDefault()).toLocalDate()
            mealDate in monday..sunday
        }
        .sumOf { it.calories }

    val giorni = listOf("Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato", "Domenica")
    val scrollState = rememberScrollState()
    val lightBlue = Color(0xFFB3E5FC)

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🔳 Card superiore con logo e riepilogo settimanale
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo CaloTracker",
                    modifier = Modifier
                        .height(100.dp)
                        .padding(end = 12.dp)
                )

                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = lightBlue),
                    elevation = CardDefaults.cardElevation(5.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Settimana corrente", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Ingerite: $weeklyCalories kcal",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (weeklyGoal > 0) {
                            Text(
                                "Obiettivo: $weeklyGoal kcal",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }

            // 🔽 Sezione giorni della settimana con pasti
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                giorni.forEachIndexed { index, giorno ->
                    val date = monday.plusDays(index.toLong())

                    val mealsForDay = meals.filter {
                        val mealDate = Instant.ofEpochMilli(it.date).atZone(ZoneId.systemDefault()).toLocalDate()
                        mealDate == date
                    }

                    val totalCalories = mealsForDay.sumOf { it.calories }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = lightBlue)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = giorno,
                                        style = MaterialTheme.typography.titleMedium.copy(color = Color.Red)
                                    )
                                    if (mealsForDay.isEmpty()) {
                                        Text("Nessun pasto")
                                    } else {
                                        mealsForDay.forEach {
                                            Text("- ${it.name} (${it.calories} kcal)")
                                        }
                                        Text(
                                            "Totale: $totalCalories kcal",
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }

                                Button(
                                    onClick = {
                                        navController.navigate("addMeal/$giorno")
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                    shape = RoundedCornerShape(20)
                                ) {
                                    Text("Aggiungi", color = Color.White)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 🔁 Pulsante per resettare i dati della settimana
                Button(
                    onClick = {
                        viewModel.deleteMealsBetween(monday, sunday)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("Reset settimana", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
