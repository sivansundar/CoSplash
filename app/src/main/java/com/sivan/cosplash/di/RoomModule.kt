package com.sivan.cosplash.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sivan.cosplash.room.CoSplashDatabase
import com.sivan.cosplash.room.dao.FavouritesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideCoSplashDatabase(@ApplicationContext context : Context) : CoSplashDatabase {
        return Room.databaseBuilder(
            context,
            CoSplashDatabase::class.java,
            "Cosplash_database"
        ).build()
    }


    @Provides
    @Singleton
    fun provideFavouritesDao(coSplashDatabase: CoSplashDatabase) : FavouritesDao {
        return coSplashDatabase.favDao()
    }
}