package com.example.asser.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.History
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Calculator : Screen("calculator", "Calculator", Icons.Default.Calculate)
    object History : Screen("history", "History", Icons.Default.History)
}

val bottomNavItems = listOf(
    Screen.Calculator,
    Screen.History
)
