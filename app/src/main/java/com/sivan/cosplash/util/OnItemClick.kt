package com.sivan.cosplash.util

import com.sivan.cosplash.network.entity.UnsplashPhotoEntity

interface OnItemClick {

    fun onItemClick(photo : UnsplashPhotoEntity)
}