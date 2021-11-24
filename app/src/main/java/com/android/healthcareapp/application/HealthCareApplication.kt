package com.android.healthcareapp.application

import android.app.Application
import com.android.healthcareapp.BuildConfig
import timber.log.Timber

class HealthCareApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}