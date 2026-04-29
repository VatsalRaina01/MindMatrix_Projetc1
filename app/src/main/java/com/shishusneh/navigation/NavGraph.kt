package com.shishusneh.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Navigation routes for the app.
 */
sealed class Screen(val route: String) {
    // Onboarding
    data object LanguageSelection : Screen("language_selection")
    data object Onboarding : Screen("onboarding")
    data object ProfileSetup : Screen("profile_setup")

    // Main screens
    data object Home : Screen("home")
    data object Growth : Screen("growth")
    data object Vaccination : Screen("vaccination")
    data object Milestones : Screen("milestones")
    data object Feeding : Screen("feeding")
    data object Settings : Screen("settings")
    data object GenAIChat : Screen("genai_chat")
    data object EditProfile : Screen("edit_profile")
}

/**
 * Bottom navigation items.
 */
data class BottomNavItem(
    val screen: Screen,
    val labelResId: Int,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Home, com.shishusneh.R.string.nav_home, Icons.Default.Home),
    BottomNavItem(Screen.Growth, com.shishusneh.R.string.growth, Icons.Default.BarChart),
    BottomNavItem(Screen.Vaccination, com.shishusneh.R.string.vaccines, Icons.Default.Vaccines),
    BottomNavItem(Screen.Milestones, com.shishusneh.R.string.milestones, Icons.Default.CheckCircle),
    BottomNavItem(Screen.Feeding, com.shishusneh.R.string.feeding, Icons.Default.Restaurant),
)
