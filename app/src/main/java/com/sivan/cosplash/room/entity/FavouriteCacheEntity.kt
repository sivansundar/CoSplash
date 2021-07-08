package com.sivan.cosplash.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.sivan.cosplash.network.entity.UnsplashPhotoEntity

@Entity(tableName = "favourites")
data class FavouriteCacheEntity(
    @PrimaryKey
    val id : String,

    @ColumnInfo(name = "username")
    val username : String,

    @ColumnInfo(name = "urls")
    var image_urls : String
) {

}