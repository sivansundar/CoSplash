package com.sivan.cosplash.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sivan.cosplash.room.dao.FavouritesDao
import com.sivan.cosplash.room.entity.FavouriteCacheEntity

@Database(entities = [FavouriteCacheEntity::class], version = 1)
abstract class CoSplashDatabase : RoomDatabase() {

    abstract fun favDao() : FavouritesDao
}