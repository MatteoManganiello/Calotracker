package com.example.calotracker.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calotracker.R

/**
 * Schermata che mostra idee per i pasti divise per Colazione, Pranzo e Cena.
 * Ogni pasto è accompagnato da una stima delle calorie.
 */
@Composable
fun MealIdeasScreen() {
    // Mappa con idee suddivise per categoria e calorie
    val mealIdeas = mapOf(
        "Colazione" to listOf(
            "Yogurt greco con frutta fresca" to 150,
            "Toast integrale con marmellata" to 180,
            "Uova strapazzate e pane integrale" to 250,
            "Smoothie alla banana e avena" to 200,
            "Cornflakes con latte" to 160,
            "Porridge con miele e frutti di bosco" to 220
        ),
        "Pranzo" to listOf(
            "Insalata di quinoa con ceci e pomodorini" to 400,
            "Pasta integrale con verdure grigliate" to 450,
            "Petto di pollo con riso e zucchine" to 480,
            "Zuppa di legumi con crostini" to 350,
            "Poke bowl con salmone e avocado" to 500,
            "Lasagna di verdure" to 520
        ),
        "Cena" to listOf(
            "Frittata con spinaci e cipolle" to 300,
            "Zuppa di verdure e farro" to 320,
            "Riso basmati con salmone al vapore" to 450,
            "Cous cous con verdure e hummus" to 400,
            "Tortino di patate e zucchine" to 370,
            "Insalata mista con tonno e uova" to 330
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(32.dp))

            // Intestazione con logo e titolo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo CaloTracker",
                    modifier = Modifier
                        .height(50.dp)
                        .padding(end = 12.dp)
                )
                Text(
                    text = "Idee per i pasti",
                    color = Color.Red,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        // Sezione card per ogni tipo di pasto
        mealIdeas.forEach { (mealType, ideas) ->
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = mealType,
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = Color(0xFF33691E),
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        ideas.forEach { (food, kcal) ->
                            Text(
                                text = "• $food - $kcal kcal",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

