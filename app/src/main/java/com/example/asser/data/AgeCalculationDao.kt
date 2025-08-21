package com.example.asser.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AgeCalculationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ageCalculation: AgeCalculation)

    @Query("SELECT * FROM age_calculations ORDER BY calculatedAt DESC LIMIT 10")
    fun getRecentCalculations(): Flow<List<AgeCalculation>>

    @Query("DELETE FROM age_calculations WHERE id NOT IN (SELECT id FROM age_calculations ORDER BY calculatedAt DESC LIMIT 10)")
    suspend fun deleteOldestCalculations()
}
