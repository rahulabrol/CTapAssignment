package com.rahul.assignment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahul.networkclient.DogImageManager
import com.rahul.networkclient.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by abrol at 09/08/23.
 */
@HiltViewModel
class DogViewModel @Inject constructor(private val dogImageManager: DogImageManager) : ViewModel() {

    private val nextImageLiveData = MutableLiveData<String>()
    private val imageListLiveData = MutableLiveData<List<String>>()
    private val _error: MutableLiveData<String> = MutableLiveData<String>()
    val errorLive: LiveData<String>
        get() = _error

    init {
        // fetch first image
        doAction()
    }

    internal fun getNextImage(): LiveData<String> {
        return nextImageLiveData
    }

    internal fun doAction() {
        viewModelScope.launch {
            when (val result = dogImageManager.getImage()) {
                is Resource.Success -> {
                    nextImageLiveData.value = result.data?.imageUrl
                }

                is Resource.Error -> {
                    _error.value = result.message
                }
            }
        }

    }

    internal fun getImages(number: Int) {
        viewModelScope.launch {
            when (val result = dogImageManager.getImages(number = number)) {
                is Resource.Success -> {
                    imageListLiveData.value = result.data?.message
                }

                is Resource.Error -> {
                    _error.value = result.message
                }
            }
        }
    }
}