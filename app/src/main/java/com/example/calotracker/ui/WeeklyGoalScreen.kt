package com.example.calotracker.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.edit

enum class ObiettivoUtente(val label: String) {
    DIMAGRIRE("Dimagrire"),
    MANTENERE("Mantenere il peso"),
    INGRASSARE("Aumentare di peso")
}

@Composable
fun WeeklyGoalScreen() {
    val context = LocalContext.current

    var peso by remember { mutableStateOf("") }
    var suggerito by remember { mutableIntStateOf(0) }
    var obiettivo by remember { mutableStateOf("") }
    var obiettivoUtente by remember { mutableStateOf(ObiettivoUtente.MANTENERE) }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val saved = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
            .getInt("weekly_goal", 0)
        if (saved > 0) obiettivo = saved.toString()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 400.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8FF)),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Imposta Obiettivo Settimanale",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )

                OutlinedTextField(
                    value = peso,
                    onValueChange = {
                        peso = it
                        suggerito = it.toIntOrNull()?.let { p ->
                            val kcalPerKg = when (obiettivoUtente) {
                                ObiettivoUtente.DIMAGRIRE -> 22
                                ObiettivoUtente.MANTENERE -> 25
                                ObiettivoUtente.INGRASSARE -> 28
                            }
                            p * kcalPerKg * 7
                        } ?: 0
                    },
                    label = { Text("Peso corporeo (kg)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Column(horizontalAlignment = Alignment.Start) {
                    Text("Obiettivo", style = MaterialTheme.typography.titleMedium)

                    Box(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = { expanded = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text(obiettivoUtente.label, color = Color.White)
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ObiettivoUtente.values().forEach { ob ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            ob.label,
                                            color = Color.Red
                                        )
                                    },
                                    onClick = {
                                        obiettivoUtente = ob
                                        peso.toIntOrNull()?.let { p ->
                                            val kcalPerKg = when (ob) {
                                                ObiettivoUtente.DIMAGRIRE -> 22
                                                ObiettivoUtente.MANTENERE -> 25
                                                ObiettivoUtente.INGRASSARE -> 28
                                            }
                                            suggerito = p * kcalPerKg * 7
                                        }
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                if (suggerito > 0) {
                    Text(
                        text = "Suggerimento: $suggerito kcal/settimana",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Divider(thickness = 1.dp)

                OutlinedTextField(
                    value = obiettivo,
                    onValueChange = { obiettivo = it },
                    label = { Text("Obiettivo personalizzato (kcal/settimana)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                //  Bottone Salva
                Button(
                    onClick = {
                        val calInt = obiettivo.toIntOrNull()
                        if (calInt != null && calInt > 0) {
                            context.getSharedPreferences("prefs", Context.MODE_PRIVATE).edit {
                                putInt("weekly_goal", calInt)
                            }
                            Toast.makeText(context, "Obiettivo salvato!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Inserisci un numero valido", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Salva obiettivo", color = Color.White)
                }

                //  Bottone Reset (blu)
                Button(
                    onClick = {
                        context.getSharedPreferences("prefs", Context.MODE_PRIVATE).edit {
                            remove("weekly_goal")
                        }
                        obiettivo = ""
                        suggerito = 0
                        peso = ""
                        Toast.makeText(context, "Obiettivo resettato", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                ) {
                    Text("Reset obiettivo", color = Color.White)
                }
            }
        }
    }
}
