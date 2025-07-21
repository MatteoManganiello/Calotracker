package com.example.calotracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calotracker.data.Meal
import com.example.calotracker.data.MealDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class MealViewModel(private val dao: MealDao) : ViewModel() {

    val meals: StateFlow<List<Meal>> = dao.getAllMeals()
        .map { list ->
            list.sortedByDescending { meal ->
                Instant.ofEpochMilli(meal.date).atZone(ZoneId.systemDefault()).toLocalDate()
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addMealForDate(name: String, calories: Int, date: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val timestamp = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val meal = Meal(
                name = name,
                calories = calories,
                date = timestamp
            )
            dao.insertMeal(meal)
        }
    }

    fun deleteAllMeals() {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteAllMeals()
        }
    }

    fun deleteMealsBetween(startDate: LocalDate, endDate: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val startMillis = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val endMillis = endDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            dao.deleteMealsBetween(startMillis, endMillis)
        }
    }
}
