package com.example.asser.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.asser.ui.screens.CalculatorScreen
import com.example.asser.ui.screens.HistoryScreen

@Composable
fun AsserNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Calculator.route,
        modifier = modifier
    ) {
        composable(Screen.Calculator.route) {
            CalculatorScreen()
        }
        composable(Screen.History.route) {
            HistoryScreen()
        }
    }
}
