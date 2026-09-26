package com.openeyeshost.swenproject.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [OrderEntity::class, RoomEntity::class, MenuItemEntity::class, StaffEntity::class],
    version = 1,
    exportSchema = false
)
abstract class HotelDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun roomDao(): RoomDao
    abstract fun menuItemDao(): MenuItemDao
}
