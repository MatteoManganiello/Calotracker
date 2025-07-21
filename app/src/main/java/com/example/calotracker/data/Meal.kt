package com.example.calotracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entità che rappresenta un pasto nel database Room.
 * Contiene: id, data (timestamp), nome del pasto e calorie.
 */
@Entity(tableName = "meals")
data class Meal(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,          // ID generato automaticamente

    val date: Long,           // Timestamp della data del pasto (millisecondi da epoch)

    val name: String,         // Nome del pasto (es. "Colazione", "Pasta al sugo")

    val calories: Int         // Calorie del pasto
)
