package com.sivan.cosplash.network

import com.sivan.cosplash.network.entity.UnsplashPhotoEntity
import com.sivan.cosplash.network.entity.UnsplashResponseEntity
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface CoSplashInterface {

    @GET("search/photos")
    suspend fun search(
        @QueryMap() options: Map<String, String?>,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("color") color: String?
    ) : UnsplashResponseEntity


    @GET("search/photos")
    suspend fun search2(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @QueryMap options : Map<String, String?>
    ) : UnsplashResponseEntity


    @GET("collections/{id}/photos")
    suspend fun getCollectionById(
        @Path("id") id : Int,
        @Query("page") page : Int,
        @Query("per_page") perPage : Int) : List<UnsplashPhotoEntity>

}