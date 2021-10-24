package com.sivan.cosplash.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.sivan.cosplash.data.FilterOptions
import com.sivan.cosplash.network.CoSplashInterface
import com.sivan.cosplash.network.entity.UnsplashPhotoEntity
import com.sivan.cosplash.paging.CoSplashPagingSource
import com.sivan.cosplash.paging.SplashPagingSource
import com.sivan.cosplash.paging.SplashPagingSourceV2
import com.sivan.cosplash.room.dao.FavouritesDao
import com.sivan.cosplash.room.entity.FavouriteCacheEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


const val TYPE_COLLECTION = 0
const val TYPE_SEARCH = 1
private const val DEFAULT_PAGE_INDEX = 1

private const val EXISTS = -1

@Singleton
class MainRepository @Inject constructor(
    private val coSplashInterface: CoSplashInterface,
    private val favouritesDao: FavouritesDao) {


//    suspend fun getFavourites(): LiveData<List<FavouriteCacheEntity>> {
//        return favouritesDao.getAllFavourites()
//    }

    suspend fun removeFavouriteItem(id : String): Boolean {
        val removeItem = favouritesDao.deleteItem(id)
        if (removeItem>0) {
            Timber.d("REMOVED : $removeItem")
        } else {
            Timber.d("REMOVED NOT : $removeItem")
        }
        return removeItem > 0
    }

    suspend fun insertFavouriteItem(item: FavouriteCacheEntity): FavouriteCacheEntity {
        val imageUrls = Json.encodeToString(item.image_urls)

        val itemexists = favouritesDao.exists(item.id)

        if (!itemexists) {
            /**
             * If the photo does not exist in the database, then we perform the insert operation.
             * If the photo exists, then we dont execute this block and it is understood that the photo exists and therefore we go ahead
             * and get the photo based on the id.
             * */
            val insertItem = favouritesDao.insert(item)
            Timber.d("Item status : $insertItem")
        }

        val itemFromDb = favouritesDao.getFavouriteItem(item.id)

        Timber.d("Item from db : $itemFromDb")

        return itemFromDb
    }

    suspend fun getPagedFavList(): Flow<PagingData<FavouriteCacheEntity>> {
        return Pager(
            PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                maxSize = 100)) {
            favouritesDao.getAllFavourites() }
            .flow
    }

    suspend fun checkIfItemExists(id: String): Boolean {
        val res = favouritesDao.exists(id)
        Timber.d("IFav item : $res : $id")
        return res
    }

    fun getDefaultCollection() =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SplashPagingSourceV2(coSplashInterface, null, TYPE_COLLECTION) }
        ).flow


     fun searchPhotos(query: FilterOptions): Flow<PagingData<UnsplashPhotoEntity>> {

        return Pager(
             config = PagingConfig(
                 pageSize = 20,
                 maxSize = 100,
                 enablePlaceholders = false
             ),
             pagingSourceFactory = { SplashPagingSourceV2(coSplashInterface, query, TYPE_SEARCH) }
         ).flow

     }



}