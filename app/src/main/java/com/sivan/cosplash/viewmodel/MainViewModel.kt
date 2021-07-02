package com.sivan.cosplash.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sivan.cosplash.datastore.data.FilterOptions
import com.sivan.cosplash.network.entity.UnsplashPhotoEntity
import com.sivan.cosplash.repository.DataStoreRepository
import com.sivan.cosplash.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val dataStoreRepository: DataStoreRepository
    ) : ViewModel() {

    private val currentQuery = MutableLiveData(DEFAULT_SEARCH_TERM)
//    val imageSearchResult = currentQuery.switchMap { query ->
//            fetchSearchResults(query)
//        }
//
//    //live data use case
//    fun fetchSearchResults(query: String): LiveData<PagingData<UnsplashPhotoEntity>> {
//
//        return repository.searchPhotos(query)
//            .cachedIn(viewModelScope)
//    }

    fun fetchDefaultCollection() : Flow<PagingData<UnsplashPhotoEntity>> {
        return repository.getDefaultCollection().cachedIn(viewModelScope)
    }

    private val currentFilter = MutableLiveData<FilterOptions>()
    val searchOptions = currentFilter.switchMap {
        Timber.d("Filter options changed : ${it}")

        repository.searchPhotos(it).cachedIn(viewModelScope)
    }

    suspend fun updateFilterOptions(filterOptions: FilterOptions) {

        currentFilter.value = filterOptions
        Timber.d("Filter options VM : ${currentFilter.value}")
    }



    private val loadStateTextMLD = MutableLiveData<String>()
    val loadStateText : LiveData<String> = loadStateTextMLD

    fun setLoadStateText(text : String) {
        loadStateTextMLD.value = text
    }

    val preferenceFlow = dataStoreRepository.preferencesFlow

    fun updateQuery(text : String) {
        currentQuery.value = text
        viewModelScope.launch {
            dataStoreRepository.updateQuery(text)
        }
    }

    fun updateSortBy(text : String) {
        viewModelScope.launch {
            dataStoreRepository.updateSortBy(text)
        }
    }

    fun updateColor(text : String) {
        viewModelScope.launch {
            dataStoreRepository.updateColor(text)
        }
    }

    fun updateOrientation(text : String) {
        viewModelScope.launch {
            dataStoreRepository.updateOrientation(text)
        }
    }

    fun updateContentFilter(text : String) {
        viewModelScope.launch {
            dataStoreRepository.updateContentFilter(text)
        }
    }


    companion object {
        private const val DEFAULT_COLLECTION_ID = 2423569
        private const val DEFAULT_SEARCH_TERM = "cats"

    }
    }