package com.example.asser.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.asser.R
import com.example.asser.data.AgeCalculation
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val history by viewModel.historyUiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.calculation_history), style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (history.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(R.string.no_history))
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(history) { calculation ->
                    HistoryItem(calculation = calculation)
                }
            }
        }
    }
}

@Composable
fun HistoryItem(calculation: AgeCalculation) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val formattedBirthDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(calculation.birthDate)
            val formattedCalculatedAt = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(calculation.calculatedAt)

            Text(text = "Birth Date: $formattedBirthDate", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Calculated At: $formattedCalculatedAt", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${calculation.years} years, ${calculation.months} months, ${calculation.days} days",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
