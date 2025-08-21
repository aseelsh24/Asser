package com.example.asser.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "age_calculations")
data class AgeCalculation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val birthDate: Date,
    val calculatedAt: Date,
    val years: Int,
    val months: Int,
    val days: Int,
    val hours: Int
)
