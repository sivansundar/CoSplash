package com.sivan.cosplash.network.entity

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UnsplashPhotoEntity(

    @SerializedName("id")
    @Expose
    var id : String,

    @SerializedName("urls")
    @Expose
    var image_urls : ImageUrls


) : Parcelable {

    @Parcelize
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


    ) : Parcelable
}