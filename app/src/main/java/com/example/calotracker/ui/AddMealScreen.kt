package com.example.calotracker.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.calotracker.data.MealDatabase
import com.example.calotracker.viewmodel.MealViewModel
import com.example.calotracker.viewmodel.MealViewModelFactory
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun AddMealScreen(
    navController: NavHostController,
    dayOfWeek: String
) {
    val context = LocalContext.current
    val dao = remember { MealDatabase.getDatabase(context).mealDao() }
    val viewModel: MealViewModel = viewModel(factory = MealViewModelFactory(dao))

    var mealName by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    val dayMap = mapOf(
        "Lunedì" to DayOfWeek.MONDAY,
        "Martedì" to DayOfWeek.TUESDAY,
        "Mercoledì" to DayOfWeek.WEDNESDAY,
        "Giovedì" to DayOfWeek.THURSDAY,
        "Venerdì" to DayOfWeek.FRIDAY,
        "Sabato" to DayOfWeek.SATURDAY,
        "Domenica" to DayOfWeek.SUNDAY
    )

    val today = LocalDate.now()
    val monday = today.minusDays(today.dayOfWeek.value.toLong() - 1)
    val selectedDayOfWeek = dayMap[dayOfWeek] ?: DayOfWeek.MONDAY

    var selectedDate by remember {
        mutableStateOf(monday.plusDays((selectedDayOfWeek.value - 1).toLong()))
    }

    val giorni = listOf("Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato", "Domenica")

    val suggestions = listOf(
        "Pasta al pomodoro" to 430,
        "Insalata di tonno" to 280,
        "Pollo alla griglia con verdure" to 520,
        "Riso basmati con salmone" to 470,
        "Panino con prosciutto e formaggio" to 410,
        "Zuppa di legumi" to 350,
        "Pizza margherita (1 fetta)" to 300,
        "Uova strapazzate e pane" to 260,
        "Frullato banana e avena" to 180,
        "Yogurt greco con miele" to 160,
        "Frittata di patate" to 330,
        "Cous cous con ceci" to 380,
        "Toast integrale e avocado" to 300,
        "Muesli con latte" to 270,
        "Lasagna" to 550,
        "Insalata di quinoa" to 400,
        "Poke bowl al salmone" to 500,
        "Tortino di verdure" to 360,
        "Hamburger classico" to 600,
        "Spaghetti aglio olio e peperoncino" to 430
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .widthIn(max = 500.dp)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //  Card principale con titolo e form
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Aggiungi Pasto per $dayOfWeek",
                        style = MaterialTheme.typography.headlineSmall.copy(color = Color.Red),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = mealName,
                        onValueChange = { mealName = it },
                        label = { Text("Nome pasto") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = calories,
                        onValueChange = { calories = it },
                        label = { Text("Calorie") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Giorno della settimana", style = MaterialTheme.typography.titleMedium)

                    // ✅ Due righe fisse per i giorni (4 + 3)
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(0..3, 4..6).forEach { range ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                for (i in range) {
                                    val giorno = giorni[i]
                                    val date = monday.plusDays(i.toLong())
                                    AssistChip(
                                        onClick = { selectedDate = date },
                                        label = { Text(giorno.take(3)) },
                                        colors = AssistChipDefaults.assistChipColors(
                                            containerColor = if (selectedDate == date) Color.Red else Color.LightGray,
                                            labelColor = if (selectedDate == date) Color.White else Color.Black
                                        )
                                    )
                                }
                            }
                        }
                    }

                    Button(
                        onClick = {
                            val calInt = calories.toIntOrNull()
                            if (mealName.isNotBlank() && calInt != null) {
                                viewModel.addMealForDate(mealName, calInt, selectedDate)
                                Toast.makeText(context, "Pasto salvato!", Toast.LENGTH_SHORT).show()
                                mealName = ""
                                calories = ""
                                navController.popBackStack()
                            } else {
                                Toast.makeText(context, "Inserisci dati validi", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Salva Pasto", color = Color.White)
                    }
                }
            }

            // 🟧 Suggerimenti
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Suggerimenti più comuni",
                    style = MaterialTheme.typography.titleLarge.copy(color = Color.Red),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                suggestions.chunked(2).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowItems.forEach { (name, kcal) ->
                            Card(
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                TextButton(
                                    onClick = {
                                        mealName = name
                                        calories = kcal.toString()
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "$name\n$kcal kcal",
                                        textAlign = TextAlign.Center,
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                        if (rowItems.size < 2) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

