package com.sivan.cosplash.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
data class Photo(

    val id : String,
    val username : String,
    var image_urls : String
) : Parcelable {
}