package com.sivan.cosplash.util

import com.sivan.cosplash.data.Photo
import com.sivan.cosplash.network.entity.UnsplashPhotoEntity
import com.sivan.cosplash.room.entity.FavouriteCacheEntity

interface OnItemClick {

    fun onItemClick(photo: Photo)
}