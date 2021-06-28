package com.sivan.cosplash.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.sivan.cosplash.network.CoSplashInterface
import com.sivan.cosplash.network.entity.UnsplashPhotoEntity
import com.sivan.cosplash.paging.CoSplashPagingSource
import okio.IOException
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


const val TYPE_COLLECTION = 0
const val TYPE_SEARCH = 1
private const val DEFAULT_PAGE_INDEX = 1


@Singleton
class MainRepository @Inject constructor(private val coSplashInterface: CoSplashInterface) {

    fun getDefaultCollection() =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CoSplashPagingSource(coSplashInterface, null, TYPE_COLLECTION) }
        ).liveData


     fun searchPhotos(query : String) =

       Pager(
             config = PagingConfig(
                 pageSize = 20,
                 maxSize = 100,
                 enablePlaceholders = false
             ),
             pagingSourceFactory = { CoSplashPagingSource(coSplashInterface, query, TYPE_SEARCH) }
         ).liveData


}