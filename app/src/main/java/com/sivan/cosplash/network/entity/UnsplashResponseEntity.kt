package com.sivan.cosplash.network.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UnsplashResponseEntity(
    @SerializedName("results")
    @Expose
    var results: List<UnsplashPhotoEntity>

)
