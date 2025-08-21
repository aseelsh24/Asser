package com.example.asser.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val ageCalculationDao: AgeCalculationDao
) : HistoryRepository {

    override fun getRecentCalculations(): Flow<List<AgeCalculation>> {
        return ageCalculationDao.getRecentCalculations()
    }

    override suspend fun saveCalculation(ageCalculation: AgeCalculation) {
        ageCalculationDao.insert(ageCalculation)
        ageCalculationDao.deleteOldestCalculations()
    }
}
