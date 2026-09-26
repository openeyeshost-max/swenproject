package com.openeyeshost.swenproject

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HotelViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState

    fun setRole(role: StaffRole) {
        _uiState.value = _uiState.value.copy(currentRole = role)
    }

    fun login(staffId: String, pin: String): Boolean {
        val staff = _uiState.value.staffList.find { it.id == staffId }
        val valid = staff != null && pin.isNotBlank()
        if (valid) {
            _uiState.value = _uiState.value.copy(
                currentRole = staff.role,
                selectedStaff = staff,
                logs = listOf(
                    ActivityLog("${System.currentTimeMillis()}", staff.name, "Logged in successfully", "Now")
                ) + _uiState.value.logs
            )
        }
        return valid
    }

    fun createOrder(type: String, number: String, waiterName: String, items: List<OrderItem>) {
        val newOrder = Order(
            id = "O-${System.currentTimeMillis()}",
            type = type,
            number = number,
            waiterName = waiterName,
            status = "Pending",
            items = items
        )
        _uiState.value = _uiState.value.copy(
            orders = listOf(newOrder) + _uiState.value.orders,
            logs = listOf(
                ActivityLog("${System.currentTimeMillis()}", waiterName, "Created ${type} order $number", "Now")
            ) + _uiState.value.logs
        )
    }

    fun updateOrderStatus(orderId: String, nextStatus: String) {
        _uiState.value = _uiState.value.copy(
            orders = _uiState.value.orders.map {
                if (it.id == orderId) it.copy(status = nextStatus) else it
            },
            logs = listOf(
                ActivityLog("${System.currentTimeMillis()}", "System", "Order $orderId changed to $nextStatus", "Now")
            ) + _uiState.value.logs
        )
    }

    fun checkInRoom(roomNumber: String, guestName: String) {
        _uiState.value = _uiState.value.copy(
            rooms = _uiState.value.rooms.map {
                if (it.number == roomNumber) it.copy(status = "Occupied", guestName = guestName) else it
            },
            logs = listOf(
                ActivityLog("${System.currentTimeMillis()}", "Front Desk", "Checked in guest $guestName to room $roomNumber", "Now")
            ) + _uiState.value.logs
        )
    }

    fun checkOutRoom(roomNumber: String) {
        _uiState.value = _uiState.value.copy(
            rooms = _uiState.value.rooms.map {
                if (it.number == roomNumber) it.copy(status = "Cleaning", guestName = "-") else it
            },
            logs = listOf(
                ActivityLog("${System.currentTimeMillis()}", "Front Desk", "Checked out room $roomNumber", "Now")
            ) + _uiState.value.logs
        )
    }

    fun saveHotelSettings(settings: HotelSettings) {
        _uiState.value = _uiState.value.copy(hotelSettings = settings)
    }

    fun addMenuItem(item: MenuItem) {
        _uiState.value = _uiState.value.copy(
            menuItems = listOf(item) + _uiState.value.menuItems
        )
    }
}
