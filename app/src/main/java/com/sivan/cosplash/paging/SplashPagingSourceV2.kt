package com.sivan.cosplash.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sivan.cosplash.data.FilterOptions
import com.sivan.cosplash.network.CoSplashInterface
import com.sivan.cosplash.network.entity.UnsplashPhotoEntity
import okio.IOException
import retrofit2.HttpException
import timber.log.Timber

class SplashPagingSourceV2(
    private val coSplashInterface: CoSplashInterface,
    private val query: FilterOptions?,
    private val type: Int
) : PagingSource<Int, UnsplashPhotoEntity>() {

    override fun getRefreshKey(state: PagingState<Int, UnsplashPhotoEntity>): Int?
    {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhotoEntity> {
        return try {
            val nextPage = params.key ?: 1
            val userList = coSplashInterface.getCollectionById(id = DEFAULT_COLLECTION, page = nextPage, perPage = params.loadSize)
            Timber.d("RESPONSE : $userList")
            LoadResult.Page(
                data = userList,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (userList.isEmpty()) null else nextPage + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    companion object {
        private const val DEFAULT_PAGE_INDEX = 1
        private const val DEFAULT_COLLECTION = 2423569

    }
}