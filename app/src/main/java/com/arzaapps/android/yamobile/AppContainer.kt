package com.arzaapps.android.yamobile

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppContainer : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}