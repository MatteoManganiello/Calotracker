package com.example.calotracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * DAO per la gestione dei pasti nel database Room.
 */
@Dao
interface MealDao {

    /**
     * Inserisce un nuovo pasto.
     */
    @Insert
    suspend fun insertMeal(meal: Meal)

    /**
     * Restituisce tutti i pasti come flusso osservabile.
     */
    @Query("SELECT * FROM meals")
    fun getAllMeals(): Flow<List<Meal>>

    /**
     * Elimina tutti i pasti dal database.
     */
    @Query("DELETE FROM meals")
    suspend fun deleteAllMeals()

    /**
     * Elimina i pasti registrati tra due date (in formato timestamp).
     */
    @Query("DELETE FROM meals WHERE date BETWEEN :startDate AND :endDate")
    suspend fun deleteMealsBetween(startDate: Long, endDate: Long)
}
