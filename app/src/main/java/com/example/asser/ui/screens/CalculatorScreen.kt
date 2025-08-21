package com.example.asser.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.asser.R
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var showBirthDatePicker by remember { mutableStateOf(false) }
    var showCalculateUntilDatePicker by remember { mutableStateOf(false) }

    if (showBirthDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showBirthDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showBirthDatePicker = false }) {
                    Text(text = stringResource(android.R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { showBirthDatePicker = false }) {
                    Text(text = stringResource(android.R.string.cancel))
                }
            }
        ) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = uiState.birthDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            )
            DatePicker(
                state = datePickerState,
                dateValidator = { it <= Instant.now().toEpochMilli() }
            )
            LaunchedEffect(datePickerState.selectedDateMillis) {
                datePickerState.selectedDateMillis?.let {
                    viewModel.onBirthDateSelected(Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate())
                }
            }
        }
    }

    if (showCalculateUntilDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showCalculateUntilDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showCalculateUntilDatePicker = false }) {
                    Text(text = stringResource(android.R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { showCalculateUntilDatePicker = false }) {
                    Text(text = stringResource(android.R.string.cancel))
                }
            }
        ) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = uiState.calculateUntil.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            )
            DatePicker(state = datePickerState)
            LaunchedEffect(datePickerState.selectedDateMillis) {
                datePickerState.selectedDateMillis?.let {
                    viewModel.onCalculateUntilDateSelected(Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate())
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.age_calculator), style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        DateSelection(
            label = stringResource(R.string.birth_date),
            date = uiState.birthDate,
            onClick = { showBirthDatePicker = true }
        )

        Spacer(modifier = Modifier.height(16.dp))

        CalculateUntil(
            useToday = uiState.useToday,
            onUseTodayChange = { viewModel.onUseToday(it) },
            customDate = uiState.calculateUntil,
            onCustomDateClick = { showCalculateUntilDatePicker = true }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.calculateAge() },
            enabled = !uiState.isCalculating
        ) {
            Text(text = stringResource(R.string.calculate))
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isCalculating) {
            CircularProgressIndicator()
        }

        uiState.calculationResult?.let { result ->
            ResultCard(result = result, onShareClick = {
                shareResult(context, result)
            })
        }

        uiState.birthdayCountdown?.let { countdown ->
            BirthdayCountdownCard(countdown = countdown)
        }
    }
}

@Composable
fun DateSelection(label: String, date: LocalDate, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text(text = "$label: ${date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))}")
    }
}

@Composable
fun CalculateUntil(
    useToday: Boolean,
    onUseTodayChange: (Boolean) -> Unit,
    customDate: LocalDate,
    onCustomDateClick: () -> Unit
) {
    Column {
        Text(text = stringResource(R.string.calculate_age_until), style = MaterialTheme.typography.titleMedium)
        Row(
            Modifier
                .fillMaxWidth()
                .selectable(
                    selected = useToday,
                    onClick = { onUseTodayChange(true) }
                )
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = useToday, onClick = { onUseTodayChange(true) })
            Text(text = stringResource(R.string.today), modifier = Modifier.padding(start = 8.dp))
        }
        Row(
            Modifier
                .fillMaxWidth()
                .selectable(
                    selected = !useToday,
                    onClick = { onUseTodayChange(false) }
                )
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = !useToday, onClick = { onUseTodayChange(false) })
            Text(text = stringResource(R.string.custom_date), modifier = Modifier.padding(start = 8.dp))
        }
        if (!useToday) {
            Spacer(modifier = Modifier.height(8.dp))
            DateSelection(
                label = stringResource(R.string.custom_date),
                date = customDate,
                onClick = onCustomDateClick
            )
        }
    }
}

@Composable
fun ResultCard(result: AgeCalculationResult, onShareClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = stringResource(R.string.result), style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ResultItem(value = result.years.toString(), label = stringResource(R.string.years))
                ResultItem(value = result.months.toString(), label = stringResource(R.string.months))
                ResultItem(value = result.days.toString(), label = stringResource(R.string.days))
                ResultItem(value = result.hours.toString(), label = stringResource(R.string.hours))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onShareClick, modifier = Modifier.align(Alignment.End)) {
                Text(text = stringResource(R.string.share_result))
            }
        }
    }
}

@Composable
fun ResultItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.headlineMedium)
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun BirthdayCountdownCard(countdown: Long) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(R.string.birthday_countdown), style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "$countdown ${stringResource(R.string.days)}", style = MaterialTheme.typography.headlineMedium)
            Text(text = stringResource(R.string.days_left_for_next_birthday), style = MaterialTheme.typography.bodyMedium)
        }
    }
}

fun shareResult(context: Context, result: AgeCalculationResult) {
    val shareText = context.getString(
        R.string.share_age_text,
        result.years,
        result.months,
        result.days,
        result.hours
    )
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.share_age_subject))
        putExtra(Intent.EXTRA_TEXT, shareText)
    }
    context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_result)))
}
