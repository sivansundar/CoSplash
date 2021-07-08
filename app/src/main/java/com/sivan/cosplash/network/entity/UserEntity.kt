package com.sivan.cosplash.network.entity

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class UserEntity(
    @SerializedName("id")
    @Expose
    val id : String,

    @SerializedName("username")
    @Expose
    val username : String
) : Parcelable {

}