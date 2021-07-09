package com.sivan.cosplash.room.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import com.sivan.cosplash.room.entity.FavouriteCacheEntity

@Dao
interface FavouritesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE) // Check while using fragments
    suspend fun insert(favouriteCacheEntity: FavouriteCacheEntity) : Long

    @Query("DELETE FROM favourites WHERE id = :id")
    suspend fun deleteItem(id: String) : Int

    @Query("SELECT EXISTS (SELECT 1 FROM favourites WHERE id = :id)")
    suspend fun exists(id: String): Boolean

    @Query("SELECT * FROM favourites")
    fun getAllFavourites() : PagingSource<Int, FavouriteCacheEntity>

    @Query("SELECT * from favourites WHERE id=:id")
    suspend fun getFavouriteItem(id : String) : FavouriteCacheEntity

}