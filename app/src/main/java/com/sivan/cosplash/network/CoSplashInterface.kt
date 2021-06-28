package com.sivan.cosplash.network

import com.sivan.cosplash.network.entity.UnsplashResponseEntity
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoSplashInterface {

    @GET("search/photos")
    suspend fun search(
        @Query("query") query : String,
        @Query("page") page : Int,
        @Query("per_page") perPage : Int) : UnsplashResponseEntity


    @GET("collections/{id}/photos")
    suspend fun getCollectionById(
        @Path("id") id : Int,
        @Query("page") page : Int,
        @Query("per_page") perPage : Int) : UnsplashResponseEntity

}