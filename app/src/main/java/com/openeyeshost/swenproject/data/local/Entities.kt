package com.openeyeshost.swenproject.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val id: String,
    val type: String,
    val number: String,
    val waiterName: String,
    val status: String,
    val itemsJson: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "rooms")
data class RoomEntity(
    @PrimaryKey val number: String,
    val type: String,
    val ratePerNight: Double,
    val status: String,
    val guestName: String = "-"
)

@Entity(tableName = "menu_items")
data class MenuItemEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val price: Double,
    val isAvailable: Boolean = true
)

@Entity(tableName = "staff")
data class StaffEntity(
    @PrimaryKey val id: String,
    val name: String,
    val role: String,
    val phone: String,
    val isActive: Boolean = true
)
