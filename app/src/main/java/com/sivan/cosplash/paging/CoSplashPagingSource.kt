package com.sivan.cosplash.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sivan.cosplash.network.CoSplashInterface
import com.sivan.cosplash.network.entity.UnsplashPhotoEntity
import com.sivan.cosplash.repository.TYPE_COLLECTION
import okio.IOException
import retrofit2.HttpException
import timber.log.Timber


private const val DEFAULT_PAGE_INDEX = 1

class CoSplashPagingSource(
    private val coSplashInterface: CoSplashInterface,
    private val query: String?
) : PagingSource<Int, UnsplashPhotoEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhotoEntity> {
        val position = params.key ?: DEFAULT_PAGE_INDEX

        return try {
            val response = coSplashInterface.search(query.toString(), position, params.loadSize)
            val result = response.results
            Timber.d("RESULT : ${result}")

            LoadResult.Page(
                data = result,
                prevKey = if (position == DEFAULT_PAGE_INDEX) null else position - 1,
                nextKey = if(result.isEmpty()) null else position + 1
            )
        } catch (exception : HttpException) {
            Timber.d("Exception : ${exception.message()}")
            return LoadResult.Error(exception)
        } catch (exception : IOException) {
            Timber.d("Exception : ${exception.message}")
            return LoadResult.Error(exception)
        }


    }



    override fun getRefreshKey(state: PagingState<Int, UnsplashPhotoEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}