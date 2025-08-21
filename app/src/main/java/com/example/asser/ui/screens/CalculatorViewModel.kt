package com.example.asser.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asser.data.AgeCalculation
import com.example.asser.data.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit
import javax.inject.Inject

data class CalculatorUiState(
    val birthDate: LocalDate = LocalDate.now(),
    val calculateUntil: LocalDate = LocalDate.now(),
    val calculationResult: AgeCalculationResult? = null,
    val birthdayCountdown: Long? = null,
    val isCalculating: Boolean = false,
    val useToday: Boolean = true
)

data class AgeCalculationResult(
    val years: Int,
    val months: Int,
    val days: Int,
    val hours: Long
)

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    fun onBirthDateSelected(date: LocalDate) {
        _uiState.value = _uiState.value.copy(birthDate = date)
    }

    fun onCalculateUntilDateSelected(date: LocalDate) {
        _uiState.value = _uiState.value.copy(calculateUntil = date)
    }

    fun onUseToday(useToday: Boolean) {
        _uiState.value = _uiState.value.copy(useToday = useToday)
    }

    fun calculateAge() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCalculating = true)
            val birthDate = _uiState.value.birthDate
            val calculateUntil = if (_uiState.value.useToday) LocalDate.now() else _uiState.value.calculateUntil

            val period = Period.between(birthDate, calculateUntil)
            val hours = ChronoUnit.HOURS.between(birthDate.atStartOfDay(), calculateUntil.atStartOfDay())

            val result = AgeCalculationResult(
                years = period.years,
                months = period.months,
                days = period.days,
                hours = hours
            )

            val countdown = calculateBirthdayCountdown(birthDate)

            _uiState.value = _uiState.value.copy(
                calculationResult = result,
                birthdayCountdown = countdown,
                isCalculating = false
            )

            saveCalculation(birthDate, calculateUntil, result)
        }
    }

    private suspend fun saveCalculation(birthDate: LocalDate, calculateUntil: LocalDate, result: AgeCalculationResult) {
        val ageCalculation = AgeCalculation(
            birthDate = java.sql.Date.valueOf(birthDate.toString()),
            calculatedAt = java.sql.Date.valueOf(calculateUntil.toString()),
            years = result.years,
            months = result.months,
            days = result.days,
            hours = result.hours.toInt()
        )
        historyRepository.saveCalculation(ageCalculation)
    }

    private fun calculateBirthdayCountdown(birthDate: LocalDate): Long {
        val today = LocalDate.now()
        val nextBirthday = birthDate.withYear(today.year)
        return if (nextBirthday.isBefore(today) || nextBirthday.isEqual(today)) {
            ChronoUnit.DAYS.between(today, nextBirthday.plusYears(1))
        } else {
            ChronoUnit.DAYS.between(today, nextBirthday)
        }
    }
}
