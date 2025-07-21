package com.example.calotracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Database Room principale dell'app CaloTracker.
 * Contiene la tabella dei pasti (Meal).
 */
@Database(entities = [Meal::class], version = 2)
abstract class MealDatabase : RoomDatabase() {

    // DAO per accedere ai metodi CRUD relativi ai pasti
    abstract fun mealDao(): MealDao

    companion object {
        @Volatile
        private var INSTANCE: MealDatabase? = null

        /**
         * Restituisce un'istanza singleton del database.
         * Usa fallbackToDestructiveMigration per gestire cambi di versione durante lo sviluppo.
         */
        fun getDatabase(context: Context): MealDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MealDatabase::class.java,
                    "meal_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
