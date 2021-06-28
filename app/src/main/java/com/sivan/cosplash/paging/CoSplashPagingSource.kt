package com.sivan.cosplash.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sivan.cosplash.network.CoSplashInterface
import com.sivan.cosplash.network.entity.UnsplashPhotoEntity
import com.sivan.cosplash.repository.TYPE_SEARCH
import okio.IOException
import retrofit2.HttpException
import timber.log.Timber


private const val DEFAULT_PAGE_INDEX = 1
private const val DEFAULT_COLLECTION = 2423569

class CoSplashPagingSource(
    private val coSplashInterface: CoSplashInterface,
    private val query: String?,
    private val type : Int
) : PagingSource<Int, UnsplashPhotoEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhotoEntity> {
        val position = params.key ?: DEFAULT_PAGE_INDEX

       return if (type == TYPE_SEARCH) loadSearchElements(query.toString(), position, params.loadSize) else loadCollectionElements(position, params.loadSize)

    }

    private suspend fun loadCollectionElements(position: Int, loadSize: Int) : LoadResult<Int, UnsplashPhotoEntity> {
        return try {
            val response = coSplashInterface.getCollectionById(DEFAULT_COLLECTION, position, loadSize)

            Timber.d("TYPE : ${type}")

            LoadResult.Page(
                data = response,
                prevKey = computePreviousKey(position),
                nextKey = computeNextKey(response, position)
            )
        } catch (exception : HttpException) {
            Timber.d("Exception : ${exception.message()}")
            return LoadResult.Error(exception)
        } catch (exception : IOException) {
            Timber.d("Exception : ${exception.message}")
            return LoadResult.Error(exception)
        }    }



    private suspend fun loadSearchElements(queryString: String, position: Int, loadSize: Int): LoadResult<Int, UnsplashPhotoEntity> {
        return try {
            val response = coSplashInterface.search(queryString, position, loadSize)
            val result = response.results
            Timber.d("TYPE : ${type}")

            LoadResult.Page(
                data = result,
                prevKey = computePreviousKey(position),
                nextKey = computeNextKey(result, position)
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


    private fun computeNextKey(result: List<UnsplashPhotoEntity>, position: Int) =
        if(result.isEmpty()) null else position + 1


    private fun computePreviousKey(position: Int) =
        if (position == DEFAULT_PAGE_INDEX) null else position - 1


}