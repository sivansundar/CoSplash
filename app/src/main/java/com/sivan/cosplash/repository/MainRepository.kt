package com.sivan.cosplash.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.sivan.cosplash.network.CoSplashInterface
import com.sivan.cosplash.network.entity.UnsplashPhotoEntity
import com.sivan.cosplash.paging.CoSplashPagingSource
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


private const val DEFAULT_COLLECTION = 2423569
const val TYPE_COLLECTION = 0
const val TYPE_SEARCH = 1

@Singleton
class MainRepository @Inject constructor(private val coSplashInterface: CoSplashInterface) {

//    fun getDefaultCollection() =
//        Pager(
//            config = PagingConfig(
//                pageSize = 20,
//                maxSize = 100,
//                enablePlaceholders = false
//            ),
//            pagingSourceFactory = { CoSplashPagingSource(coSplashInterface, TYPE_COLLECTION, null) }
//        ).liveData


     fun searchPhotos(query : String) =

       Pager(
             config = PagingConfig(
                 pageSize = 20,
                 maxSize = 100,
                 enablePlaceholders = false
             ),
             pagingSourceFactory = { CoSplashPagingSource(coSplashInterface, query) }
         ).liveData




}