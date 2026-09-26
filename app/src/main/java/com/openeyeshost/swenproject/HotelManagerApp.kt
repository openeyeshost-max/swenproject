package com.openeyeshost.swenproject

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.BedroomBaby
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.ui.graphics.vector.ImageVector

enum class StaffRole(val label: String) {
    ADMIN("Admin"),
    WAITER("Waiter"),
    KITCHEN_STAFF("Kitchen Staff"),
    BILLING_STAFF("Billing Staff"),
    RECEPTIONIST("Receptionist")
}

data class Staff(
    val id: String,
    val name: String,
    val role: StaffRole,
    val phone: String,
    val isActive: Boolean = true
)

data class MenuItem(
    val id: String,
    val name: String,
    val category: String,
    val price: Double,
    val isAvailable: Boolean = true
)

data class OrderItem(
    val name: String,
    val qty: Int,
    val price: Double
)

data class Order(
    val id: String,
    val type: String,
    val number: String,
    val waiterName: String,
    val status: String,
    val items: List<OrderItem>
)

data class Room(
    val number: String,
    val type: String,
    val ratePerNight: Double,
    val status: String,
    val guestName: String = "-"
)

data class ActivityLog(
    val id: String,
    val staffName: String,
    val action: String,
    val time: String
)

data class HotelSettings(
    val name: String = "Royal Horizon Hotel",
    val address: String = "Main Market Road, Jaipur",
    val phone: String = "+91 98765 43210",
    val gstin: String = "08ABCDE1234F1Z5",
    val taxPercent: Double = 5.0,
    val serviceChargePercent: Double = 10.0
)

object MockData {
    val staffList = listOf(
        Staff("ADM-001", "Priya Sharma", StaffRole.ADMIN, "+91 98765 12345", true),
        Staff("WT-101", "Rohit Verma", StaffRole.WAITER, "+91 98765 22222", true),
        Staff("KT-201", "Amit Singh", StaffRole.KITCHEN_STAFF, "+91 98765 33333", true),
        Staff("BL-301", "Neha Gupta", StaffRole.BILLING_STAFF, "+91 98765 44444", true),
        Staff("RC-401", "Karan Mehta", StaffRole.RECEPTIONIST, "+91 98765 55555", true)
    )

    val menuItems = listOf(
        MenuItem("m1", "Paneer Tikka", "Starters", 320.0, true),
        MenuItem("m2", "Veg Biryani", "Main Course", 450.0, true),
        MenuItem("m3", "Masala Dosa", "Breakfast", 180.0, true),
        MenuItem("m4", "Cold Coffee", "Beverages", 140.0, true),
        MenuItem("m5", "Chocolate Cake", "Desserts", 220.0, true)
    )

    val orders = listOf(
        Order("O-101", "Table", "T-12", "Rohit Verma", "Pending", listOf(OrderItem("Paneer Tikka", 2, 320.0))),
        Order("O-102", "Room", "R-505", "Rohit Verma", "Preparing", listOf(OrderItem("Veg Biryani", 1, 450.0), OrderItem("Cold Coffee", 2, 140.0))),
        Order("O-103", "Table", "T-08", "Neha", "Ready", listOf(OrderItem("Masala Dosa", 3, 180.0))),
        Order("O-104", "Room", "R-210", "Rohit Verma", "Billed", listOf(OrderItem("Chocolate Cake", 1, 220.0)))
    )

    val rooms = listOf(
        Room("101", "Single", 1800.0, "Vacant"),
        Room("205", "Double", 3200.0, "Occupied", "Aman Shah"),
        Room("302", "Suite", 5200.0, "Cleaning"),
        Room("405", "Double", 3000.0, "Maintenance")
    )

    val logs = listOf(
        ActivityLog("1", "Priya Sharma", "Logged in", "09:10 AM"),
        ActivityLog("2", "Rohit Verma", "Created Table Order T-12", "09:20 AM"),
        ActivityLog("3", "Amit Singh", "Marked order as Preparing", "09:45 AM"),
        ActivityLog("4", "Neha Gupta", "Generated invoice", "10:15 AM")
    )

    val hotelSettings = HotelSettings()
}

sealed class ScreenRoute(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : ScreenRoute("dashboard", "Dashboard", Icons.Default.Home)
    object Orders : ScreenRoute("orders", "Orders", Icons.Default.ShoppingCart)
    object Kitchen : ScreenRoute("kitchen", "Kitchen", Icons.Default.Fastfood)
    object Billing : ScreenRoute("billing", "Billing", Icons.Default.Receipt)
    object Rooms : ScreenRoute("rooms", "Rooms", Icons.Default.BedroomBaby)
    object Menu : ScreenRoute("menu", "Menu", Icons.Default.RestaurantMenu)
    object Staff : ScreenRoute("staff", "Staff", Icons.Default.Group)
    object Admin : ScreenRoute("admin", "Admin", Icons.Default.Analytics)
    object Settings : ScreenRoute("settings", "Settings", Icons.Default.Settings)
    object Profile : ScreenRoute("profile", "Profile", Icons.Default.AccountCircle)
    object Login : ScreenRoute("login", "Login", Icons.Default.SupervisorAccount)
}
