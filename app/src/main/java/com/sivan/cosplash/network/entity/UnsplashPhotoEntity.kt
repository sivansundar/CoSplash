package com.sivan.cosplash.network.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UnsplashPhotoEntity(

    @SerializedName("id")
    @Expose
    var id : String,

    @SerializedName("urls")
    @Expose
    var image_urls : ImageUrls


) {

    data class ImageUrls(

        @SerializedName("raw")
        @Expose
        var raw : String,

        @SerializedName("full")
        @Expose
        var full : String,

        @SerializedName("regular")
        @Expose
        var regular : String,

        @SerializedName("small")
        @Expose
        var small : String,

        @SerializedName("thumb")
        @Expose
        var thumb : String,


    )
}