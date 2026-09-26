package com.openeyeshost.swenproject.data.repository

import com.openeyeshost.swenproject.Order
import com.openeyeshost.swenproject.Room
import com.openeyeshost.swenproject.MenuItem
import com.openeyeshost.swenproject.OrderItem
import com.openeyeshost.swenproject.data.local.OrderDao
import com.openeyeshost.swenproject.data.local.RoomDao
import com.openeyeshost.swenproject.data.local.MenuItemDao
import com.openeyeshost.swenproject.data.local.OrderEntity
import com.openeyeshost.swenproject.data.local.RoomEntity
import com.openeyeshost.swenproject.data.local.MenuItemEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

class HotelRepositoryImpl(
    private val orderDao: OrderDao,
    private val roomDao: RoomDao,
    private val menuItemDao: MenuItemDao
) : HotelRepository {

    override fun getAllOrders(): Flow<List<Order>> {
        return orderDao.getAllOrders().map { entities ->
            entities.map { it.toOrder() }
        }
    }

    override fun getOrdersByStatus(status: String): Flow<List<Order>> {
        return orderDao.getOrdersByStatus(status).map { entities ->
            entities.map { it.toOrder() }
        }
    }

    override suspend fun createOrder(order: Order) {
        val itemsJson = Json.encodeToString(order.items)
        val entity = OrderEntity(
            id = order.id,
            type = order.type,
            number = order.number,
            waiterName = order.waiterName,
            status = order.status,
            itemsJson = itemsJson
        )
        orderDao.insertOrder(entity)
    }

    override suspend fun updateOrderStatus(orderId: String, status: String) {
        val order = orderDao.getOrderById(orderId)
        order?.let {
            orderDao.updateOrder(it.copy(status = status))
        }
    }

    override suspend fun deleteOrder(orderId: String) {
        val order = orderDao.getOrderById(orderId)
        order?.let { orderDao.deleteOrder(it) }
    }

    override fun getAllRooms(): Flow<List<Room>> {
        return roomDao.getAllRooms().map { entities ->
            entities.map { it.toRoom() }
        }
    }

    override fun getRoomsByStatus(status: String): Flow<List<Room>> {
        return roomDao.getRoomsByStatus(status).map { entities ->
            entities.map { it.toRoom() }
        }
    }

    override suspend fun checkInRoom(roomNumber: String, guestName: String) {
        val room = roomDao.getAllRooms().collect { rooms ->
            rooms.find { it.number == roomNumber }?.let {
                roomDao.updateRoom(it.copy(status = "Occupied", guestName = guestName))
            }
        }
    }

    override suspend fun checkOutRoom(roomNumber: String) {
        val room = roomDao.getAllRooms().collect { rooms ->
            rooms.find { it.number == roomNumber }?.let {
                roomDao.updateRoom(it.copy(status = "Cleaning", guestName = "-"))
            }
        }
    }

    override fun getMenuItems(): Flow<List<MenuItem>> {
        return menuItemDao.getAllMenuItems().map { entities ->
            entities.map { it.toMenuItem() }
        }
    }

    override fun getMenuItemsByCategory(category: String): Flow<List<MenuItem>> {
        return menuItemDao.getMenuItemsByCategory(category).map { entities ->
            entities.map { it.toMenuItem() }
        }
    }

    override suspend fun addMenuItem(item: MenuItem) {
        val entity = MenuItemEntity(
            id = item.id,
            name = item.name,
            category = item.category,
            price = item.price,
            isAvailable = item.isAvailable
        )
        menuItemDao.insertMenuItem(entity)
    }

    override suspend fun logActivity(staffName: String, action: String) {
        // Implement logging to Firebase or local DB
    }

    private fun OrderEntity.toOrder(): Order {
        val items = try {
            Json.decodeFromString<List<OrderItem>>(itemsJson)
        } catch (e: Exception) {
            emptyList()
        }
        return Order(id, type, number, waiterName, status, items)
    }

    private fun RoomEntity.toRoom(): Room {
        return Room(number, type, ratePerNight, status, guestName)
    }

    private fun MenuItemEntity.toMenuItem(): MenuItem {
        return MenuItem(id, name, category, price, isAvailable)
    }
}
