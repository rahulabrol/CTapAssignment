package com.rahul.assignment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahul.networkclient.DogImageManager
import com.rahul.networkclient.model.DogImage
import com.rahul.networkclient.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by abrol at 09/08/23.
 */
@HiltViewModel
class DogViewModel @Inject constructor(private val dogImageManager: DogImageManager) : ViewModel() {

    private val nextImageLiveData = MutableLiveData<DogImage>()
    private val prevImageLiveData = MutableLiveData<DogImage>()
    private val imageListLiveData = MutableLiveData<List<String>>()
    private val _error: MutableLiveData<String> = MutableLiveData<String>()
    val isPrevButtonEnable = MutableLiveData(false)
    val errorLive: LiveData<String>
        get() = _error

    private var lastDogImage: DogImage? = null

    init {
        // fetch first image
        doAction()
    }

    internal fun getNextImage(): LiveData<DogImage> {
        return nextImageLiveData
    }

    internal fun getPrevImage(): LiveData<DogImage> {
        return prevImageLiveData
    }

    internal fun doAction() {
        lastDogImage?.let {
            isPrevButtonEnable.postValue(true)
        }
        viewModelScope.launch {
            when (val result = dogImageManager.getImage()) {
                is Resource.Success -> {
                    lastDogImage = result.data
                    nextImageLiveData.postValue(lastDogImage)
                    println("Added Image: -------------> ${lastDogImage?.id}")
                }

                is Resource.Error -> {
                    _error.postValue(result.message)
                }
            }
        }

    }

    internal fun getImages(number: Int) {
        viewModelScope.launch {
            when (val result = dogImageManager.getImages(number = number)) {
                is Resource.Success -> {
                    imageListLiveData.postValue(result.data?.message)
                }

                is Resource.Error -> {
                    _error.postValue(result.message)
                }
            }
        }
    }

    fun getPreviousImage() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                println("prev Image: -------------> ${lastDogImage?.id}")
                if (lastDogImage?.id!! > 0L) {
                    val prevIndex = lastDogImage?.id!! - 1
                    lastDogImage = dogImageManager.getPreviousImage(prevIndex.toLong())
                    prevImageLiveData.postValue(lastDogImage)
                } else {
                    isPrevButtonEnable.postValue(false)
                }
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }
}