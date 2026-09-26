package com.openeyeshost.swenproject.data.repository

import com.openeyeshost.swenproject.Order
import com.openeyeshost.swenproject.Room
import com.openeyeshost.swenproject.MenuItem
import kotlinx.coroutines.flow.Flow

interface HotelRepository {
    fun getAllOrders(): Flow<List<Order>>
    fun getOrdersByStatus(status: String): Flow<List<Order>>
    suspend fun createOrder(order: Order)
    suspend fun updateOrderStatus(orderId: String, status: String)
    suspend fun deleteOrder(orderId: String)

    fun getAllRooms(): Flow<List<Room>>
    fun getRoomsByStatus(status: String): Flow<List<Room>>
    suspend fun checkInRoom(roomNumber: String, guestName: String)
    suspend fun checkOutRoom(roomNumber: String)

    fun getMenuItems(): Flow<List<MenuItem>>
    fun getMenuItemsByCategory(category: String): Flow<List<MenuItem>>
    suspend fun addMenuItem(item: MenuItem)

    suspend fun logActivity(staffName: String, action: String)
}
