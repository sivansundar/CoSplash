package com.sivan.cosplash.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.sivan.cosplash.network.entity.UnsplashPhotoEntity
import com.sivan.cosplash.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
    ) : ViewModel() {

    private val currentQuery = MutableLiveData(DEFAULT_SEARCH_TERM)
    val imageSearchResult = currentQuery.switchMap { query ->
            fetchSearchResults(query)
        }

    //live data use case
    fun fetchSearchResults(query: String): LiveData<PagingData<UnsplashPhotoEntity>> {
        return repository.searchPhotos(query)
            .cachedIn(viewModelScope)
    }

    fun fetchDefaultCollection() : LiveData<PagingData<UnsplashPhotoEntity>> {
        return repository.getDefaultCollection().cachedIn(viewModelScope)
    }


    fun searchImages(query : String) {
        currentQuery.value = query
        Timber.d("Search query is $currentQuery")
    }


    companion object {
        private const val DEFAULT_COLLECTION_ID = 2423569
        private const val DEFAULT_SEARCH_TERM = "cats"
    }
    }