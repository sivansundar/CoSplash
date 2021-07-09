package com.sivan.cosplash.network.entity

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.sivan.cosplash.data.Photo
import com.sivan.cosplash.room.entity.FavouriteCacheEntity
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class UnsplashPhotoEntity(

    @SerializedName("id")
    @Expose
    var id : String,

    @SerializedName("urls")
    @Expose
    var image_urls : ImageUrls,

    @SerializedName("user")
    @Expose
    var user: UserEntity


) {

    @Serializable
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

fun Photo.toEntity(): FavouriteCacheEntity {
    return FavouriteCacheEntity(
        id = this.id,
        username = this.username,
        image_urls = this.image_urls
    )
}

fun UnsplashPhotoEntity.toPhoto(): Photo {
    return Photo(
        id = this.id,
        username = this.user.username,
        image_urls = Json.encodeToString(this.image_urls)
    )
}