package com.sivan.cosplash.viewmodel

import androidx.lifecycle.ViewModel
import com.sivan.cosplash.repository.MainRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repository: MainRepository
    ) : ViewModel() {


    }