package com.openeyeshost.swenproject.di

import android.content.Context
import androidx.room.Room
import com.openeyeshost.swenproject.data.local.HotelDatabase
import com.openeyeshost.swenproject.data.repository.HotelRepository
import com.openeyeshost.swenproject.data.repository.HotelRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideHotelDatabase(
        @ApplicationContext context: Context
    ): HotelDatabase {
        return Room.databaseBuilder(
            context,
            HotelDatabase::class.java,
            "hotel_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideOrderDao(database: HotelDatabase) = database.orderDao()

    @Singleton
    @Provides
    fun provideRoomDao(database: HotelDatabase) = database.roomDao()

    @Singleton
    @Provides
    fun provideMenuItemDao(database: HotelDatabase) = database.menuItemDao()

    @Singleton
    @Provides
    fun provideHotelRepository(
        orderDao: com.openeyeshost.swenproject.data.local.OrderDao,
        roomDao: com.openeyeshost.swenproject.data.local.RoomDao,
        menuItemDao: com.openeyeshost.swenproject.data.local.MenuItemDao
    ): HotelRepository = HotelRepositoryImpl(orderDao, roomDao, menuItemDao)
}
