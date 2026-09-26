package com.openeyeshost.swenproject

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun HotelManagerApp() {
    val navController = rememberNavController()
    var currentRole by remember { mutableStateOf(StaffRole.ADMIN) }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { role ->
                    currentRole = role
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("dashboard") {
            DashboardScreen(
                role = currentRole,
                onNavigate = { destination ->
                    navController.navigate(destination)
                },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                }
            )
        }

        composable("orders") { OrdersScreen() }
        composable("kitchen") { KitchenScreen() }
        composable("billing") { BillingScreen() }
        composable("rooms") { RoomsScreen() }
        composable("menu") { MenuScreen() }
        composable("staff") { StaffScreen() }
        composable("admin") { AdminDashboardScreen() }
        composable("settings") { SettingsScreen() }
        composable("profile") { ProfileScreen() }
    }
}
