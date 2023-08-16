package com.rahul.assignment

import android.app.Application
import com.rahul.networkclient.constant.ApiUrls
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by abrol at 09/08/23.
 */
@HiltAndroidApp
class DogApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ApiUrls.initialize( getString(com.rahul.networkclient.R.string.baseurl))
    }
}