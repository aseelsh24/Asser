package com.example.asser.data

import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun getRecentCalculations(): Flow<List<AgeCalculation>>
    suspend fun saveCalculation(ageCalculation: AgeCalculation)
}
