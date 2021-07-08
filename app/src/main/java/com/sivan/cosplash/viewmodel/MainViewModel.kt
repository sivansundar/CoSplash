package com.sivan.cosplash.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sivan.cosplash.data.FilterOptions
import com.sivan.cosplash.network.entity.UnsplashPhotoEntity
import com.sivan.cosplash.repository.MainRepository
import com.sivan.cosplash.room.entity.FavouriteCacheEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
) : ViewModel() {

    /**
     * @param currentFilter
     * @param searchOptions
     * We update the currentFilter object to initiate a query. Whenever this value is changed, we execute the searchPhotos(filterOptions) function from our repository class
     * which is responsible for initiating a GET request and returns a PagingData object.
     *
     * We observe changes on the searchOptions variable in our fragment and update the adapter data accordingly.
     *
     * */

    private val _selectionState = MutableLiveData<Boolean>(false) // Holds an initial state of false
    val selectionState : LiveData<Boolean> = _selectionState

    suspend fun getFavItem(id : String) {
        setSelectionState(repository.checkIfItemExists(id))
    }

    suspend fun addFavouriteItem(item : UnsplashPhotoEntity) {
        repository.insertFavouriteItem(item)
        setSelectionState(true)
    }

    suspend fun removeFavouriteItem(id : String) {
        _selectionState.value = repository.removeFavouriteItem(id)
        setSelectionState(false)

    }

    private fun setSelectionState(state : Boolean) {
        Timber.d("Selection state from VM : ${state}")
        _selectionState.value = state
    }

    private val currentFilter = MutableLiveData<FilterOptions>()
    val searchOptions = currentFilter.switchMap {
        Timber.d("Filter options : ${it}")
        repository.searchPhotos(it).cachedIn(viewModelScope)
    }

    fun updateFilterOptions(filterOptions: FilterOptions) {
        viewModelScope.launch {
            currentFilter.value = filterOptions
            Timber.d("Filter options VM : ${currentFilter.value}")
        }

    }

    fun updateFilter() {
        viewModelScope.launch {
            val item = FilterOptions(
                query = query.value,
                sort_by = sort_by.value,
                color = color.value,
                content_filter = content_filter.value,
                orientation = orientation.value
            )
            currentFilter.value = item
        }
    }

    /**
     * Responsible for loading the default star wars collection. Static data for search term
     **/

    fun fetchDefaultCollection(): Flow<PagingData<UnsplashPhotoEntity>> {
        return repository.getDefaultCollection().cachedIn(viewModelScope)
    }

    private val loadStateTextMLD = MutableLiveData<String>()
    val loadStateText: LiveData<String> = loadStateTextMLD

    fun setLoadStateText(text: String) {
        loadStateTextMLD.value = text
    }

    private val query = MutableLiveData<String>()
    val _query: LiveData<String> = query
    fun updateQuery(text: String) {
        viewModelScope.launch {
            query.value = text
        }
    }

    private val sort_by = MutableLiveData<String>()
    fun updateSortBy(text: String) {
        viewModelScope.launch {
            sort_by.value = text
        }
    }

    private val color = MutableLiveData<String>()
    fun updateColor(text: String) {
        viewModelScope.launch {
            color.value = text
        }
    }

    private val orientation = MutableLiveData<String>()
    fun updateOrientation(text: String) {
        viewModelScope.launch {
            orientation.value = text
        }
    }

    private val content_filter = MutableLiveData<String>()
    fun updateContentFilter(text: String) {
        viewModelScope.launch {
            content_filter.value = text
        }
    }


    fun clearFilterOptions() {
        updateContentFilter("")
        updateOrientation("")
        updateSortBy("")
        updateColor("")
    }


    companion object {
        /**
         * @param DEFAULT_COLLECTION_ID points to the default "Star Wars" collection
         */
        private const val DEFAULT_COLLECTION_ID = 2423569

    }

}