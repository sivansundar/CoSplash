package com.sivan.cosplash.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sivan.cosplash.data.Photo

@Entity(tableName = "favourites")
data class FavouriteCacheEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "urls")
    var image_urls: String
)

fun FavouriteCacheEntity.toPhoto(): Photo {
    return Photo(
        id = id,
        username = username,
        image_urls = image_urls
    )
}
